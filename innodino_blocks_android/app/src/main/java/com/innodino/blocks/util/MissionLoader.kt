package com.innodino.blocks.util

import android.content.Context
import com.google.gson.Gson
import com.innodino.blocks.model.MissionData

/**
 * Loads mission data from a specified asset file (e.g., missions_led.json or missions_robot.json)
 */
object MissionLoader {
    fun loadMissions(context: Context, assetFile: String = "missions.json"): List<MissionData> {
        val json = context.assets.open(assetFile).bufferedReader().use { it.readText() }
        val missionsObj = Gson().fromJson(json, MissionsWrapper::class.java)
        return missionsObj.missions
    }

    private data class MissionsWrapper(
        val missions: List<MissionData>
    )
}
