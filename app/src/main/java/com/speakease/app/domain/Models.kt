package com.speakease.app.domain

enum class Language {
    ENGLISH,
    URDU
}

enum class EmotionContext {
    NEUTRAL,
    URGENT,
    CONFUSED
}

data class GestureDefinition(
    val id: String,
    val title: String,
    val englishSentence: String,
    val urduSentence: String,
    val hint: String
)

data class PredictionResult(
    val gesture: GestureDefinition,
    val context: EmotionContext,
    val confidence: Float
)

data class CommunicationOutput(
    val text: String,
    val language: Language,
    val context: EmotionContext
)
