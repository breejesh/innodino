package com.innodino.blocks.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.innodino.blocks.R

class ModuleThemes {
    @Composable
    fun LedModuleTheme(): MissionTheme {
        return MissionTheme(
            primaryColor = colorResource(R.color.soft_coral),
            secondaryColor = colorResource(R.color.soft_coral_light),
            lightBackgroundColor = Color.White,
            title = "💎 LED Crystal Chronicles",
            subtitle = "Rex's Quest to Restore Digital Dinosaur Valley",
            completionEmoji = "🦖"
        )
    }

    @Composable
    fun DinobotModuleTheme(): MissionTheme {
        return MissionTheme(
            primaryColor = colorResource(R.color.tech_teal),
            secondaryColor = colorResource(R.color.tech_teal_light),
            lightBackgroundColor = Color.White,
            title = "🤖 DinoBot Expedition",
            subtitle = "Zara's Journey to Save the Lost Dino City",
            completionEmoji = "🤖"
        )
    }
}