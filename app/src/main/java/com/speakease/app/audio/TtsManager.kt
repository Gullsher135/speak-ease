package com.speakease.app.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import com.speakease.app.domain.Language
import java.util.Locale

class TtsManager(context: Context) {
    private var ready = false
    private val tts = TextToSpeech(context) { status ->
        ready = status == TextToSpeech.SUCCESS
    }

    fun speak(text: String, language: Language) {
        if (!ready) return
        val locale = when (language) {
            Language.ENGLISH -> Locale.UK
            Language.URDU -> Locale("ur", "PK")
        }
        tts.language = locale
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "speakease_utterance")
    }

    fun release() {
        tts.stop()
        tts.shutdown()
    }
}
