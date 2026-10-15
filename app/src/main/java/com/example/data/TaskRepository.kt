package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class TaskRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("hagag_study_prefs", Context.MODE_PRIVATE)

    private val _tasks = MutableStateFlow<List<StudyTask>>(emptyList())
    val tasks: StateFlow<List<StudyTask>> = _tasks.asStateFlow()

    private val _focusMinutes = MutableStateFlow(0)
    val focusMinutes: StateFlow<Int> = _focusMinutes.asStateFlow()

    private val _userName = MutableStateFlow("حجاج")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _autoGreeting = MutableStateFlow(true)
    val autoGreeting: StateFlow<Boolean> = _autoGreeting.asStateFlow()

    private val _speechRate = MutableStateFlow(0.95f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _speechPitch = MutableStateFlow(1.05f)
    val speechPitch: StateFlow<Float> = _speechPitch.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _userName.value = prefs.getString("user_name", "حجاج") ?: "حجاج"
        _autoGreeting.value = prefs.getBoolean("auto_greeting", true)
        _focusMinutes.value = prefs.getInt("focus_minutes", 15)
        _speechRate.value = prefs.getFloat("speech_rate", 0.95f)
        _speechPitch.value = prefs.getFloat("speech_pitch", 1.05f)

        val tasksJson = prefs.getString("study_tasks", null)
        if (tasksJson.isNullOrEmpty()) {
            // Seed initial realistic & humorous tasks for Hagag
            val defaultTasks = listOf(
                StudyTask(
                    id = "task_1",
                    subject = "فيزياء",
                    title = "حل 15 مسألة على قانون أوم وكيرشوف",
                    estimatedMinutes = 40,
                    isCompleted = false
                ),
                StudyTask(
                    id = "task_2",
                    subject = "رياضيات",
                    title = "مراجعة قواعد الاشتقاق والتفاضل",
                    estimatedMinutes = 30,
                    isCompleted = false
                ),
                StudyTask(
                    id = "task_3",
                    subject = "إنجليزي",
                    title = "حفظ 20 كلمة من الوحدة الثالثة",
                    estimatedMinutes = 20,
                    isCompleted = true
                ),
                StudyTask(
                    id = "task_4",
                    subject = "كيمياء",
                    title = "فهم معادلات الباب الثاني بدل الحفظ الأعمى",
                    estimatedMinutes = 35,
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
            } catch (e: Exception) {
                // Ignore parse errors and use empty
            }
            _tasks.value = list
        }
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    fun addFocusMinutes(minutes: Int) {
        val newTotal = _focusMinutes.value + minutes
        _focusMinutes.value = newTotal
        prefs.edit().putInt("focus_minutes", newTotal).apply()
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
}
