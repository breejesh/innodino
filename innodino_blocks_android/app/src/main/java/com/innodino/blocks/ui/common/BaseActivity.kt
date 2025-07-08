package com.innodino.blocks.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.innodino.blocks.R
import com.innodino.blocks.util.DinoKitConnectionChecker

/**
 * BaseActivity: All activities should inherit from this to get the status bar on every screen.
 */
open class BaseActivity : AppCompatActivity() {
    
    private val handler = android.os.Handler()
    private var hasShownUsbPermissionDialog = false

    // Added periodic connection status checks for real-time updates.
    private val connectionStatusRunnable = object : Runnable {
        override fun run() {
            DinoKitConnectionChecker.notifyConnectionStatus(this@BaseActivity)
            handler.postDelayed(this, 1000) // Check every second
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DinoKitConnectionChecker.addConnectionStatusListener { isConnected ->
            updateStatusBar(isConnected)
            
            // Check if we need to show USB permission dialog
            if (!isConnected && !hasShownUsbPermissionDialog && 
                DinoKitConnectionChecker.isKitConnected(this@BaseActivity) && 
                !DinoKitConnectionChecker.hasUsbPermission(this@BaseActivity)) {
                showUsbPermissionDialog()
                hasShownUsbPermissionDialog = true
            }
        }
    }

    /**
     * Shows a dialog to request USB permission for DinoKit
     */
    private fun showUsbPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("USB Permission Required")
            .setMessage("InnoDino Blocks needs permission to access your DinoKit device. Please grant USB permission when prompted.")
            .setPositiveButton("Grant Permission") { _, _ ->
                DinoKitConnectionChecker.requestUsbPermission(this)
            }
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .show()
    }
    
    override fun setContentView(layoutResID: Int) {
        val base = LayoutInflater.from(this).inflate(R.layout.activity_base, null) as ViewGroup
        val content = LayoutInflater.from(this).inflate(layoutResID, base.findViewById(R.id.baseContentContainer), false)
        base.findViewById<ViewGroup>(R.id.baseContentContainer).addView(content)
        super.setContentView(base)
        
        // Update status bar
        updateStatusBar(DinoKitConnectionChecker.isKitConnected(this))
    }

    override fun setContentView(view: View?) {
        val base = LayoutInflater.from(this).inflate(R.layout.activity_base, null) as ViewGroup
        base.findViewById<ViewGroup>(R.id.baseContentContainer).addView(view)
        super.setContentView(base)
        
        // Update status bar
        updateStatusBar(DinoKitConnectionChecker.isKitConnected(this))
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        val base = LayoutInflater.from(this).inflate(R.layout.activity_base, null) as ViewGroup
        base.findViewById<ViewGroup>(R.id.baseContentContainer).addView(view, params)
        super.setContentView(base)
        
        // Update status bar
        updateStatusBar(DinoKitConnectionChecker.isKitConnected(this))
    }
    
    private fun updateStatusBar(isConnected: Boolean) {
        val statusValue = findViewById<TextView>(R.id.statusValue)
        statusValue?.text = if (isConnected) "Connected" else "Disconnected"
        statusValue?.setTextColor(if (isConnected) 0xFF6FCF97.toInt() else 0xFFEB5757.toInt())
    }

    override fun onStart() {
        super.onStart()
        handler.post(connectionStatusRunnable)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(connectionStatusRunnable)
    }
}
