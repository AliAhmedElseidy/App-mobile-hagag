package com.example.data

data class StudyReward(
    val id: String,
    val title: String,
    val description: String,
    val requiredSessions: Int,
    val category: String,
    val emoji: String,
    val isClaimed: Boolean = false,
    val claimedAt: Long? = null
)
