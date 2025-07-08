package com.innodino.blocks.util

import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialPort

/**
 * DinoSerialHelper - Singleton
 * Handles sending protocol commands to Arduino via USB serial with acknowledgment support.
 * This is a singleton that maintains the serial connection across the entire app.
 */
object DinoSerialHelper {
    private var serialPort: UsbSerialPort? = null
    private var isConnected: Boolean = false

    /**
     * Sends a protocol command string to Arduino over serial.
     * @param command The protocol command (e.g., "@LED|TURN_ON|2,3;")
     */
    fun sendCommand(command: String): String {
        if (!isConnected || serialPort == null) {
            Log.w("DinoSerial", "Serial port not connected, cannot send command: $command")
            Thread.sleep(1000)
            return ""
        }
        
        try {
            Log.d("DinoSerial", "Sending to Arduino: $command")
            serialPort?.write(command.toByteArray(), 5000)
            // Non-blocking read with shorter timeout to prevent UI freezing
            var response = ByteArray(2000)
            val bytesRead = serialPort?.read(response, 10000) ?: 0 // Reduced from 100000 to 1000ms
            if (bytesRead > 0) {
                return String(response, 0, bytesRead).trim()
            }
            return ""
        } catch (e: Exception) {
            Log.e("DinoSerial", "Error sending command: $command", e)
            isConnected = false // Mark as disconnected on error
        }
        return ""
    }

    /**
     * Initializes the serial port with the correct baud rate and parameters.
     * Call this after opening the port and before sending commands.
     */
    fun configureSerialPort(port: UsbSerialPort) {
        try {
            serialPort = port
            serialPort?.setParameters(
                9600, // baudRate
                8,    // dataBits
                UsbSerialPort.STOPBITS_1,
                UsbSerialPort.PARITY_NONE
            )
            isConnected = true
            Log.d("DinoSerial", "Serial port configured to 9600 baud")
        } catch (e: Exception) {
            Log.e("DinoSerial", "Error configuring serial port", e)
            isConnected = false
            serialPort = null
        }
    }

    /**
     * Check if the serial connection is active
     */
    fun isConnected(): Boolean = isConnected && serialPort != null

    /**
     * Disconnect and cleanup the serial port
     */
    fun disconnect() {
        try {
            serialPort?.close()
        } catch (e: Exception) {
            Log.e("DinoSerial", "Error closing serial port", e)
        } finally {
            serialPort = null
            isConnected = false
            Log.d("DinoSerial", "Serial port disconnected")
        }
    }
}
