package com.innodino.blocks.ui.execution

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.innodino.blocks.util.DinoSerialHelper

/**
 * Activity to send a single serial command from JS to Arduino and finish.
 */
class CodeExecutionActivity : AppCompatActivity() {
    private val usbPermissionAction = "com.innodino.blocks.USB_PERMISSION"
    private var usbPermissionReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // No UI, just send command
        registerUsbPermissionReceiver()
        connectToDinoUsbSerial(this)
        val command = intent.getStringExtra("SERIAL_COMMAND") ?: ""
        if (command.isNotBlank()) {
            DinoSerialHelper.sendCommand(command)
        }
        finish()
    }

    private fun registerUsbPermissionReceiver() {
        usbPermissionReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == usbPermissionAction) {
                    synchronized(this) {
                        val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            connectToDinoUsbSerial(this@CodeExecutionActivity, device)
                        }
                    }
                }
            }
        }
        registerReceiver(usbPermissionReceiver, IntentFilter(usbPermissionAction))
    }

    private fun connectToDinoUsbSerial(activity: Activity, specificDevice: UsbDevice? = null) {
        val usbManager = activity.getSystemService(UsbManager::class.java)
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        if (availableDrivers.isEmpty()) {
            return
        }
        val driver = if (specificDevice != null) {
            availableDrivers.find { it.device.deviceId == specificDevice.deviceId } ?: availableDrivers[0]
        } else {
            availableDrivers[0]
        }
        val device = driver.device
        val connection = usbManager.openDevice(device)
        if (connection == null) {
            val permissionIntent = android.app.PendingIntent.getBroadcast(
                activity, 0, Intent(usbPermissionAction), android.app.PendingIntent.FLAG_IMMUTABLE
            )
            usbManager.requestPermission(device, permissionIntent)
            return
        }
        val port: UsbSerialPort = driver.ports[0]
        port.open(connection)
        DinoSerialHelper.configureSerialPort(port)
    }

    override fun onDestroy() {
        super.onDestroy()
        usbPermissionReceiver?.let { unregisterReceiver(it) }
    }
}
