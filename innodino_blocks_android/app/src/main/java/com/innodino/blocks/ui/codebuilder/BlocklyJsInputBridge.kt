package com.innodino.blocks.ui.codebuilder

import android.webkit.JavascriptInterface

class BlocklyJsInputBridge(
    private val sensorProvider: SensorProvider
) {
    @JavascriptInterface
    fun getSensorValue(type: String): String {
        return when (type) {
            "DISTANCE" -> sensorProvider.getDistanceSensorValue().toString()
            "LIGHT" -> sensorProvider.getLightSensorValue().toString()
            "TEMPERATURE" -> sensorProvider.getTemperatureSensorValue().toString()
            else -> "0"
        }
    }

    interface SensorProvider {
        fun getDistanceSensorValue(): Int
        fun getLightSensorValue(): Int
        fun getTemperatureSensorValue(): Float
    }
}
