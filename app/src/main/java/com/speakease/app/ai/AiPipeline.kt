package com.speakease.app.ai

import com.speakease.app.data.GestureRepository
import com.speakease.app.domain.EmotionContext
import com.speakease.app.domain.PredictionResult
import kotlin.random.Random

/**
 * Production wiring point for MediaPipe landmarks and TensorFlow Lite inference.
 * This prototype returns deterministic-style sample predictions for UI and flow testing.
 */
class AiPipeline(
    private val repository: GestureRepository = GestureRepository()
) {
    private var currentIndex = 0

    fun predictFromFrame(): PredictionResult {
        val gesture = repository.gestures[currentIndex % repository.gestures.size]
        val context = EmotionContext.entries.random()
        val confidence = Random.nextDouble(0.86, 0.98).toFloat()
        currentIndex++
        return PredictionResult(gesture = gesture, context = context, confidence = confidence)
    }
}
