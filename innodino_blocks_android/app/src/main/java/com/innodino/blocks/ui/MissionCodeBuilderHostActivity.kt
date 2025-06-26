package com.innodino.blocks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.innodino.blocks.R

/**
 * Hosts the MissionCodeBuilderFragment for a specific mission.
 * Pass mission ID via Intent extra "MISSION_ID".
 */
class MissionCodeBuilderHostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission_code_builder_host)

        if (savedInstanceState == null) {
            val missionId = intent.getStringExtra("MISSION_ID")
            val module = intent.getStringExtra("MISSION_MODULE") ?: "led"
            val fragment = MissionCodeBuilderFragment().apply {
                arguments = Bundle().apply {
                    putString("MISSION_ID", missionId)
                    putString("MISSION_MODULE", module)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.missionCodeBuilderHostContainer, fragment)
                .commit()
        }
    }
}
