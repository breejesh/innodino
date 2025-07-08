package com.innodino.blocks.ui.codebuilder

import android.util.Log
import android.webkit.JavascriptInterface
import com.innodino.blocks.util.DinoSerialHelper

// Add JS interface for receiving code from WebView
class BlocklyJsOutputBridge {
    @JavascriptInterface
    fun sendCommandToArduino(code: String) {
        // Launch the execution UI with the generated code
        Log.d("BlocklyJsOutputBridge", "Received code: $code")
        val response = DinoSerialHelper.sendCommand(code)
        Log.d("BlocklyJsOutputBridge", "Response from Arduino: $response")
    }
}