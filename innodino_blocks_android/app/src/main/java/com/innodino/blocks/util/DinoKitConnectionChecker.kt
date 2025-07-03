package com.innodino.blocks.util

import android.content.Context
import android.hardware.usb.UsbManager
import com.hoho.android.usbserial.driver.UsbSerialProber

/**
 * Utility to check if InnoDino Kit (USB serial) is connected.
 */
object DinoKitConnectionChecker {
    // Added listener mechanism for connection status updates.
    private val listeners = mutableListOf<(Boolean) -> Unit>()

    fun isKitConnected(context: Context): Boolean {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as? UsbManager
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        return availableDrivers.isNotEmpty()
    }

    fun addConnectionStatusListener(listener: (Boolean) -> Unit) {
        listeners.add(listener)
    }

    fun removeConnectionStatusListener(listener: (Boolean) -> Unit) {
        listeners.remove(listener)
    }

    fun notifyConnectionStatus(context: Context) {
        val isConnected = isKitConnected(context)
        listeners.forEach { it(isConnected) }
    }
}
