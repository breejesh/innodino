package com.innodino.blocks.ui.codebuilder

import android.util.Log
import android.webkit.JavascriptInterface
import kotlin.random.Random

class BlocklyJsInputBridge {
    @JavascriptInterface
    fun getSensorValue(type: String): String {
        val value = when (type) {
            "DISTANCE" -> Random.nextInt(0, 100).toString()
            "LIGHT" -> Random.nextInt(0, 1024).toString()
            "TEMPERATURE" -> (20 + Random.nextDouble() * 10).toString()
            else -> "0"
        }
        Log.d("BlocklyJsInputBridge", "getSensorValue($type) = $value")
        return value
    }
}
