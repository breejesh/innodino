package com.innodino.blocks.ui.common

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.innodino.blocks.R

/**
 * Persistent bottom status bar for InnoDino Kit USB connection.
 * Uses XML children for layout.
 */
class DinoKitStatusBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val statusLabel: TextView by lazy { findViewById(R.id.statusLabel) }
    private val statusValue: TextView by lazy { findViewById(R.id.statusValue) }

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        // Do not add children in code, use XML children only
        // Ensure always visible unless explicitly hidden
        visibility = View.VISIBLE
    }

    fun showStatus(connected: Boolean) {
        statusValue.text = if (connected) "Connected" else "Disconnected"
        statusValue.setTextColor(
            if (connected) 0xFF6FCF97.toInt() else 0xFFEB5757.toInt()
        )
        visibility = View.VISIBLE
        alpha = 1f
    }

    companion object {
        /**
         * Utility to add the bar to any activity at runtime, overlays all content.
         */
        fun attachToActivity(activity: Activity): DinoKitStatusBar {
            val decor = activity.window.decorView as? android.view.ViewGroup
            // Remove any existing bar to avoid duplicates
            decor?.findViewWithTag<View>("dino_status_bar")?.let { decor.removeView(it) }
            val bar = View.inflate(activity, R.layout.bottom_status_bar, null) as DinoKitStatusBar
            bar.tag = "dino_status_bar"
            val params = android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.BOTTOM
            decor?.addView(bar, params)
            return bar
        }
    }
}
