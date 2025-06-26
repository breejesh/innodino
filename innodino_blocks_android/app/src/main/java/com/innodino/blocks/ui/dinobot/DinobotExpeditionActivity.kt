package com.innodino.blocks.ui.dinobot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.innodino.blocks.R
import com.innodino.blocks.ui.theme.InodinoBlocksTheme
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
            InodinoBlocksTheme {
                DinobotExpeditionScreen(
                    onBackClick = { finish() },
                    onMissionClick = { missionId -> 
                        // TODO: Navigate to specific mission
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DinobotExpeditionScreen(
    onBackClick: () -> Unit,
    onMissionClick: (Int) -> Unit
) {
    val missions = listOf(
        RobotMission(
            id = 1,
            title = "Mission 1: First Steps in the Ruined City",
            description = "Master basic Movement Programming to navigate the rubble",
            icon = "🦶",
            isLocked = false,
            concepts = listOf("Move Robot", "Stop Robot", "Duration")
        ),
        RobotMission(
            id = 2,
            title = "Mission 2: The Engineer's Workshop",
            description = "Learn Variable Programming from Chief Engineer Brontosaurus",
            icon = "🔧",
            isLocked = false,
            concepts = listOf("Variables", "Memory Storage", "Movement Control")
        ),
        RobotMission(
            id = 3,
            title = "Mission 3: The Sensor Sage's Trial",
            description = "Master Conditional Programming with the wise Ankylosaurus",
            icon = "📡",
            isLocked = true,
            concepts = listOf("Read Distance", "If/Else", "Smart Decisions")
        ),
        RobotMission(
            id = 4,
            title = "Mission 4: Dance of the Ancient Guardians",
            description = "Learn Loop Programming to reactivate the Guardian Robots",
            icon = "💃",
            isLocked = true,
            concepts = listOf("Repeat Loop", "Patrol Patterns", "Coordination")
        ),
        RobotMission(
            id = 5,
            title = "FINALE: The Great Rescue Operation",
            description = "Use autonomous navigation to save Lost Dino City!",
            icon = "🚁",
            isLocked = true,
            concepts = listOf("Obstacle Avoidance", "Forever Loop", "Master Quest")
        )
    )
    
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
                            Color(0xFF6FCF97), // Dino Green
                            Color(0xFF81C784)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
                
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
                RobotMissionCard(
                    mission = mission,
                    onClick = { onMissionClick(mission.id) }
                )
            }
        }
    }
}

@Composable
fun RobotMissionCard(
    mission: RobotMission,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (!mission.isLocked) Modifier.clickable { onClick() } else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (mission.isLocked) Color(0xFFF5F5F5) else Color.White
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
                            colors = if (mission.isLocked) {
                                listOf(Color.Gray, Color.LightGray)
                            } else {
                                listOf(Color(0xFF6FCF97), Color(0xFF81C784))
                            }
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (mission.isLocked) "🔒" else mission.icon,
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
                    color = if (mission.isLocked) Color.Gray else Color(0xFF4F4F4F)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = mission.description,
                    fontSize = 14.sp,
                    color = if (mission.isLocked) Color.Gray else Color(0xFF4F4F4F).copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Concept chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    mission.concepts.take(2).forEach { concept ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (mission.isLocked) Color.LightGray else Color(0xFF6FCF97).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = concept,
                                fontSize = 10.sp,
                                color = if (mission.isLocked) Color.Gray else Color(0xFF6FCF97),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    if (mission.concepts.size > 2) {
                        Text(
                            text = "+${mission.concepts.size - 2}",
                            fontSize = 10.sp,
                            color = if (mission.isLocked) Color.Gray else Color(0xFF4F4F4F).copy(alpha = 0.6f)
                        )
                    }
                }
            }
            
            // Status indicator
            if (!mission.isLocked) {
                Text(
                    text = "▶️",
                    fontSize = 16.sp
                )
            }
        }
    }
}

data class RobotMission(
    val id: Int,
    val title: String,
    val description: String,
    val icon: String,
    val isLocked: Boolean,
    val concepts: List<String>
)

@Preview(showBackground = true)
@Composable
fun DinobotExpeditionScreenPreview() {
    InodinoBlocksTheme {
        DinobotExpeditionScreen(
            onBackClick = {},
            onMissionClick = {}
        )
    }
}
