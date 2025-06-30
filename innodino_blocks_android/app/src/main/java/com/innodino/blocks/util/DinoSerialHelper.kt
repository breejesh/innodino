package com.innodino.blocks.util

import android.content.Context
import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialPort

/**
 * DinoSerialHelper
 * Handles sending protocol commands to Arduino via USB serial.
 * NOTE: You must initialize 'serialPort' with a valid UsbSerialPort before sending commands.
 */
object DinoSerialHelper {
    var serialPort: UsbSerialPort? = null

    /**
     * Sends a protocol command string to Arduino over serial.
     * @param command The protocol command (e.g., "@LED|TURN_ON|2,3;")
     */
    fun sendCommand(command: String) {
        Log.d("DinoSerial", "Sending to Arduino: $command") // Log the command for debugging
        serialPort?.write(command.toByteArray(), 1000)
    }

    /**
     * Initializes the serial port with the correct baud rate and parameters.
     * Call this after opening the port and before sending commands.
     */
    fun configureSerialPort(port: UsbSerialPort) {
        serialPort = port
        serialPort?.setParameters(
            9600, // baudRate
            8,    // dataBits
            UsbSerialPort.STOPBITS_1,
            UsbSerialPort.PARITY_NONE
        )
        Log.d("DinoSerial", "Serial port configured to 9600 baud")
    }

    /**
     * Builds a protocol command string for the LED module.
     * @param action The LED action: TURN_ON, TURN_OFF, SET_BRIGHTNESS
     * @param row The row index (0-7)
     * @param col The column index (0-7)
     * @param brightness Optional brightness (0-255) for SET_BRIGHTNESS
     * @return The protocol command string (e.g., "@LED|TURN_ON|2,3;")
     */
    fun buildLedCommand(action: String, row: Int, col: Int, brightness: Int? = null): String {
        return when (action) {
            "TURN_ON" -> "@LED|TURN_ON|$row,$col;"
            "TURN_OFF" -> "@LED|TURN_OFF|$row,$col;"
            "SET_BRIGHTNESS" -> "@LED|SET_BRIGHTNESS|$row,$col,${brightness ?: 255};"
            else -> throw IllegalArgumentException("Unknown LED action: $action")
        }
    }

    /**
     * Sends an LED command to Arduino using the protocol.
     * @param action The LED action: TURN_ON, TURN_OFF, SET_BRIGHTNESS
     * @param row The row index (0-7)
     * @param col The column index (0-7)
     * @param brightness Optional brightness (0-255) for SET_BRIGHTNESS
     */
    fun sendLedCommand(action: String, row: Int, col: Int, brightness: Int? = null) {
        val command = buildLedCommand(action, row, col, brightness)
        sendCommand(command)
    }
}
