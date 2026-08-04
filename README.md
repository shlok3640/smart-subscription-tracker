# 💸 Smart Subscription Tracker

An offline-first, privacy-focused Android application that automates subscription tracking by locally parsing bank SMS messages. Built natively for Android using Modern Kotlin, Jetpack Compose, and Clean Architecture.

## 🖼 Screenshots

| Biometric Login | Predictive Cashflow Dashboard | Home Screen Widget |
| :---: | :---: | :---: |
| <img width="250" alt="Screenshot_20260804_100626" src="https://github.com/user-attachments/assets/0b0eb765-61fc-431e-97d8-bfb17ec9aa81" /> | <img width="250" alt="Screenshot_20260804_100205" src="https://github.com/user-attachments/assets/43a5ddbc-b864-4298-81ba-e05a04fc234d" /> | <img width="250" alt="Screenshot_20260804_100332" src="https://github.com/user-attachments/assets/09ab8d8a-d930-4d28-8ee8-bf379a0890fb" /> |



## 🚀 Technical Highlights

* **Privacy-First SMS Parsing:** Instead of requiring invasive bank API OAuth, the app uses a background `BroadcastReceiver` to intercept banking SMS intents, parse amounts and keywords via regex, and securely log them offline.
* **Custom Mathematical Rendering:** The Predictive Cashflow Dashboard completely avoids 3rd-party charting libraries. It utilizes the Jetpack Compose `Canvas` API, mapping raw Unix timestamps to a floating coordinate plane and rendering smooth Bezier curves via `Path.cubicTo()`.
* **Biometric Security:** Integrated `androidx.biometric` to lock financial data behind device-level hardware (Fingerprint / FaceID).
* **OS-Level Integration:** Features a Jetpack Glance Home Screen Widget that queries the Room database via Coroutines to display the nearest upcoming charge without launching the app.

## 🛠 Tech Stack & Architecture

* **Language:** Kotlin (2.0)
* **Architecture:** Clean Architecture (Domain, Data, Presentation) + MVVM / UDF (Unidirectional Data Flow)
* **UI:** Jetpack Compose, Material Design 3, Jetpack Glance (Widgets)
* **Dependency Injection:** Dagger-Hilt
* **Database:** Room (SQLite) with Coroutines Flow
* **Background Processing:** WorkManager (24hr periodic scheduling) & BroadcastReceivers

## ⚙️ How to Build Locally

1. Clone the repository: `git clone https://github.com/shlok3640/smart-subscription-tracker.git`
2. Open the project in **Android Studio**.
3. Ensure you have the Android SDK 34+ installed.
4. Click `Sync Project with Gradle Files`.
5. Build and run on a physical device or emulator. 
