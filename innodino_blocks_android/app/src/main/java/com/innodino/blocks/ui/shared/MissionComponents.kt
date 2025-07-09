package com.innodino.blocks.ui.shared

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.innodino.blocks.R
import com.innodino.blocks.model.MissionData
import com.innodino.blocks.ui.codebuilder.MissionCodeBuilderHostActivity

/**
 * 🦖 Shared Mission Components for InnoDino Adventures
 * Reusable UI components for both LED Crystal Chronicles and DinoBot Expedition
 */

data class MissionTheme(
    val primaryColor: Color,
    val secondaryColor: Color,
    val lightBackgroundColor: Color,
    val title: String,
    val subtitle: String,
    val completionEmoji: String
)

@Composable
fun MissionScreen(
    missions: List<MissionData>,
    completedMissions: Set<String>,
    theme: MissionTheme,
    moduleType: String,
    allowSequentialUnlock: Boolean = false,
    onBackClick: () -> Unit,
    onMissionClick: (String) -> Unit
) {
    val completedCount = missions.count { it.id in completedMissions }
    val totalCount = missions.size
    val progressPercentage = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                   theme.primaryColor
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = theme.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = theme.subtitle,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Progress Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(8.dp)
                            .background(
                                Color.White.copy(alpha = 0.3f),
                                RoundedCornerShape(4.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressPercentage)
                                .background(
                                    colorResource(R.color.dino_green), // Dino Green
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Text(
                        text = "$completedCount of $totalCount Missions Complete",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
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
                val isCompleted = mission.id in completedMissions
                val activity = LocalContext.current as? android.app.Activity
                
                MissionCard(
                    mission = mission,
                    isCompleted = isCompleted,
                    theme = theme,
                    onClick = { 
                        if(activity != null) {
                            val intent = Intent(activity, MissionCodeBuilderHostActivity::class.java)
                            intent.putExtra("MISSION_ID", mission.id)
                            intent.putExtra("MISSION_MODULE", moduleType)
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
    theme: MissionTheme,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCompleted) 6.dp else 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mission Icon with completion indicator
            Box(
                modifier = Modifier.size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = theme.primaryColor,
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text =  mission.icon,
                        fontSize = if (isCompleted) 28.sp else 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Mission Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = mission.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                
                Text(
                    text = mission.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
                
                if (isCompleted) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    colorResource(R.color.dino_green),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                        
                        Spacer(modifier = Modifier.width(6.dp))
                        
                        Text(
                            text = "Mission Completed!",
                            color =  colorResource(R.color.dino_green),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    colorResource(R.color.block_condition_secondary),
                                    RoundedCornerShape(4.dp)
                                )
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "To Do",
                            color = colorResource(R.color.block_condition_secondary),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Helper function to get completed missions from SharedPreferences
 */
fun getCompletedMissions(context: Context): Set<String> {
    val prefs = context.getSharedPreferences("mission_progress", Context.MODE_PRIVATE)
    return prefs.getStringSet("completed", emptySet()) ?: emptySet()
}
