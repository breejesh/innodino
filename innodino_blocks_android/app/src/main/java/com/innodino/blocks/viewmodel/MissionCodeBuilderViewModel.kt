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
 * Handles mission data and progress tracking only. No mission locking.
 */
class MissionCodeBuilderViewModel(app: Application) : AndroidViewModel(app) {
    private val prefs = app.getSharedPreferences("mission_progress", Context.MODE_PRIVATE)
    private val _missions = MutableLiveData<List<MissionData>>()
    val missions: LiveData<List<MissionData>> = _missions
    private val _currentMission = MutableLiveData<MissionData?>()
    val currentMission: LiveData<MissionData?> = _currentMission
    private val _completedMissions = MutableLiveData<Set<String>>()
    val completedMissions: LiveData<Set<String>> = _completedMissions

    fun loadMissions(context: Context, assetFile: String = "missions.json", forceMissionId: String? = null) {
        val loaded = MissionLoader.loadMissions(context, assetFile)
        _missions.value = loaded
        val completed = prefs.getStringSet("completed", emptySet()) ?: emptySet()
        _completedMissions.value = completed
        // Always show the first mission or forced mission
        _currentMission.value = when {
            forceMissionId != null -> loaded.firstOrNull { it.id == forceMissionId }
            else -> loaded.firstOrNull()
        }
    }

    fun markMissionDone(missionId: String) {
        val updated = (_completedMissions.value ?: emptySet()).toMutableSet().apply { add(missionId) }
        _completedMissions.value = updated
        prefs.edit().putStringSet("completed", updated).apply()
    }

    fun setCurrentMission(mission: MissionData?) {
        _currentMission.value = mission
    }

    fun clearCompletedMissions() {
        prefs.edit().remove("completed").apply()
        _completedMissions.value = emptySet()
        _currentMission.value = _missions.value?.firstOrNull()
    }
}
