package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.notification.HagagNotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("hagag_study_prefs", Context.MODE_PRIVATE)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private val _tasks = MutableStateFlow<List<StudyTask>>(emptyList())
    val tasks: StateFlow<List<StudyTask>> = _tasks.asStateFlow()

    private val _sessions = MutableStateFlow<List<StudySession>>(emptyList())
    val sessions: StateFlow<List<StudySession>> = _sessions.asStateFlow()

    private val _focusMinutes = MutableStateFlow(0)
    val focusMinutes: StateFlow<Int> = _focusMinutes.asStateFlow()

    private val _streakDays = MutableStateFlow(0)
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _streakBrokenAlert = MutableStateFlow<String?>(null)
    val streakBrokenAlert: StateFlow<String?> = _streakBrokenAlert.asStateFlow()

    private val _userName = MutableStateFlow("حجاج")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _autoGreeting = MutableStateFlow(true)
    val autoGreeting: StateFlow<Boolean> = _autoGreeting.asStateFlow()

    private val _speechRate = MutableStateFlow(0.95f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _speechPitch = MutableStateFlow(1.05f)
    val speechPitch: StateFlow<Float> = _speechPitch.asStateFlow()

    private val _dailyReminderEnabled = MutableStateFlow(true)
    val dailyReminderEnabled: StateFlow<Boolean> = _dailyReminderEnabled.asStateFlow()

    private val _reminderHour = MutableStateFlow(17)
    val reminderHour: StateFlow<Int> = _reminderHour.asStateFlow()

    private val _reminderMinute = MutableStateFlow(0)
    val reminderMinute: StateFlow<Int> = _reminderMinute.asStateFlow()

    init {
        loadData()
        checkStreakIntegrity()
        scheduleOrUpdateAlarm()
    }

    private fun loadData() {
        _userName.value = prefs.getString("user_name", "حجاج") ?: "حجاج"
        _autoGreeting.value = prefs.getBoolean("auto_greeting", true)
        _focusMinutes.value = prefs.getInt("focus_minutes", 0)
        _speechRate.value = prefs.getFloat("speech_rate", 0.95f)
        _speechPitch.value = prefs.getFloat("speech_pitch", 1.05f)
        _streakDays.value = prefs.getInt("streak_days", 0)

        _dailyReminderEnabled.value = prefs.getBoolean("daily_reminder_enabled", true)
        _reminderHour.value = prefs.getInt("reminder_hour", 17)
        _reminderMinute.value = prefs.getInt("reminder_minute", 0)

        // Load tasks
        val tasksJson = prefs.getString("study_tasks", null)
        if (tasksJson.isNullOrEmpty()) {
            val defaultTasks = listOf(
                StudyTask(
                    id = "task_1",
                    subject = "رياضيات",
                    title = "حل 10 مسائل على الجذر التكعيبي ومتوسطات المثلث",
                    estimatedMinutes = 30,
                    isCompleted = false
                ),
                StudyTask(
                    id = "task_2",
                    subject = "علوم",
                    title = "مذاكرة درس تدرج خواص العناصر في الجدول الدوري",
                    estimatedMinutes = 35,
                    isCompleted = false
                ),
                StudyTask(
                    id = "task_3",
                    subject = "لغة عربية",
                    title = "إعراب جمل درس كبرياء طفل ومراجعة المعرب والمبني",
                    estimatedMinutes = 25,
                    isCompleted = true
                ),
                StudyTask(
                    id = "task_4",
                    subject = "إنجليزي",
                    title = "حفظ كلمات الوحدة وحل تدريبات على While و If",
                    estimatedMinutes = 20,
                    isCompleted = false
                )
            )
            _tasks.value = defaultTasks
            saveTasks(defaultTasks)
        } else {
            val list = mutableListOf<StudyTask>()
            try {
                val array = JSONArray(tasksJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        StudyTask(
                            id = obj.getString("id"),
                            subject = obj.optString("subject", "عام"),
                            title = obj.getString("title"),
                            estimatedMinutes = obj.optInt("estimatedMinutes", 25),
                            isCompleted = obj.optBoolean("isCompleted", false),
                            createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                        )
                    )
                }
            } catch (_: Exception) {}
            _tasks.value = list
        }

        // Load sessions
        val sessionsJson = prefs.getString("study_sessions", null)
        if (!sessionsJson.isNullOrEmpty()) {
            val list = mutableListOf<StudySession>()
            try {
                val array = JSONArray(sessionsJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        StudySession(
                            id = obj.getString("id"),
                            timestamp = obj.getLong("timestamp"),
                            subject = obj.optString("subject", "عام"),
                            durationMinutes = obj.getInt("durationMinutes")
                        )
                    )
                }
            } catch (_: Exception) {}
            _sessions.value = list
        } else {
            // Seed a few past realistic sessions so stats aren't completely blank on first turn
            val now = System.currentTimeMillis()
            val dayMillis = 24 * 60 * 60 * 1000L
            val initialSessions = listOf(
                StudySession("s1", now - 2 * dayMillis, "علوم", 25),
                StudySession("s2", now - dayMillis, "رياضيات", 30),
                StudySession("s3", now - 3 * 3600 * 1000L, "لغة عربية", 25)
            )
            _sessions.value = initialSessions
            saveSessions(initialSessions)
            _focusMinutes.value = 80
            _streakDays.value = 2
            prefs.edit().putInt("focus_minutes", 80).putInt("streak_days", 2).apply()
        }
    }

    private fun checkStreakIntegrity() {
        val lastDateStr = prefs.getString("last_study_date", null) ?: return
        val todayStr = dateFormat.format(Date())

        if (lastDateStr == todayStr) {
            // Already studied today
            return
        }

        try {
            val lastDate = dateFormat.parse(lastDateStr) ?: return
            val today = dateFormat.parse(todayStr) ?: return
            val diffDays = ((today.time - lastDate.time) / (1000 * 60 * 60 * 24)).toInt()

            if (diffDays > 1) {
                // Streak broken!
                if (_streakDays.value > 0) {
                    val name = _userName.value
                    val roast = RoastQuotesRepository.getRandomQuote(QuoteCategory.STREAK_BROKEN).text.replace("حجاج", name)
                    _streakBrokenAlert.value = roast
                    _streakDays.value = 0
                    prefs.edit()
                        .putInt("streak_days", 0)
                        .remove("last_study_date")
                        .apply()
                }
            }
        } catch (_: Exception) {}
    }

    fun dismissStreakBrokenAlert() {
        _streakBrokenAlert.value = null
    }

    fun recordCompletedSession(subject: String, durationMinutes: Int) {
        val now = System.currentTimeMillis()
        val newSession = StudySession(
            id = "sess_${System.currentTimeMillis()}",
            timestamp = now,
            subject = subject.ifBlank { "عام" },
            durationMinutes = durationMinutes
        )
        val updatedSessions = _sessions.value + newSession
        _sessions.value = updatedSessions
        saveSessions(updatedSessions)

        // Add focus minutes
        val newTotalMinutes = _focusMinutes.value + durationMinutes
        _focusMinutes.value = newTotalMinutes
        prefs.edit().putInt("focus_minutes", newTotalMinutes).apply()

        // Update streak
        val todayStr = dateFormat.format(Date(now))
        val lastDateStr = prefs.getString("last_study_date", null)

        val newStreak = if (lastDateStr == null) {
            1
        } else if (lastDateStr == todayStr) {
            // Already recorded today, maintain current streak
            if (_streakDays.value == 0) 1 else _streakDays.value
        } else {
            val calendar = Calendar.getInstance().apply {
                time = Date(now)
                add(Calendar.DAY_OF_YEAR, -1)
            }
            val yesterdayStr = dateFormat.format(calendar.time)

            if (lastDateStr == yesterdayStr) {
                _streakDays.value + 1
            } else {
                1
            }
        }

        _streakDays.value = newStreak
        _streakBrokenAlert.value = null
        prefs.edit()
            .putInt("streak_days", newStreak)
            .putString("last_study_date", todayStr)
            .apply()
    }

    private fun saveTasks(list: List<StudyTask>) {
        try {
            val array = JSONArray()
            for (task in list) {
                val obj = JSONObject().apply {
                    put("id", task.id)
                    put("subject", task.subject)
                    put("title", task.title)
                    put("estimatedMinutes", task.estimatedMinutes)
                    put("isCompleted", task.isCompleted)
                    put("createdAt", task.createdAt)
                }
                array.put(obj)
            }
            prefs.edit().putString("study_tasks", array.toString()).apply()
        } catch (_: Exception) {}
    }

    private fun saveSessions(list: List<StudySession>) {
        try {
            val array = JSONArray()
            for (sess in list) {
                val obj = JSONObject().apply {
                    put("id", sess.id)
                    put("timestamp", sess.timestamp)
                    put("subject", sess.subject)
                    put("durationMinutes", sess.durationMinutes)
                }
                array.put(obj)
            }
            prefs.edit().putString("study_sessions", array.toString()).apply()
        } catch (_: Exception) {}
    }

    fun addTask(subject: String, title: String, estimatedMinutes: Int) {
        val newTask = StudyTask(
            id = "task_${System.currentTimeMillis()}",
            subject = subject.trim().ifEmpty { "عام" },
            title = title.trim(),
            estimatedMinutes = estimatedMinutes,
            isCompleted = false
        )
        val updated = listOf(newTask) + _tasks.value
        _tasks.value = updated
        saveTasks(updated)
    }

    fun toggleTask(id: String) {
        val updated = _tasks.value.map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
        }
        _tasks.value = updated
        saveTasks(updated)
    }

    fun deleteTask(id: String) {
        val updated = _tasks.value.filter { it.id != id }
        _tasks.value = updated
        saveTasks(updated)
    }

    fun setUserName(name: String) {
        val cleanName = name.trim().ifEmpty { "حجاج" }
        _userName.value = cleanName
        prefs.edit().putString("user_name", cleanName).apply()
    }

    fun setAutoGreeting(enabled: Boolean) {
        _autoGreeting.value = enabled
        prefs.edit().putBoolean("auto_greeting", enabled).apply()
    }

    fun setSpeechSettings(pitch: Float, rate: Float) {
        _speechPitch.value = pitch
        _speechRate.value = rate
        prefs.edit()
            .putFloat("speech_pitch", pitch)
            .putFloat("speech_rate", rate)
            .apply()
    }

    fun setDailyReminder(enabled: Boolean, hour: Int, minute: Int) {
        _dailyReminderEnabled.value = enabled
        _reminderHour.value = hour
        _reminderMinute.value = minute
        prefs.edit()
            .putBoolean("daily_reminder_enabled", enabled)
            .putInt("reminder_hour", hour)
            .putInt("reminder_minute", minute)
            .apply()

        scheduleOrUpdateAlarm()
    }

    private fun scheduleOrUpdateAlarm() {
        if (_dailyReminderEnabled.value) {
            HagagNotificationHelper.createNotificationChannels(context)
            HagagNotificationHelper.scheduleDailyReminder(
                context,
                _reminderHour.value,
                _reminderMinute.value
            )
        } else {
            HagagNotificationHelper.cancelDailyReminder(context)
        }
    }
}
