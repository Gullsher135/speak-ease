package com.speakease.app.domain

class SentenceBuilder {
    fun build(
        baseSentence: String,
        context: EmotionContext,
        language: Language
    ): CommunicationOutput {
        val contextualText = when (language) {
            Language.ENGLISH -> applyEnglishContext(baseSentence, context)
            Language.URDU -> applyUrduContext(baseSentence, context)
        }
        return CommunicationOutput(text = contextualText, language = language, context = context)
    }

    private fun applyEnglishContext(text: String, context: EmotionContext): String = when (context) {
        EmotionContext.NEUTRAL -> text
        EmotionContext.URGENT -> "Urgent: $text"
        EmotionContext.CONFUSED -> "I am confused. $text"
    }

    private fun applyUrduContext(text: String, context: EmotionContext): String = when (context) {
        EmotionContext.NEUTRAL -> text
        EmotionContext.URGENT -> "فوری: $text"
        EmotionContext.CONFUSED -> "میں الجھن میں ہوں۔ $text"
    }
}
