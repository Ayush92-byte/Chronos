# 🕰️ Chronos – Smart Reminder App

Chronos is a modern reminder app built using **Jetpack Compose** and Firebase, designed to help you manage your tasks efficiently with features like smart scheduling, image-based reminders, and notifications.

---

## ✨ Features

### ✅ Reminder Management
- Create, edit, and delete reminders
- Each reminder includes:
  - Title
  - Description
  - Priority (Low, Medium, High)
  - Date & Time
  - Optional Notes
  - Optional Circular Image

### 🖼️ Image Support with Firebase Storage
- Pick images from camera or gallery
- Uploads image to Firebase Storage
- Stores image URL in Firestore
- Displays image with the corresponding task

### 🔔 Smart Notifications
- Schedule local notifications using `AlarmManager` or `WorkManager`
- Triggers notifications at the scheduled reminder time
- Handles runtime notification permissions for Android 13+

### ☁️ Firebase Integration
- All reminders are stored in **Firebase Firestore**
- Real-time syncing of tasks
- No local Room database used

### 🧠 Clean Architecture
- MVVM + Repository pattern
- Modular and scalable codebase
- Uses Hilt for dependency injection

---

## 🛠️ Built With
- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase Firestore](https://firebase.google.com/docs/firestore)
- [Firebase Storage](https://firebase.google.com/docs/storage)
- [AlarmManager / WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

---
