package com.innodino.blocks.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialProber

/**
 * Utility to check if InnoDino Kit (USB serial) is connected and configure it.
 */
object DinoKitConnectionChecker {
    // Added listener mechanism for connection status updates.
    private const val USB_PERMISSION_ACTION = "com.innodino.blocks.USB_PERMISSION"

    private val listeners = mutableListOf<(Boolean) -> Unit>()

    fun isKitConnected(context: Context): Boolean {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as? UsbManager
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        return availableDrivers.isNotEmpty()
    }

    /**
     * Checks if we have permission for the USB device
     */
    fun hasUsbPermission(context: Context): Boolean {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as? UsbManager ?: return false
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        
        if (availableDrivers.isEmpty()) return false
        
        val device = availableDrivers[0].device
        return usbManager.hasPermission(device)
    }

    /**
     * Requests USB permission for the device
     */
    fun requestUsbPermission(context: Context) {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as? UsbManager ?: return
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        
        if (availableDrivers.isEmpty()) {
            Log.w("DinoKit", "No USB devices found to request permission for")
            return
        }
        
        val device = availableDrivers[0].device
        if (!usbManager.hasPermission(device)) {
            val permissionIntent = PendingIntent.getBroadcast(
                context, 0, Intent(USB_PERMISSION_ACTION), PendingIntent.FLAG_IMMUTABLE
            )
            usbManager.requestPermission(device, permissionIntent)
            Log.d("DinoKit", "Requesting USB permission for device: ${device.deviceName}")
        }
    }

    /**
     * Establishes connection to the DinoKit and configures the serial port.
     * Should be called when the app needs to communicate with the kit.
     */
    fun connectAndConfigureKit(context: Context): Boolean {
        // Check if already connected
        if (DinoSerialHelper.isConnected()) {
            Log.d("DinoKit", "DinoKit already connected")
            return true
        }
        
        val usbManager = context.getSystemService(Context.USB_SERVICE) as? UsbManager ?: return false
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        
        if (availableDrivers.isEmpty()) {
            Log.w("DinoKit", "No USB serial devices found")
            return false
        }
        
        try {
            val driver = availableDrivers[0]
            val device = driver.device
            
            // Check if we have permission
            if (!usbManager.hasPermission(device)) {
                Log.w("DinoKit", "No USB permission, requesting permission...")
                requestUsbPermission(context)
                return false
            }
            
            val connection = usbManager.openDevice(device)
            
            if (connection == null) {
                Log.w("DinoKit", "Could not open USB device - permission needed")
                return false
            }
            
            val port = driver.ports[0]
            port.open(connection)
            DinoSerialHelper.configureSerialPort(port)

            Log.i("DinoKit", "Successfully connected and configured DinoKit")
            return DinoSerialHelper.isConnected()
            
        } catch (e: Exception) {
            Log.e("DinoKit", "Error connecting to DinoKit", e)
            return false
        }
    }

    fun addConnectionStatusListener(listener: (Boolean) -> Unit) {
        listeners.add(listener)
    }

    fun removeConnectionStatusListener(listener: (Boolean) -> Unit) {
        listeners.remove(listener)
    }

    fun notifyConnectionStatus(context: Context) {
        val isKitConnected = isKitConnected(context)
        
        // If kit is detected but not connected to DinoSerialHelper, establish connection
        if (isKitConnected && !DinoSerialHelper.isConnected()) {
            if (hasUsbPermission(context)) {
                Log.d("DinoKit", "Kit detected and permission granted, attempting to connect...")
                connectAndConfigureKit(context)
            } else {
                Log.d("DinoKit", "Kit detected but no USB permission")
                // Don't spam permission requests - only request once per session
                // The user will need to manually grant permission via system dialog
            }
        }
        
        // Notify listeners with the actual connection status
        val actualConnectionStatus = isKitConnected && DinoSerialHelper.isConnected()
        listeners.forEach { it(actualConnectionStatus) }
    }
}
