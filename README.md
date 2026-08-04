# 💸 Smart Subscription Tracker

An offline-first, privacy-focused Android application that automates subscription tracking by locally parsing bank SMS messages. Built natively for Android using Modern Kotlin, Jetpack Compose, and Clean Architecture.

## 🖼 Screenshots
*(Drag and drop your 3 screenshots below this line when editing on GitHub)*
* **Screenshot 1:** Biometric Login Screen
* **Screenshot 2:** Predictive Cashflow Canvas Dashboard
* **Screenshot 3:** Jetpack Glance Home Screen Widget

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
