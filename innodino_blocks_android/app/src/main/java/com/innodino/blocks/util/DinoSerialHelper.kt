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

    fun getSensorValue(command: String): Float {
        if (!isConnected || serialPort == null) {
            Log.w("DinoSerial", "Serial port not connected, cannot send command: $command")
            return -1.0f
        }
        
        try {
            Log.d("DinoSerial", "Sending sensor command to Arduino: $command")
            serialPort?.write(command.toByteArray(), 5000)
            
            // Read response with timeout
            var response = ByteArray(2000)
            val bytesRead = serialPort?.read(response, 10000) ?: 0
            
            if (bytesRead > 0) {
                val responseString = String(response, 0, bytesRead).trim()
                Log.d("DinoSerial", "Received sensor response: $responseString")
                
                // Simple parsing: get value after 3rd |
                val parts = responseString.split("|")
                if (parts.size >= 3) {
                    val valuesPart = parts[2].removeSuffix(";")
                    return valuesPart.toFloatOrNull() ?: -1.0f
                }
                
                return -1.0f
            }
            return -1.0f
        } catch (e: Exception) {
            Log.e("DinoSerial", "Error getting sensor value for command: $command", e)
            isConnected = false // Mark as disconnected on error
        }
        return -1.0f
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

    /**
     * Gets accelerometer values as a triple (X, Y, Z)
     * @param command The sensor command (e.g., "@SENSOR|READ_ACCELEROMETER|;")
     * @return Triple of X, Y, Z values or Triple(-1f, -1f, -1f) on error
     */
    fun getAccelerometerValues(command: String): Triple<Float, Float, Float> {
        val response = sendCommand(command)
        
        if (response.startsWith("@SENSOR_DATA|ACCELEROMETER|")) {
            val parts = response.split("|")
            if (parts.size >= 3) {
                val valuesPart = parts[2].removeSuffix(";")
                val values = valuesPart.split(",")
                if (values.size >= 3) {
                    val x = values[0].toFloatOrNull() ?: -1.0f
                    val y = values[1].toFloatOrNull() ?: -1.0f
                    val z = values[2].toFloatOrNull() ?: -1.0f
                    return Triple(x, y, z)
                }
            }
        }
        
        return Triple(-1.0f, -1.0f, -1.0f)
    }

    /**
     * Gets all sensor values as a data class
     * @param command The sensor command (e.g., "@SENSOR|READ_ALL|;")
     * @return SensorData object with all values or null on error
     */
    fun getAllSensorValues(command: String): SensorData? {
        val response = sendCommand(command)
        
        if (response.startsWith("@SENSOR_DATA|ALL|")) {
            val parts = response.split("|")
            if (parts.size >= 3) {
                val valuesPart = parts[2].removeSuffix(";")
                val values = valuesPart.split(",")
                if (values.size >= 6) {
                    return SensorData(
                        distance = values[0].toFloatOrNull() ?: -1.0f,
                        temperature = values[1].toFloatOrNull() ?: -1.0f,
                        lightLevel = values[2].toIntOrNull() ?: -1,
                        accelX = values[3].toFloatOrNull() ?: -1.0f,
                        accelY = values[4].toFloatOrNull() ?: -1.0f,
                        accelZ = values[5].toFloatOrNull() ?: -1.0f
                    )
                }
            }
        }
        
        return null
    }

    /**
     * Data class to hold all sensor readings
     */
    data class SensorData(
        val distance: Float,        // cm
        val temperature: Float,     // °C
        val lightLevel: Int,        // 0-1023
        val accelX: Float,          // m/s²
        val accelY: Float,          // m/s²
        val accelZ: Float           // m/s²
    )
}
