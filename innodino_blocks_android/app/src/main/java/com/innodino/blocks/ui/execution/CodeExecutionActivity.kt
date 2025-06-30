package com.innodino.blocks.ui.execution

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.innodino.blocks.R
import java.util.LinkedList
import java.util.Queue

/**
 * Activity to show code execution animation and allow stopping execution.
 * Shows a UI flow (phone -> command -> Arduino) and the current command.
 */
class CodeExecutionActivity : AppCompatActivity() {

    private lateinit var commandQueue: Queue<String>
    private val handler = Handler(Looper.getMainLooper())
    private var isExecuting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_execution)

        val stopButton = findViewById<Button>(R.id.stopButton)
        val statusText = findViewById<TextView>(R.id.executionStatus)
        val commandText = findViewById<TextView>(R.id.currentCommandText)
        val arrowIcon = findViewById<ImageView>(R.id.arrowIcon)

        // Get code from intent and prepare for execution
        val fullCode = intent.getStringExtra("GENERATED_CODE") ?: ""
        commandQueue = LinkedList(fullCode.split("\n").filter { it.isNotBlank() })

        // Animate the arrow to show data flow
        ObjectAnimator.ofFloat(arrowIcon, "translationX", -20f, 20f).apply {
            duration = 500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = LinearInterpolator()
            start()
        }

        stopButton.setOnClickListener {
            isExecuting = false
            statusText.text = "Execution stopped."
            handler.removeCallbacksAndMessages(null) // Stop scheduled commands
            finish()
        }

        startExecution(statusText, commandText)
    }

    private fun startExecution(statusText: TextView, commandText: TextView) {
        isExecuting = true
        statusText.text = "Executing..."
        executeNextCommand(statusText, commandText)
    }

    private fun executeNextCommand(statusText: TextView, commandText: TextView) {
        if (!isExecuting || commandQueue.isEmpty()) {
            statusText.text = "Execution Finished!"
            commandText.text = "All commands complete."
            isExecuting = false
            // Close after a delay
            handler.postDelayed({ finish() }, 2000)
            return
        }

        val command = commandQueue.poll()
        commandText.text = command
        // TODO: Send the 'command' string to Arduino via USB serial here.

        // Simulate execution time and move to the next command
        handler.postDelayed({
            executeNextCommand(statusText, commandText)
        }, 1500) // 1.5 second delay per command
    }

    override fun onDestroy() {
        super.onDestroy()
        isExecuting = false
        handler.removeCallbacksAndMessages(null)
    }
}
