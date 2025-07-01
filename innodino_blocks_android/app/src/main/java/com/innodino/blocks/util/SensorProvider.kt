package com.innodino.blocks.util

interface SensorProvider {
    fun getDistanceSensorValue(): Int
    fun getLightSensorValue(): Int
    fun getTemperatureSensorValue(): Float
}