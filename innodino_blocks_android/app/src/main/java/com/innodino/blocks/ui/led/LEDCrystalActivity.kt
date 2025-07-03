package com.innodino.blocks.ui.led

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.innodino.blocks.model.MissionData
import com.innodino.blocks.ui.codebuilder.MissionCodeBuilderHostActivity
import com.innodino.blocks.ui.common.BaseActivity
import com.innodino.blocks.ui.theme.InodinoBlocksTheme
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
            LEDCrystalScreen(
                missions = missions,
                completedMissions = completed,
                onBackClick = { finish() },
                onMissionClick = { missionId ->
                    val intent = Intent(this, MissionCodeBuilderHostActivity::class.java)
                    intent.putExtra("MISSION_ID", missionId)
                    startActivity(intent)
                }
            )
        }
    }

    private fun getCompletedMissions(context: Context): Set<String> {
        val prefs = context.getSharedPreferences("mission_progress", Context.MODE_PRIVATE)
        return prefs.getStringSet("completed", emptySet()) ?: emptySet()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LEDCrystalScreen(
    missions: List<MissionData>,
    completedMissions: Set<String>,
    onBackClick: () -> Unit,
    onMissionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEB5757), // Soft Coral
                            Color(0xFFFF8A65)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💎 LED Crystal Chronicles",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Rex's Quest to Restore Digital Dinosaur Valley",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Mission List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA)),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(missions) { mission ->
                val activity = LocalContext.current as? android.app.Activity
                MissionCard(
                    mission = mission,
                    isCompleted = mission.id in completedMissions,
                    onClick = {
                        if (activity != null) {
                            val intent = Intent(activity, MissionCodeBuilderHostActivity::class.java)
                            intent.putExtra("MISSION_ID", mission.id)
                            intent.putExtra("MISSION_MODULE", "led")
                            activity.startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MissionCard(
    mission: MissionData,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mission Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFEB5757), Color(0xFFFF8A65))
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mission.icon,
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Mission Info
            Column {
                Text(
                    text = mission.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF4F4F4F)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = mission.description,
                    fontSize = 14.sp,
                    color = Color(0xFF7A7A7A)
                )
                
                if (isCompleted) {
                    Text(
                        text = "✓ Completed",
                        color = Color(0xFF6FCF97),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LEDCrystalScreenPreview() {
    InodinoBlocksTheme {
        LEDCrystalScreen(
            missions = listOf(
                MissionData("1", "Mission 1", "Description 1", listOf("block1"), "💎"),
                MissionData("2", "Mission 2", "Description 2", listOf("block2"), "🧠")
            ),
            completedMissions = setOf(),
            onBackClick = {},
            onMissionClick = {}
        )
    }
}
