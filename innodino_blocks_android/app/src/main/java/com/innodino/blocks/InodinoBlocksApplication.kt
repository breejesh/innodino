package com.innodino.blocks

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 🦖 InnoDino Blocks Application
 * Main application class for the educational block programming app
 */
@HiltAndroidApp
class InodinoBlocksApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide configurations here
    }
}
