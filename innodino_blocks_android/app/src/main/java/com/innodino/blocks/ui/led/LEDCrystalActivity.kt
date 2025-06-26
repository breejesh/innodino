package com.innodino.blocks.ui.led

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
 * 💡 LED Crystal Chronicles Activity
 * Rex's adventure through the mystical Digital Dinosaur Valley
 */
@AndroidEntryPoint
class LEDCrystalActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InodinoBlocksTheme {
                LEDCrystalScreen(
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
fun LEDCrystalScreen(
    onBackClick: () -> Unit,
    onMissionClick: (Int) -> Unit
) {
    val missions = listOf(
        LEDMission(
            id = 1,
            title = "Mission 1: Awakening the First Crystal",
            description = "Learn the ancient art of Light Magic by mastering basic LED commands",
            icon = "💎",
            isLocked = false,
            concepts = listOf("Set LED", "Row/Col Coordinates")
        ),
        LEDMission(
            id = 2,
            title = "Mission 2: Memory Crystals of Elder Triceratops",
            description = "Master the secrets of Memory Magic using Variables",
            icon = "🧠",
            isLocked = false,
            concepts = listOf("Variables", "Set LED", "Memory Storage")
        ),
        LEDMission(
            id = 3,
            title = "Mission 3: The Guardian's Challenge",
            description = "Learn Sensing Magic with the Ultrasonic Sensor",
            icon = "👁️",
            isLocked = true,
            concepts = listOf("Read Distance", "If/Else", "Comparisons")
        ),
        LEDMission(
            id = 4,
            title = "Mission 4: Rhythm of the Ancient Pterodactyl",
            description = "Master Rhythm Magic through loop patterns",
            icon = "🔄",
            isLocked = true,
            concepts = listOf("Repeat Loop", "Patterns", "Animation")
        ),
        LEDMission(
            id = 5,
            title = "FINALE: The Great Illumination Ceremony",
            description = "Combine all magic to restore Digital Dinosaur Valley!",
            icon = "🌟",
            isLocked = true,
            concepts = listOf("All Skills", "Map Function", "Master Quest")
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
                MissionCard(
                    mission = mission,
                    onClick = { onMissionClick(mission.id) }
                )
            }
        }
    }
}

@Composable
fun MissionCard(
    mission: LEDMission,
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
                                listOf(Color(0xFFEB5757), Color(0xFFFF8A65))
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
                                    color = if (mission.isLocked) Color.LightGray else Color(0xFFEB5757).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = concept,
                                fontSize = 10.sp,
                                color = if (mission.isLocked) Color.Gray else Color(0xFFEB5757),
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

data class LEDMission(
    val id: Int,
    val title: String,
    val description: String,
    val icon: String,
    val isLocked: Boolean,
    val concepts: List<String>
)

@Preview(showBackground = true)
@Composable
fun LEDCrystalScreenPreview() {
    InodinoBlocksTheme {
        LEDCrystalScreen(
            onBackClick = {},
            onMissionClick = {}
        )
    }
}
