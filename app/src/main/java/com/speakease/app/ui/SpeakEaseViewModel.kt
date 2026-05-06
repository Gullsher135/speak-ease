package com.speakease.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speakease.app.ai.AiPipeline
import com.speakease.app.data.GestureRepository
import com.speakease.app.domain.CommunicationOutput
import com.speakease.app.domain.GestureDefinition
import com.speakease.app.domain.Language
import com.speakease.app.domain.SentenceBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SpeakEaseUiState(
    val isRunning: Boolean = false,
    val selectedLanguage: Language = Language.ENGLISH,
    val latestOutput: CommunicationOutput? = null,
    val history: List<CommunicationOutput> = emptyList(),
    val confidence: Float = 0f,
    val gestures: List<GestureDefinition> = emptyList()
)

class SpeakEaseViewModel : ViewModel() {
    private val repository = GestureRepository()
    private val aiPipeline = AiPipeline(repository)
    private val sentenceBuilder = SentenceBuilder()

    private val _uiState = MutableStateFlow(
        SpeakEaseUiState(gestures = repository.gestures)
    )
    val uiState: StateFlow<SpeakEaseUiState> = _uiState.asStateFlow()

    fun setLanguage(language: Language) {
        _uiState.update { it.copy(selectedLanguage = language) }
    }

    fun toggleListening() {
        val shouldRun = !_uiState.value.isRunning
        _uiState.update { it.copy(isRunning = shouldRun) }
        if (shouldRun) startPipeline()
    }

    private fun startPipeline() {
        viewModelScope.launch {
            while (_uiState.value.isRunning) {
                val result = aiPipeline.predictFromFrame()
                val text = if (_uiState.value.selectedLanguage == Language.ENGLISH) {
                    result.gesture.englishSentence
                } else {
                    result.gesture.urduSentence
                }
                val output = sentenceBuilder.build(
                    baseSentence = text,
                    context = result.context,
                    language = _uiState.value.selectedLanguage
                )

                _uiState.update { state ->
                    state.copy(
                        latestOutput = output,
                        confidence = result.confidence,
                        history = (listOf(output) + state.history).take(8)
                    )
                }
                delay(2500)
            }
        }
    }
}
