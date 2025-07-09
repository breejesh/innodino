package com.innodino.blocks.ui.led

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.innodino.blocks.ui.common.BaseActivity
import com.innodino.blocks.ui.shared.MissionScreen
import com.innodino.blocks.ui.shared.ModuleThemes
import com.innodino.blocks.ui.shared.getCompletedMissions
import com.innodino.blocks.util.MissionLoader
import dagger.hilt.android.AndroidEntryPoint

/**
 * 💡 LED Crystal Chronicles Activity
 * Rex's adventure through the mystical Digital Dinosaur Valley
 */
@AndroidEntryPoint
class LEDCrystalActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val missions = remember { MissionLoader.loadMissions(this, "missions/missions_led.json") }
            val completed = remember { getCompletedMissions(this) }
            
            MissionScreen(
                missions = missions,
                completedMissions = completed,
                theme = ModuleThemes().LedModuleTheme(),
                moduleType = "led",
                allowSequentialUnlock = false, // LED missions are all unlocked
                onBackClick = { finish() },
                onMissionClick = { missionId ->
                    // Navigation handled in shared component
                }
            )
        }
    }
}