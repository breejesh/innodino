package com.innodino.blocks.ui.codebuilder

import android.util.Log
import android.webkit.JavascriptInterface
import kotlin.random.Random
import com.innodino.blocks.util.DinoSerialHelper

class BlocklyJsInputBridge {
    @JavascriptInterface
    fun getSensorValue(command: String): Float {
        val value = DinoSerialHelper.getSensorValue(command);
        Log.d("BlocklyJsInputBridge", "getSensorValue($command) = $value")
        return value
    }
}
