package com.example.ai

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.content.ContextCompat
import com.example.tts.HagagTtsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LiveState {
    object Idle : LiveState()
    object Listening : LiveState()
    object Thinking : LiveState()
    object Speaking : LiveState()
    data class NoPermission(val message: String) : LiveState()
    data class Offline(val message: String) : LiveState()
    data class Error(val message: String) : LiveState()
}

class HagagVoiceLiveManager(
    private val context: Context,
    private val ttsManager: HagagTtsManager,
    private val scope: CoroutineScope
) {
    private var speechRecognizer: SpeechRecognizer? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    private val _liveState = MutableStateFlow<LiveState>(LiveState.Idle)
    val liveState: StateFlow<LiveState> = _liveState.asStateFlow()

    private val _userSpokenText = MutableStateFlow("")
    val userSpokenText: StateFlow<String> = _userSpokenText.asStateFlow()

    private val _assistantReplyText = MutableStateFlow("")
    val assistantReplyText: StateFlow<String> = _assistantReplyText.asStateFlow()

    private val _soundLevel = MutableStateFlow(0f)
    val soundLevel: StateFlow<Float> = _soundLevel.asStateFlow()

    private var activeJob: Job? = null

    init {
        mainHandler.post {
            try {
                if (SpeechRecognizer.isRecognitionAvailable(context)) {
                    setupRecognizer()
                }
            } catch (_: Exception) {}
        }
    }

    private fun setupRecognizer() {
        try {
            speechRecognizer?.destroy()
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _liveState.value = LiveState.Listening
                    }

                    override fun onBeginningOfSpeech() {}

                    override fun onRmsChanged(rmsdB: Float) {
                        _soundLevel.value = ((rmsdB + 2f) / 10f).coerceIn(0f, 1f)
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        _liveState.value = LiveState.Thinking
                    }

                    override fun onError(error: Int) {
                        _soundLevel.value = 0f
                        val friendlyMessage = when (error) {
                            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                                "يا حجاج النت فاصل عندك! وصّل النت وتعال اتكلم مع مستر حجاج!"
                            SpeechRecognizer.ERROR_NO_MATCH ->
                                "مسامعتكش كويس يا حجاج.. دوس على المايك واتكلم تاني بصوت أوضح يا بطل!"
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                                "يا حجاج التطبيق محتاج إذن الميكروفون عشان مستر حجاج يسمعك!"
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                                "مستر حجاج مستنيك.. دوس على المايك وقول سؤالك يا بطل!"
                            else ->
                                "حصلت لخبطة بسيطة في الصوت.. دوس تاني وجرب يا حجاج!"
                        }

                        if (error == SpeechRecognizer.ERROR_NETWORK || error == SpeechRecognizer.ERROR_NETWORK_TIMEOUT) {
                            _liveState.value = LiveState.Offline(friendlyMessage)
                        } else if (error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS) {
                            _liveState.value = LiveState.NoPermission(friendlyMessage)
                        } else {
                            _liveState.value = LiveState.Error(friendlyMessage)
                        }
                    }

                    override fun onResults(results: Bundle?) {
                        _soundLevel.value = 0f
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull() ?: ""
                        if (text.isNotBlank()) {
                            _userSpokenText.value = text
                            processUserVoice(text)
                        } else {
                            _liveState.value = LiveState.Idle
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull() ?: ""
                        if (text.isNotBlank()) {
                            _userSpokenText.value = text
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
        } catch (_: Exception) {}
    }

    fun hasRecordPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isOnline(): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Exception) {
            false
        }
    }

    fun startListening(studentName: String = "حجاج") {
        // Check permission first
        if (!hasRecordPermission()) {
            _liveState.value = LiveState.NoPermission("يا حجاج لازم تدوس سماح للميكروفون الأول عشان أسمعك وما تزوغش من المذاكرة! 🎙️")
            return
        }

        // Check network connection
        if (!isOnline()) {
            _liveState.value = LiveState.Offline("يا حجاج مافيش إنترنت دلوقتي! وصّل الواي فاي أو الباقة وتعال كلم مستر حجاج، أو جرب الأسئلة السريعة الأوفلاين! 🌐")
            return
        }

        // Stop any current audio
        ttsManager.stop()
        _liveState.value = LiveState.Listening
        _userSpokenText.value = ""

        mainHandler.post {
            try {
                if (speechRecognizer == null) {
                    setupRecognizer()
                }
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG")
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ar-EG")
                    putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "ar-EG")
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                }
                speechRecognizer?.startListening(intent)
            } catch (e: Exception) {
                _liveState.value = LiveState.Error("الميكروفون مش متاح دلوقتي على جهازك يا حجاج، تقدر تستخدم الأسئلة السريعة!")
            }
        }
    }

    fun stopListening() {
        mainHandler.post {
            try {
                speechRecognizer?.stopListening()
            } catch (_: Exception) {}
        }
    }

    fun sendDirectPrompt(query: String, studentName: String = "حجاج") {
        ttsManager.stop()
        _userSpokenText.value = query
        processUserVoice(query, studentName)
    }

    private fun processUserVoice(query: String, studentName: String = "حجاج") {
        _liveState.value = LiveState.Thinking

        activeJob?.cancel()
        activeJob = scope.launch(Dispatchers.IO) {
            val reply = GeminiChatService.sendMessage(
                history = emptyList(),
                userMessage = "سؤال صوتي مباشر من الطالب $studentName: $query. جاوب بصوتك بالعامية المصرية في المذاكرة بس بإيجاز مرح (جملتين تلاتة).",
                studentName = studentName
            )

            _assistantReplyText.value = reply
            _liveState.value = LiveState.Speaking

            launch(Dispatchers.Main) {
                ttsManager.speak(reply)
                // When TTS completes or after estimated duration, reset to Idle
                val estimatedDurationMs = (reply.length * 85L).coerceIn(3000L, 14000L)
                mainHandler.postDelayed({
                    if (_liveState.value == LiveState.Speaking) {
                        _liveState.value = LiveState.Idle
                    }
                }, estimatedDurationMs)
            }
        }
    }

    fun stopSpeaking() {
        ttsManager.stop()
        _liveState.value = LiveState.Idle
    }

    fun stopAll() {
        activeJob?.cancel()
        ttsManager.stop()
        mainHandler.post {
            try {
                speechRecognizer?.cancel()
            } catch (_: Exception) {}
        }
        _liveState.value = LiveState.Idle
        _soundLevel.value = 0f
    }

    fun resetState() {
        _liveState.value = LiveState.Idle
    }

    fun destroy() {
        stopAll()
        mainHandler.post {
            try {
                speechRecognizer?.destroy()
                speechRecognizer = null
            } catch (_: Exception) {}
        }
    }
}
