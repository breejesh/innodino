package com.innodino.blocks.ui.dinobot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
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
import com.innodino.blocks.ui.theme.InodinoBlocksTheme
import com.innodino.blocks.util.MissionLoader
import dagger.hilt.android.AndroidEntryPoint

/**
 * 🤖 DinoBot Expedition Activity
 * Zara's journey through the Lost Dino City
 */
@AndroidEntryPoint
class DinobotExpeditionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val missions = remember { MissionLoader.loadMissions(this, "missions_robot.json") }
            val completed = remember { getCompletedMissions(this) }
            DinobotExpeditionScreen(
                missions = missions,
                completedMissions = completed,
                onBackClick = { finish() },
                onMissionClick = { missionId ->
                    val intent = Intent(this, MissionCodeBuilderHostActivity::class.java)
                    intent.putExtra("MISSION_ID", missionId)
                    intent.putExtra("MISSION_MODULE", "robot")
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
fun DinobotExpeditionScreen(
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
                // Change background gradient to Tech Teal
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2D9CDB), // Tech Teal
                            Color(0xFF2D9CDB)
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
                    text = "🤖 DinoBot Expedition",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Zara's Journey to Save the Lost Dino City",
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
                val isFirst = missions.indexOf(mission) == 0
                val prevCompleted = missions.getOrNull(missions.indexOf(mission) - 1)?.id in completedMissions
                val isUnlocked = isFirst || prevCompleted || mission.id in completedMissions
                val activity = LocalContext.current as? android.app.Activity
                RobotMissionCard(
                    mission = mission,
                    isUnlocked = isUnlocked,
                    onClick = { if (isUnlocked && activity != null) {
                        val intent = Intent(activity, MissionCodeBuilderHostActivity::class.java)
                        intent.putExtra("MISSION_ID", mission.id)
                        intent.putExtra("MISSION_MODULE", "robot")
                        activity.startActivity(intent)
                    }}
                )
            }
        }
    }
}

@Composable
fun RobotMissionCard(
    mission: MissionData,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isUnlocked) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!isUnlocked) Color(0xFFF5F5F5) else Color.White
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
                            colors = if (!isUnlocked) {
                                listOf(Color.Gray, Color.LightGray)
                            } else {
                                listOf(Color(0xFF2D9CDB), Color(0xFF2D9CDB)) // Tech Teal
                            }
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (!isUnlocked) "🔒" else mission.icon,
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Mission Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mission.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (!isUnlocked) Color.Gray else Color(0xFF4F4F4F)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = mission.description,
                    fontSize = 14.sp,
                    color = if (!isUnlocked) Color.Gray else Color(0xFF4F4F4F).copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Change chips and accent colors to Tech Teal
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    mission.allowedBlocks.take(2).forEach { block ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (!isUnlocked) Color.LightGray else Color(0xFF2D9CDB).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = block,
                                fontSize = 10.sp,
                                color = if (!isUnlocked) Color.Gray else Color(0xFF2D9CDB),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    if (mission.allowedBlocks.size > 2) {
                        Text(
                            text = "+${mission.allowedBlocks.size - 2}",
                            fontSize = 10.sp,
                            color = if (!isUnlocked) Color.Gray else Color(0xFF2D9CDB).copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DinobotExpeditionScreenPreview() {
    InodinoBlocksTheme {
        DinobotExpeditionScreen(
            missions = listOf(
                MissionData(
                    id = "1",
                    title = "Mission 1: First Steps in the Ruined City",
                    description = "Master basic Movement Programming to navigate the rubble",
                    allowedBlocks = listOf("Move Robot", "Stop Robot", "Duration"),
                    icon = "🦶"
                ),
                MissionData(
                    id = "2",
                    title = "Mission 2: The Engineer's Workshop",
                    description = "Learn Variable Programming from Chief Engineer Brontosaurus",
                    allowedBlocks = listOf("Variables", "Memory Storage", "Movement Control"),
                    icon = "🔧"
                ),
                MissionData(
                    id = "3",
                    title = "Mission 3: The Sensor Sage's Trial",
                    description = "Master Conditional Programming with the wise Ankylosaurus",
                    allowedBlocks = listOf("Read Distance", "If/Else", "Smart Decisions"),
                    icon = "📡"
                )
            ),
            completedMissions = setOf("1"),
            onBackClick = {},
            onMissionClick = {}
        )
    }
}
