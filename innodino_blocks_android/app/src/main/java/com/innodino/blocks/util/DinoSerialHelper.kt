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

}
