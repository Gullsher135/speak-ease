# SpeakEase - AI Gesture & Emotion Communication Assistant

SpeakEase is an Android offline-first communication app prototype based on the FYP proposal.
It converts recognized gestures and basic emotion context into meaningful sentences and voice output
in English and Urdu.

## Tech Stack (as requested by proposal)

- Android app: Kotlin
- UI: Jetpack Compose (Material 3 modern UI)
- Computer vision: MediaPipe (integration dependency added)
- On-device inference: TensorFlow Lite (integration dependency added)
- Speech: Android offline Text-to-Speech
- Data/model preparation (development phase): Python + TensorFlow/Keras (as in proposal)

## Implemented Features

- Modern, clean, caregiver-friendly UI
- Real-time communication screen with start/stop pipeline flow
- Gesture prediction pipeline service structure (`AiPipeline`)
- Emotion/context layer (`NEUTRAL`, `URGENT`, `CONFUSED`)
- Sentence builder rules using gesture + context
- Bilingual output support (English + Urdu)
- Offline TTS manager for spoken output
- Recent sentence history screen
- Settings screen for language and offline-first behavior
- **Gesture Guide feature**:
  - Teaches each configured gesture
  - Shows gesture hint
  - Shows exact generated sentence in English and Urdu

## Project Structure

- `app/src/main/java/com/speakease/app/ai` - MediaPipe/TFLite pipeline integration point
- `app/src/main/java/com/speakease/app/domain` - models and sentence building rules
- `app/src/main/java/com/speakease/app/audio` - offline TTS manager
- `app/src/main/java/com/speakease/app/data` - gesture definitions and mappings
- `app/src/main/java/com/speakease/app/ui` - view model and Compose screens

## Next Integration Steps (Model + Camera)

1. Replace mock `predictFromFrame()` in `AiPipeline` with:
   - CameraX frame stream
   - MediaPipe landmark extraction
   - TFLite gesture model inference
   - TFLite emotion/context inference
2. Add model files in `app/src/main/assets/`:
   - `gesture_model.tflite`
   - `emotion_model.tflite`
3. Expand `GestureRepository` with your finalized FYP gesture vocabulary.
4. Add evaluation scripts (Python) for confusion matrix and latency logs.

## Run

1. Open this folder in Android Studio.
2. Let Gradle sync project dependencies.
3. Run on an Android 10+ device/emulator.

