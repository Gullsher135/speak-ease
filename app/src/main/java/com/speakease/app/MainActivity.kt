package com.speakease.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.speakease.app.ui.SpeakEaseApp
import com.speakease.app.ui.theme.SpeakEaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeakEaseTheme {
                SpeakEaseApp()
            }
        }
    }
}
