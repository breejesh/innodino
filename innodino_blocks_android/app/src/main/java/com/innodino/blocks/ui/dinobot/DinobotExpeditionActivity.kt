package com.innodino.blocks.ui.dinobot

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
 * 🤖 DinoBot Expedition Activity
 * Zara's journey through the Lost Dino City
 */
@AndroidEntryPoint
class DinobotExpeditionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val missions = remember { MissionLoader.loadMissions(this, "missions/missions_robot.json") }
            val completed = remember { getCompletedMissions(this) }
            
            MissionScreen(
                missions = missions,
                completedMissions = completed,
                theme = ModuleThemes().DinobotModuleTheme(),
                moduleType = "robot",
                allowSequentialUnlock = true, // DinoBot missions unlock sequentially
                onBackClick = { finish() },
                onMissionClick = { missionId ->
                    // Navigation handled in shared component
                }
            )
        }
    }
}
