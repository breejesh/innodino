package com.innodino.blocks.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.innodino.blocks.model.MissionData
import com.innodino.blocks.util.MissionLoader

/**
 * ViewModel for mission code builder screen.
 * Handles mission data, progress, and block filtering.
 */
class MissionCodeBuilderViewModel(app: Application) : AndroidViewModel(app) {
    private val prefs = app.getSharedPreferences("mission_progress", Context.MODE_PRIVATE)
    private val _missions = MutableLiveData<List<MissionData>>()
    val missions: LiveData<List<MissionData>> = _missions
    private val _currentMission = MutableLiveData<MissionData?>()
    val currentMission: LiveData<MissionData?> = _currentMission
    private val _completedMissions = MutableLiveData<Set<String>>()
    val completedMissions: LiveData<Set<String>> = _completedMissions

    fun loadMissions(context: Context, assetFile: String = "missions_led.json") {
        val loaded = MissionLoader.loadMissions(context, assetFile)
        _missions.value = loaded
        val completed = prefs.getStringSet("completed", emptySet()) ?: emptySet()
        _completedMissions.value = completed
        // Set first incomplete mission as current
        _currentMission.value = loaded.firstOrNull { it.id !in completed }
    }

    fun markMissionDone(missionId: String) {
        val updated = (_completedMissions.value ?: emptySet()).toMutableSet().apply { add(missionId) }
        _completedMissions.value = updated
        prefs.edit().putStringSet("completed", updated).apply()
        // Move to next incomplete mission
        val next = _missions.value?.firstOrNull { it.id !in updated }
        _currentMission.value = next
    }

    fun setCurrentMission(mission: MissionData?) {
        _currentMission.value = mission
    }
}
