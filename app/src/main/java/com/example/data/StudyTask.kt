package com.example.data

data class StudyTask(
    val id: String,
    val subject: String,
    val title: String,
    val estimatedMinutes: Int = 25,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class StudySession(
    val id: String,
    val timestamp: Long,
    val subject: String,
    val durationMinutes: Int
)

data class HagagStats(
    val totalFocusMinutes: Int = 0,
    val completedTasksCount: Int = 0,
    val streakDays: Int = 0,
    val sessionsToday: Int = 0,
    val sessionsThisWeek: Int = 0,
    val mostStudiedSubject: String = "لسه مابدأتش 😴",
    val currentLevelTitle: String = "فاشل تحت التدريب 😴",
    val progressToGenius: Float = 0.05f,
    val streakBrokenAlert: String? = null
)
