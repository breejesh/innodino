package com.innodino.blocks.ui.codebuilder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.innodino.blocks.R
import com.innodino.blocks.ui.common.BaseActivity

/**
 * Hosts the MissionCodeBuilderFragment for a specific mission.
 * Pass mission ID via Intent extra "MISSION_ID".
 */
class MissionCodeBuilderHostActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission_code_builder_host)

        if (savedInstanceState == null) {
            val missionId = intent.getStringExtra("MISSION_ID")
            val module = intent.getStringExtra("MISSION_MODULE") ?: "led"
            val freePlay = intent.getBooleanExtra("FREE_PLAY", false)
            val fragment = MissionCodeBuilderFragment().apply {
                arguments = Bundle().apply {
                    putString("MISSION_ID", missionId)
                    putString("MISSION_MODULE", module)
                    putBoolean("FREE_PLAY", freePlay)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.missionCodeBuilderHostContainer, fragment)
                .commit()
        }
    }
}
