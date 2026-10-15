package com.example.tts

import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class HagagTtsManager(
    private val context: Context,
    private val onReadyCallback: (() -> Unit)? = null
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _lastSpoken = MutableStateFlow("")
    val lastSpoken: StateFlow<String> = _lastSpoken.asStateFlow()

    private var pendingSpeech: String? = null

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val ttsEngine = tts ?: return
            
            // Try Arabic (Egypt) first, then generic Arabic, then system default
            val arabicEg = Locale("ar", "EG")
            val arabic = Locale("ar")
            val result = ttsEngine.setLanguage(arabicEg)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                val genericResult = ttsEngine.setLanguage(arabic)
                if (genericResult == TextToSpeech.LANG_MISSING_DATA || genericResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w("HagagTts", "Arabic language data missing or not supported on this device. Using default.")
                    ttsEngine.language = Locale.getDefault()
                }
            }

            ttsEngine.setPitch(1.05f)
            ttsEngine.setSpeechRate(0.95f)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ttsEngine.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
            }

            ttsEngine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    _isSpeaking.value = false
                }
            })

            _isReady.value = true
            onReadyCallback?.invoke()

            // If a speech request came before initialization completed
            pendingSpeech?.let { speech ->
                pendingSpeech = null
                speak(speech)
            }
        } else {
            Log.e("HagagTts", "TextToSpeech initialization failed with status $status")
        }
    }

    fun speak(text: String, pitch: Float = 1.05f, rate: Float = 0.95f) {
        _lastSpoken.value = text
        val ttsEngine = tts
        if (ttsEngine == null || !_isReady.value) {
            pendingSpeech = text
            return
        }

        try {
            ttsEngine.setPitch(pitch)
            ttsEngine.setSpeechRate(rate)

            val utteranceId = "HagagUtterance_${System.currentTimeMillis()}"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val params = Bundle().apply {
                    putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
                }
                ttsEngine.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
            } else {
                val params = HashMap<String, String>().apply {
                    put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
                    put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1.0")
                }
                @Suppress("DEPRECATION")
                ttsEngine.speak(text, TextToSpeech.QUEUE_FLUSH, params)
            }
        } catch (e: Exception) {
            Log.e("HagagTts", "Error while speaking text: ${e.message}")
        }
    }

    fun speakGreeting(userName: String = "حجاج") {
        speak("ازيك يا $userName.. ذاكر يا فاشل!")
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
