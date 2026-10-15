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
import com.example.data.QuoteCategory
import com.example.data.QuizQuestion
import com.example.data.QuizRepository
import com.example.data.RoastQuote
import com.example.data.RoastQuotesRepository
import com.example.data.StudyReward
import com.example.data.StudySession
import com.example.data.StudyTask
import com.example.data.TaskRepository
import com.example.notification.HagagNotificationHelper
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val rewards: StateFlow<List<StudyReward>> = repository.rewards
    val sessions: StateFlow<List<StudySession>> = repository.sessions
    val focusMinutes: StateFlow<Int> = repository.focusMinutes
    val streakDays: StateFlow<Int> = repository.streakDays
    val streakBrokenAlert: StateFlow<String?> = repository.streakBrokenAlert
    val userName: StateFlow<String> = repository.userName

    // Gemini AI Chatbot state
    private val _chatMessages = MutableStateFlow<List<com.example.ai.ChatMessage>>(
        listOf(
            com.example.ai.ChatMessage(
                id = "welcome",
                sender = "model",
                text = "يا هلا بيك يا حبيب قلبي حجاج! أنا 'مستر حجاج الفرفوش'.. اسألني في أي مسألة أو درس في تانية إعدادي، أو قولي محتاج تشجيع لفتح المكافآت! 🧠✨"
            )
        )
    )
    val chatMessages: StateFlow<List<com.example.ai.ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()
    val autoGreeting: StateFlow<Boolean> = repository.autoGreeting
    val speechRate: StateFlow<Float> = repository.speechRate
    val speechPitch: StateFlow<Float> = repository.speechPitch

    val dailyReminderEnabled: StateFlow<Boolean> = repository.dailyReminderEnabled
    val reminderHour: StateFlow<Int> = repository.reminderHour
    val reminderMinute: StateFlow<Int> = repository.reminderMinute

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

    private val _timerSubject = MutableStateFlow("رياضيات")
    val timerSubject: StateFlow<String> = _timerSubject.asStateFlow()

    private var timerJob: Job? = null

    // Combined stats for Genius Meter and Statistics screen
    val stats: StateFlow<HagagStats> = combine(
        focusMinutes,
        tasks,
        sessions,
        streakDays,
        streakBrokenAlert
    ) { minutes, allTasks, allSessions, streak, brokenAlert ->
        val completed = allTasks.count { it.isCompleted }
        val now = System.currentTimeMillis()
        val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayStr = dayFormat.format(Date(now))

        val sessionsToday = allSessions.count {
            dayFormat.format(Date(it.timestamp)) == todayStr
        }

        val weekAgo = now - (7 * 24 * 60 * 60 * 1000L)
        val sessionsThisWeek = allSessions.count { it.timestamp >= weekAgo }

        // Find most studied subject
        val subjectCounts = allSessions.groupingBy { it.subject }.eachCount()
        val topSubject = subjectCounts.maxByOrNull { it.value }?.key ?: "رياضيات 📐"

        val progress = ((minutes * 1.5f + completed * 15f + streak * 10f) / 180f).coerceIn(0.05f, 1f)

        val level = when {
            streak == 0 && minutes < 25 -> "فاشل بامتياز مع مرتبة الشرف 😴"
            minutes < 50 -> "عايز يذاكر بس الشاي بيبرد ☕"
            minutes < 120 -> "طالب تانية إعدادي في مرحلة الصحوة 📖"
            else -> "دحيح الدفعة يا حجاج! 🎓🏆"
        }

        HagagStats(
            totalFocusMinutes = minutes,
            completedTasksCount = completed,
            streakDays = streak,
            sessionsToday = sessionsToday,
            sessionsThisWeek = sessionsThisWeek,
            mostStudiedSubject = topSubject,
            currentLevelTitle = level,
            progressToGenius = progress,
            streakBrokenAlert = brokenAlert
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HagagStats()
    )

    // ==========================================
    // QUIZ STATE (تانية إعدادي)
    // ==========================================
    private val _selectedQuizSubject = MutableStateFlow("رياضيات")
    val selectedQuizSubject: StateFlow<String> = _selectedQuizSubject.asStateFlow()

    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _selectedOption = MutableStateFlow<Int?>(null)
    val selectedOption: StateFlow<Int?> = _selectedOption.asStateFlow()

    private val _isAnswerSubmitted = MutableStateFlow(false)
    val isAnswerSubmitted: StateFlow<Boolean> = _isAnswerSubmitted.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _currentFeedbackComment = MutableStateFlow<String?>(null)
    val currentFeedbackComment: StateFlow<String?> = _currentFeedbackComment.asStateFlow()

    fun selectTab(tab: Int) {
        _currentTab.value = tab
    }

    fun shoutGreeting() {
        triggerHaptic()
        val name = userName.value
        val greetingText = "إزّيك يا $name.. ذاكر يا فاشل!"
        _currentQuote.value = RoastQuote(
            id = "greeting",
            text = "$greetingText 📢",
            spokenText = greetingText,
            category = QuoteCategory.START_SESSION,
            severityEmoji = "🔥",
            punchline = "الدفعة كلها بتذاكر وأنت ماسك الموبايل!"
        )
        ttsManager.speak(greetingText, pitch = speechPitch.value, rate = speechRate.value)
    }

    fun roastMeAgain(category: QuoteCategory? = null) {
        triggerHaptic()
        val newQuote = RoastQuotesRepository.getRandomQuote(category)
        _currentQuote.value = newQuote
        val name = userName.value
        val textToSpeak = newQuote.spokenText.replace("حجاج", name)
        ttsManager.speak(textToSpeak, pitch = speechPitch.value, rate = speechRate.value)
    }

    fun speakPhrase(phrase: String) {
        triggerHaptic()
        val name = userName.value
        val customized = phrase.replace("حجاج", name)
        ttsManager.speak(customized, pitch = speechPitch.value, rate = speechRate.value)
    }

    fun stopSpeaking() {
        ttsManager.stop()
    }

    fun dismissStreakBrokenAlert() {
        repository.dismissStreakBrokenAlert()
    }

    // Pomodoro Timer controls
    fun setTimerSubject(subject: String) {
        _timerSubject.value = subject
    }

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
        val subject = _timerSubject.value
        val startMessage = if (_isBreak.value) {
            "استراحة خمس دقايق يا $name.. اشرب شايك وبلاش سرحان في التيك توك!"
        } else {
            "يلا يا $name.. بدأت جلسة الـ $subject وممنوع لمس الموبايل خالص!"
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
        // Warn if paused
        if (!_isBreak.value) {
            roastMeAgain(QuoteCategory.ON_FAILURE)
        }
    }

    fun resetTimer() {
        _timerRunning.value = false
        timerJob?.cancel()
        _timeLeftSeconds.value = _selectedDurationMinutes.value * 60
    }

    fun onAppBackgrounded() {
        if (_timerRunning.value && !_isBreak.value) {
            // Trigger abandoned study shouting notification
            HagagNotificationHelper.showAbandonedStudyNotification(getApplication(), userName.value)
        }
    }

    private fun onTimerFinished() {
        _timerRunning.value = false
        val duration = _selectedDurationMinutes.value
        val name = userName.value
        val subject = _timerSubject.value
        triggerSuccessVibration()

        if (!_isBreak.value) {
            repository.recordCompletedSession(subject, duration)
            val successQuote = RoastQuotesRepository.getRandomQuote(QuoteCategory.POST_SUCCESS)
            _currentQuote.value = successQuote
            val finishSpeech = successQuote.spokenText.replace("حجاج", name)
            ttsManager.speak(finishSpeech, pitch = speechPitch.value, rate = speechRate.value)
        } else {
            val breakEndSpeech = "الاستراحة خلصت يا $name.. كفاية دلع وقوم ارجع للمذاكرة!"
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

    // ==========================================
    // QUIZ ACTIONS
    // ==========================================
    fun selectQuizSubject(subject: String) {
        _selectedQuizSubject.value = subject
        _currentQuizIndex.value = 0
        _selectedOption.value = null
        _isAnswerSubmitted.value = false
        _quizScore.value = 0
        _currentFeedbackComment.value = null
    }

    fun selectQuizOption(index: Int) {
        if (!_isAnswerSubmitted.value) {
            triggerHaptic()
            _selectedOption.value = index
        }
    }

    fun submitQuizAnswer() {
        val selected = _selectedOption.value ?: return
        if (_isAnswerSubmitted.value) return
        _isAnswerSubmitted.value = true

        val currentSubject = _selectedQuizSubject.value
        val list = QuizRepository.getQuestionsBySubject(currentSubject)
        val currentQ = list.getOrNull(_currentQuizIndex.value) ?: return

        val isCorrect = selected == currentQ.correctIndex
        val name = userName.value

        if (isCorrect) {
            _quizScore.value += 1
            triggerSuccessVibration()
            val comment = currentQ.correctEgyptianComment.replace("حجاج", name)
            _currentFeedbackComment.value = comment
            ttsManager.speak(comment, pitch = speechPitch.value, rate = speechRate.value)
        } else {
            triggerHaptic()
            val comment = currentQ.wrongEgyptianComment.replace("حجاج", name)
            _currentFeedbackComment.value = comment
            ttsManager.speak(comment, pitch = speechPitch.value, rate = speechRate.value)
        }
    }

    fun nextQuizQuestion() {
        val currentSubject = _selectedQuizSubject.value
        val list = QuizRepository.getQuestionsBySubject(currentSubject)
        if (_currentQuizIndex.value < list.size - 1) {
            _currentQuizIndex.value += 1
            _selectedOption.value = null
            _isAnswerSubmitted.value = false
            _currentFeedbackComment.value = null
        }
    }

    fun restartQuiz() {
        _currentQuizIndex.value = 0
        _selectedOption.value = null
        _isAnswerSubmitted.value = false
        _quizScore.value = 0
        _currentFeedbackComment.value = null
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

    fun updateDailyReminder(enabled: Boolean, hour: Int, minute: Int) {
        repository.setDailyReminder(enabled, hour, minute)
    }

    // ==========================================
    // REWARDS LOGIC (مكافآت حجاج)
    // ==========================================
    fun claimReward(rewardId: String) {
        val reward = rewards.value.find { it.id == rewardId } ?: return
        triggerSuccessVibration()
        repository.claimReward(rewardId)
        val name = userName.value
        val speech = "مبروك يا $name يا وحش! استلمت مكافأة ${reward.title}.. روح استمتع بيها تستاهل بعد تعبك ومذاكرتك!"
        ttsManager.speak(speech, pitch = speechPitch.value, rate = speechRate.value)
    }

    fun resetReward(rewardId: String) {
        triggerHaptic()
        repository.resetReward(rewardId)
    }

    fun addCustomReward(title: String, description: String, requiredSessions: Int, category: String, emoji: String) {
        triggerHaptic()
        repository.addCustomReward(title, description, requiredSessions, category, emoji)
    }

    fun deleteReward(rewardId: String) {
        triggerHaptic()
        repository.deleteReward(rewardId)
    }

    // ==========================================
    // GEMINI CHATBOT (مستر حجاج الذكي)
    // ==========================================
    fun sendChatMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank() || _isChatLoading.value) return

        val userMsg = com.example.ai.ChatMessage(
            id = "user_${System.currentTimeMillis()}",
            sender = "user",
            text = trimmed
        )
        _chatMessages.value = _chatMessages.value + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            val reply = com.example.ai.GeminiChatService.sendMessage(
                history = _chatMessages.value,
                userMessage = trimmed,
                studentName = userName.value
            )
            val modelMsg = com.example.ai.ChatMessage(
                id = "model_${System.currentTimeMillis()}",
                sender = "model",
                text = reply
            )
            _chatMessages.value = _chatMessages.value + modelMsg
            _isChatLoading.value = false
        }
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
