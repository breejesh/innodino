package com.innodino.blocks.ui.execution

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.innodino.blocks.R
import java.util.LinkedList
import java.util.Queue

/**
 * Activity to show code execution animation and allow stopping execution.
 * Shows a UI flow (phone -> command -> Arduino) and the current command.
 */
class CodeExecutionActivity : AppCompatActivity() {

    private lateinit var commandQueue: Queue<String>
    private val handler = Handler(Looper.getMainLooper())
    private var isExecuting = false

    private val usbPermissionAction = "com.innodino.blocks.USB_PERMISSION"
    private var usbPermissionReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_execution)

        // Register USB permission receiver
        registerUsbPermissionReceiver()
        // Connect to Dino USB Serial on activity start
        connectToDinoUsbSerial(this)

        val stopButton = findViewById<Button>(R.id.stopButton)
        val statusText = findViewById<TextView>(R.id.executionStatus)
        val commandText = findViewById<TextView>(R.id.currentCommandText)
        val arrowIcon = findViewById<ImageView>(R.id.arrowIcon)

        // Get code from intent and prepare for execution
        val fullCode = intent.getStringExtra("GENERATED_CODE") ?: ""
        commandQueue = LinkedList(fullCode.split("\n").filter { it.isNotBlank() })

        // Animate the arrow to show data flow
        ObjectAnimator.ofFloat(arrowIcon, "translationX", -20f, 20f).apply {
            duration = 500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = LinearInterpolator()
            start()
        }

        stopButton.setOnClickListener {
            isExecuting = false
            statusText.text = "Execution stopped."
            handler.removeCallbacksAndMessages(null) // Stop scheduled commands
            finish()
        }

        startExecution(statusText, commandText)
    }

    private fun registerUsbPermissionReceiver() {
        usbPermissionReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == usbPermissionAction) {
                    synchronized(this) {
                        val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            // Permission granted, try to connect again
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
            // No USB serial device found
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
            // Request permission
            val permissionIntent = PendingIntent.getBroadcast(
                activity, 0, Intent(usbPermissionAction), PendingIntent.FLAG_IMMUTABLE
            )
            usbManager.requestPermission(device, permissionIntent)
            return
        }
        val port: UsbSerialPort = driver.ports[0]
        port.open(connection)
        com.innodino.blocks.util.DinoSerialHelper.configureSerialPort(port)
    }

    private fun startExecution(statusText: TextView, commandText: TextView) {
        isExecuting = true
        statusText.text = "Executing..."
        executeNextCommand(statusText, commandText)
    }

    private fun executeNextCommand(statusText: TextView, commandText: TextView) {
        if (!isExecuting || commandQueue.isEmpty()) {
            statusText.text = "Execution Finished!"
            commandText.text = "All commands complete."
            isExecuting = false
            // Close after a delay
            handler.postDelayed({ finish() }, 2000)
            return
        }

        val command = commandQueue.poll()
        commandText.text = command
        // Send the command to Arduino if it's an LED command
        com.innodino.blocks.util.DinoSerialHelper.sendCommand((command))

        // Simulate execution time and move to the next command
        handler.postDelayed({
            executeNextCommand(statusText, commandText)
        }, 1500) // 1.5 second delay per command
    }

    override fun onDestroy() {
        super.onDestroy()
        usbPermissionReceiver?.let { unregisterReceiver(it) }
        isExecuting = false
        handler.removeCallbacksAndMessages(null)
    }
}
