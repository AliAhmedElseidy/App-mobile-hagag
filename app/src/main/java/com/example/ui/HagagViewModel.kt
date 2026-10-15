package com.example.ui

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.HagagStats
import com.example.data.RoastQuote
import com.example.data.RoastQuotesRepository
import com.example.data.StudyTask
import com.example.data.TaskRepository
import com.example.tts.HagagTtsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HagagViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(application)
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        application.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private var hasSpokenInitialGreeting = false

    val ttsManager = HagagTtsManager(application) {
        // Trigger initial greeting when TTS is ready if auto greeting is enabled
        if (!hasSpokenInitialGreeting && repository.autoGreeting.value) {
            hasSpokenInitialGreeting = true
            shoutGreeting()
        }
    }

    val isSpeaking: StateFlow<Boolean> = ttsManager.isSpeaking
    val isTtsReady: StateFlow<Boolean> = ttsManager.isReady

    val tasks: StateFlow<List<StudyTask>> = repository.tasks
    val focusMinutes: StateFlow<Int> = repository.focusMinutes
    val userName: StateFlow<String> = repository.userName
    val autoGreeting: StateFlow<Boolean> = repository.autoGreeting
    val speechRate: StateFlow<Float> = repository.speechRate
    val speechPitch: StateFlow<Float> = repository.speechPitch

    // Current active roast / quote on display
    private val _currentQuote = MutableStateFlow(RoastQuotesRepository.quotes[0])
    val currentQuote: StateFlow<RoastQuote> = _currentQuote.asStateFlow()

    // Navigation state
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    // Timer State
    private val _timerRunning = MutableStateFlow(false)
    val timerRunning: StateFlow<Boolean> = _timerRunning.asStateFlow()

    private val _selectedDurationMinutes = MutableStateFlow(25)
    val selectedDurationMinutes: StateFlow<Int> = _selectedDurationMinutes.asStateFlow()

    private val _timeLeftSeconds = MutableStateFlow(25 * 60)
    val timeLeftSeconds: StateFlow<Int> = _timeLeftSeconds.asStateFlow()

    private val _isBreak = MutableStateFlow(false)
    val isBreak: StateFlow<Boolean> = _isBreak.asStateFlow()

    private var timerJob: Job? = null

    // Combined stats for Genius Meter
    val stats: StateFlow<HagagStats> = combine(
        focusMinutes,
        tasks
    ) { minutes, allTasks ->
        val completed = allTasks.count { it.isCompleted }
        val progress = ((minutes * 1.5f + completed * 15f) / 150f).coerceIn(0.05f, 1f)

        val level = when {
            minutes < 20 && completed == 0 -> "فاشل بامتياز مع مرتبة الشرف 😴"
            minutes < 40 -> "عايز يذاكر بس الشاي بيبرد ☕"
            minutes < 80 -> "طالب مجتهد في مرحلة الصحوة 📖"
            else -> "دحيح الدفعة يا حجاج! 🎓🏆"
        }

        HagagStats(
            totalFocusMinutes = minutes,
            completedTasksCount = completed,
            streakDays = if (minutes > 0) 3 else 1,
            currentLevelTitle = level,
            progressToGenius = progress
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HagagStats()
    )

    fun selectTab(tab: Int) {
        _currentTab.value = tab
    }

    fun shoutGreeting() {
        triggerHaptic()
        val name = userName.value
        val greetingText = "ازيك يا $name.. ذاكر يا فاشل!"
        _currentQuote.value = RoastQuote(
            id = "greeting",
            text = "$greetingText 📢",
            spokenText = greetingText,
            category = "كلاسيكيات حجاج",
            severityEmoji = "🔥",
            punchline = "الدفعة كلها بتذاكر وأنت ماسك الموبايل!"
        )
        ttsManager.speak(greetingText, pitch = speechPitch.value, rate = speechRate.value)
    }

    fun roastMeAgain() {
        triggerHaptic()
        val newQuote = RoastQuotesRepository.getRandomQuote()
        _currentQuote.value = newQuote
        val name = userName.value
        val textToSpeak = newQuote.spokenText.replace("حجاج", name)
        ttsManager.speak(textToSpeak, pitch = speechPitch.value, rate = speechRate.value)
    }

    fun speakPhrase(phrase: String) {
        triggerHaptic()
        ttsManager.speak(phrase, pitch = speechPitch.value, rate = speechRate.value)
    }

    fun stopSpeaking() {
        ttsManager.stop()
    }

    // Pomodoro Timer controls
    fun setDuration(minutes: Int, isBreakMode: Boolean = false) {
        if (_timerRunning.value) {
            pauseTimer()
        }
        _isBreak.value = isBreakMode
        _selectedDurationMinutes.value = minutes
        _timeLeftSeconds.value = minutes * 60
    }

    fun startTimer() {
        if (_timerRunning.value) return
        _timerRunning.value = true
        triggerHaptic()

        val name = userName.value
        val startMessage = if (_isBreak.value) {
            "استراحة مستحقة يا $name.. خمس دقايق بس بلاش سرحان!"
        } else {
            "يلا يا $name.. وقت التركيز بدأ وممنوع لمس الموبايل!"
        }
        ttsManager.speak(startMessage, pitch = speechPitch.value, rate = speechRate.value)

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeLeftSeconds.value > 0 && _timerRunning.value) {
                delay(1000)
                _timeLeftSeconds.value -= 1
            }

            if (_timeLeftSeconds.value <= 0 && _timerRunning.value) {
                onTimerFinished()
            }
        }
    }

    fun pauseTimer() {
        _timerRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _timeLeftSeconds.value = _selectedDurationMinutes.value * 60
    }

    private fun onTimerFinished() {
        _timerRunning.value = false
        val duration = _selectedDurationMinutes.value
        val name = userName.value
        triggerSuccessVibration()

        if (!_isBreak.value) {
            repository.addFocusMinutes(duration)
            val finishSpeech = "عاش يا $name يا وحش! خلصت جلسة الـ $duration دقيقة.. بطلت تبقى فاشل خلاص!"
            ttsManager.speak(finishSpeech, pitch = speechPitch.value, rate = speechRate.value)
        } else {
            val breakEndSpeech = "الاستراحة خلصت يا $name.. كفاية دلع وارجع للمذاكرة!"
            ttsManager.speak(breakEndSpeech, pitch = speechPitch.value, rate = speechRate.value)
        }
    }

    // Task management
    fun addTask(subject: String, title: String, estimatedMinutes: Int) {
        triggerHaptic()
        repository.addTask(subject, title, estimatedMinutes)
    }

    fun toggleTask(id: String) {
        triggerHaptic()
        repository.toggleTask(id)
    }

    fun deleteTask(id: String) {
        triggerHaptic()
        repository.deleteTask(id)
    }

    // Settings
    fun updateUserName(name: String) {
        repository.setUserName(name)
    }

    fun updateAutoGreeting(enabled: Boolean) {
        repository.setAutoGreeting(enabled)
    }

    fun updateSpeechSettings(pitch: Float, rate: Float) {
        repository.setSpeechSettings(pitch, rate)
    }

    private fun triggerHaptic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(45)
            }
        } catch (_: Exception) {}
    }

    private fun triggerSuccessVibration() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 100, 80, 150, 80, 200),
                        -1
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(300)
            }
        } catch (_: Exception) {}
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        ttsManager.shutdown()
    }
}
