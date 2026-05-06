package com.speakease.app.data

import com.speakease.app.domain.GestureDefinition

class GestureRepository {
    val gestures: List<GestureDefinition> = listOf(
        GestureDefinition(
            id = "help",
            title = "Help",
            englishSentence = "Please help me.",
            urduSentence = "براہ کرم میری مدد کریں۔",
            hint = "Raise open hand and keep palm visible."
        ),
        GestureDefinition(
            id = "water",
            title = "Water",
            englishSentence = "I need water.",
            urduSentence = "مجھے پانی چاہیے۔",
            hint = "Cup hand near mouth, then point down."
        ),
        GestureDefinition(
            id = "pain",
            title = "Pain",
            englishSentence = "I am feeling pain.",
            urduSentence = "مجھے درد ہو رہا ہے۔",
            hint = "Touch the painful area and hold for one second."
        ),
        GestureDefinition(
            id = "emergency",
            title = "Emergency",
            englishSentence = "This is an emergency, call someone now.",
            urduSentence = "یہ ایمرجنسی ہے، ابھی کسی کو بلائیں۔",
            hint = "Wave hand quickly left to right twice."
        ),
        GestureDefinition(
            id = "thank_you",
            title = "Thank You",
            englishSentence = "Thank you for your support.",
            urduSentence = "آپ کی مدد کا شکریہ۔",
            hint = "Hand from chin forward, gentle motion."
        )
    )
}
