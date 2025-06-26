package com.innodino.blocks.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.innodino.blocks.R
import com.innodino.blocks.ui.led.LEDCrystalActivity
import com.innodino.blocks.ui.dinobot.DinobotExpeditionActivity
import com.innodino.blocks.ui.theme.InodinoBlocksTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.WindowCompat

/**
 * 🏠 Main Activity - Home Screen
 * The entry point where young adventurers choose their coding quest!
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Force status bar to black for MainActivity
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            InodinoBlocksTheme {
                SideEffect {
                    window.statusBarColor = android.graphics.Color.BLACK
                    window.navigationBarColor = android.graphics.Color.BLACK
                }
                HomeScreen(
                    onLEDAdventureClick = { startLEDAdventure() },
                    onRobotAdventureClick = { startRobotAdventure() }
                )
            }
        }
    }
    
    private fun startLEDAdventure() {
        startActivity(Intent(this, LEDCrystalActivity::class.java))
    }
    
    private fun startRobotAdventure() {
        startActivity(Intent(this, DinobotExpeditionActivity::class.java))
    }
}

@Composable
fun HomeScreen(
    onLEDAdventureClick: () -> Unit,
    onRobotAdventureClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6FCF97)) // Dino Green solid background
    ) {
        // Animated playful background shapes
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0x336FCF97),
                radius = size.minDimension * 0.35f,
                center = center.copy(x = center.x - 200f, y = center.y - 350f)
            )
            drawCircle(
                color = Color(0x332D9CDB),
                radius = size.minDimension * 0.25f,
                center = center.copy(x = center.x + 250f, y = center.y + 400f)
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeroHeader()
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
                    .background(Color.White.copy(alpha = 0.95f)),
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.main_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF4F4F4F),
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 1.2.sp,
                        lineHeight = 38.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.main_subtitle),
                        fontSize = 16.sp,
                        color = Color(0xFF4F4F4F).copy(alpha = 0.7f),
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(36.dp))
                    AdventureCard(
                        title = stringResource(R.string.led_module_title),
                        subtitle = stringResource(R.string.led_module_subtitle),
                        icon = "\uD83D\uDCA1",
                        gradientColors = listOf(
                            Color(0xFFEB5757), // Soft Coral
                            Color(0xFFFFCE55) // Sun Yellow
                        ),
                        adventureText = stringResource(R.string.led_adventures),
                        freePlayText = stringResource(R.string.led_free_play),
                        onAdventureClick = onLEDAdventureClick,
                        onFreePlayClick = { /* TODO: Implement free play */ }
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    AdventureCard(
                        title = stringResource(R.string.robot_module_title),
                        subtitle = stringResource(R.string.robot_module_subtitle),
                        icon = "\uD83E\uDD16",
                        gradientColors = listOf(
                            Color(0xFF2D9CDB), // Tech Teal ONLY
                            Color(0xFF2D9CDB)
                        ),
                        adventureText = stringResource(R.string.robot_adventures),
                        freePlayText = stringResource(R.string.robot_free_play),
                        onAdventureClick = onRobotAdventureClick,
                        onFreePlayClick = { /* TODO: Implement free play */ }
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    // Coming Soon Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp, max = 200.dp)
                            .shadow(2.dp, RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.98f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                listOf(
                                                    Color(0xFFBDBDBD), // Gray
                                                    Color(0xFFEEEEEE)  // Lighter Gray
                                                )
                                            ),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "\uD83E\uDD96", // Sauropod dino
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "More Modules Coming Soon!",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFBDBDBD),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Stay tuned for new adventures!",
                                        fontSize = 12.sp,
                                        color = Color(0xFF4F4F4F).copy(alpha = 0.5f),
                                        maxLines = 1
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "🧩 Unlock more dino fun soon!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFFBDBDBD),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(Color(0xFF6FCF97)), // Dino Green
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            // Parent box is as large as the logo, so logo is never clipped
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.White, shape = CircleShape)
                )
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "InnoDino Logo",
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "InnoDino Blocks",
                fontSize = 20.sp, // Larger title
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Code. Create. Conquer!",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AdventureCard(
    title: String,
    subtitle: String,
    icon: String,
    gradientColors: List<Color>,
    adventureText: String,
    freePlayText: String,
    onAdventureClick: () -> Unit,
    onFreePlayClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp, max = 200.dp)
            .shadow(2.dp, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.98f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            brush = Brush.radialGradient(gradientColors),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = gradientColors[0],
                        maxLines = 1
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFF4F4F4F).copy(alpha = 0.7f),
                        maxLines = 1
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onAdventureClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gradientColors[0],
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(vertical = 2.dp, horizontal = 0.dp)
                ) {
                    Text(
                        text = adventureText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
                OutlinedButton(
                    onClick = onFreePlayClick,
                    modifier = Modifier.weight(1f),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(gradientColors)
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(vertical = 2.dp, horizontal = 0.dp)
                ) {
                    Text(
                        text = freePlayText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = gradientColors[0]
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    InodinoBlocksTheme {
        HomeScreen(
            onLEDAdventureClick = {},
            onRobotAdventureClick = {}
        )
    }
}
