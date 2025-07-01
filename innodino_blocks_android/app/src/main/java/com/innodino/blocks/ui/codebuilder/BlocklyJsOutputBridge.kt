package com.innodino.blocks.ui.codebuilder

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import com.innodino.blocks.ui.execution.CodeExecutionActivity

// Add JS interface for receiving code from WebView
class BlocklyJsOutputBridge {
    @JavascriptInterface
    fun sendCommandToArduino(code: String) {
        // Launch the execution UI with the generated code
        Log.d("BlocklyJsOutputBridge", "Received code: $code")
    }
}