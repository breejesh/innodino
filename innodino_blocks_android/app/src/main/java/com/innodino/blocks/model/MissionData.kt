// MissionData.kt
package com.innodino.blocks.model

/**
 * Data class representing a mission for the code builder screen.
 * @param id Unique mission identifier
 * @param title Mission title
 * @param description Mission description/story
 * @param allowedBlocks List of block types allowed in this mission
 * @param nextMissionId ID of the next mission (null if last)
 */
data class MissionData(
    val id: String,
    val title: String,
    val description: String,
    val allowedBlocks: List<String>,
    val icon: String, // Emoji or icon for the mission
    val nextMissionId: String? = null
)
