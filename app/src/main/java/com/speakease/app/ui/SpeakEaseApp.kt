package com.speakease.app.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.speakease.app.audio.TtsManager
import com.speakease.app.domain.GestureDefinition
import com.speakease.app.domain.Language

private enum class Destination { Live, Guide, History, Settings }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakEaseApp(vm: SpeakEaseViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current
    val tts = remember { TtsManager(context) }
    var destination by remember { mutableStateOf(Destination.Live) }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SpeakEase") },
                actions = {
                    IconButton(onClick = { destination = Destination.Guide }) {
                        Icon(Icons.Default.MenuBook, contentDescription = "Gesture Guide")
                    }
                    IconButton(onClick = { destination = Destination.Settings }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = destination == Destination.Live,
                    onClick = { destination = Destination.Live },
                    icon = { Icon(Icons.Default.Cameraswitch, null) },
                    label = { Text("Live") }
                )
                NavigationBarItem(
                    selected = destination == Destination.Guide,
                    onClick = { destination = Destination.Guide },
                    icon = { Icon(Icons.Default.MenuBook, null) },
                    label = { Text("Guide") }
                )
                NavigationBarItem(
                    selected = destination == Destination.History,
                    onClick = { destination = Destination.History },
                    icon = { Icon(Icons.Default.RecordVoiceOver, null) },
                    label = { Text("History") }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                        )
                    )
                )
                .padding(padding)
        ) {
            when (destination) {
                Destination.Live -> LiveScreen(
                    state = state,
                    hasCameraPermission = hasCameraPermission,
                    onRequestPermission = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    onToggle = vm::toggleListening,
                    onLanguageChanged = vm::setLanguage,
                    onSpeak = { text -> tts.speak(text, state.selectedLanguage) }
                )
                Destination.Guide -> GestureGuideScreen(gestures = state.gestures)
                Destination.History -> HistoryScreen(state = state)
                Destination.Settings -> SettingsScreen(selectedLanguage = state.selectedLanguage, onLanguageChanged = vm::setLanguage)
            }
        }
    }
}

@Composable
private fun LiveScreen(
    state: SpeakEaseUiState,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit,
    onToggle: () -> Unit,
    onLanguageChanged: (Language) -> Unit,
    onSpeak: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
                    Text("Real-time Communication", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        if (hasCameraPermission) "Camera ready for gesture detection."
                        else "Camera permission required for live detection."
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (!hasCameraPermission) {
                        Button(onClick = onRequestPermission) { Text("Allow Camera Access") }
                    } else {
                        Button(onClick = onToggle) {
                            Text(if (state.isRunning) "Stop Listening" else "Start Listening")
                        }
                    }
                }
            }
        }

        item {
            LanguageSegment(state.selectedLanguage, onLanguageChanged)
        }

        item {
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("Generated Sentence", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.latestOutput?.text ?: "No gesture recognized yet.",
                        fontSize = 18.sp,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AssistChip(
                            onClick = {},
                            label = { Text("Confidence ${(state.confidence * 100).toInt()}%") }
                        )
                        Button(
                            onClick = { state.latestOutput?.let { onSpeak(it.text) } },
                            enabled = state.latestOutput != null
                        ) { Text("Speak") }
                    }
                }
            }
        }
    }
}

@Composable
private fun GestureGuideScreen(gestures: List<GestureDefinition>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Gesture Guide", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Learn each gesture and what sentence it generates in English and Urdu.")
                }
            }
        }

        items(gestures) { gesture ->
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(gesture.title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Gesture Tip: ${gesture.hint}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("EN: ${gesture.englishSentence}")
                    Text("UR: ${gesture.urduSentence}")
                }
            }
        }
    }
}

@Composable
private fun HistoryScreen(state: SpeakEaseUiState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Text("Recent Sentences", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
        }
        items(state.history) { item ->
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(item.text)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Context: ${item.context}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun SettingsScreen(selectedLanguage: Language, onLanguageChanged: (Language) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(shape = RoundedCornerShape(18.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Output Language", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LanguageSegment(selectedLanguage, onLanguageChanged)
            }
        }
        Card(shape = RoundedCornerShape(18.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Offline-first Design", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text("SpeakEase is designed for on-device gesture processing and offline voice output.")
            }
        }
    }
}

@Composable
private fun LanguageSegment(selectedLanguage: Language, onLanguageChanged: (Language) -> Unit) {
    val options = listOf(Language.ENGLISH, Language.URDU)
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, language ->
            SegmentedButton(
                shape = androidx.compose.material3.SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { onLanguageChanged(language) },
                selected = selectedLanguage == language,
                label = { Text(if (language == Language.ENGLISH) "English" else "اردو") },
                icon = {}
            )
        }
    }
}
