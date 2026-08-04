# 💸 Smart Subscription Tracker

An offline-first, privacy-focused Android application that automates subscription tracking by locally parsing bank SMS messages. Built natively for Android using Modern Kotlin, Jetpack Compose, and Clean Architecture.

## 🌟 About the Project
Managing subscriptions is a chore, and most apps require invasive access to your bank accounts via OAuth. **Smart Subscription Tracker** takes a different approach: it leverages local SMS intents to detect financial transactions in real-time, extracting subscription data entirely on-device. It combines high-level security with professional-grade data visualization to give users a clear forecast of their upcoming expenses.

## 🖼 Screenshots

| Biometric Login | Predictive Cashflow Dashboard | Home Screen Widget |
| :---: | :---: | :---: |
| <img width="250" src="https://github.com/user-attachments/assets/0b0eb765-61fc-431e-97d8-bfb17ec9aa81" /> | <img width="250" src="https://github.com/user-attachments/assets/7a8e62fb-3731-45ac-9aae-e7f5dc0cc8df" /> | <img width="250" src="https://github.com/user-attachments/assets/09ab8d8a-d930-4d28-8ee8-bf379a0890fb" /> |

## 🚀 Technical Highlights

*   **Privacy-First SMS Parsing:** Utilizes a background `BroadcastReceiver` to intercept banking SMS intents. Implements a robust Regex engine (`SmsParser`) to extract amounts, currencies, and recurring keywords without any cloud processing.
*   **Custom Mathematical Rendering:** Features a Predictive Cashflow Dashboard built using the Jetpack Compose `Canvas` API. It maps raw Unix timestamps to a floating coordinate plane and renders mathematically smooth Bezier curves using `Path.cubicTo()`.
*   **Biometric Security:** Integrated `androidx.biometric` with strong authentication and device credential fallback (PIN/Pattern) to protect sensitive financial data.
*   **OS-Level Integration:** Implements a **Jetpack Glance Home Screen Widget** that queries the Room database via Coroutines to display the closest upcoming charge.
*   **Robust Background Work:** Uses **WorkManager** to schedule daily subscription checks and trigger system-level notifications 3 days before a charge is due.

## 🛠 Tech Stack & Architecture

*   **Language:** Kotlin (2.0)
*   **Architecture:** Clean Architecture (Domain, Data, Presentation) + MVVM / UDF (Unidirectional Data Flow)
*   **UI:** Jetpack Compose, Material Design 3, Jetpack Glance (Widgets)
*   **Dependency Injection:** Dagger-Hilt
*   **Database:** Room (SQLite) with Coroutines Flow for reactive updates
*   **Background Processing:** WorkManager & BroadcastReceivers
*   **Security:** AndroidX Biometric

## 🧪 Testing & CI/CD
*   **Unit Testing:** Comprehensive JUnit 4 tests for core logic (e.g., `SmsParserTest`).
*   **CI/CD:** Automated GitHub Actions pipeline that verifies code integrity by running tests and building the debug APK on every push.

## ⚙️ How to Build Locally

1. Clone the repository: `git clone https://github.com/shlok3640/smart-subscription-tracker.git`
2. Open the project in **Android Studio**.
3. Ensure you have the Android SDK 34+ installed.
4. Click `Sync Project with Gradle Files`.
5. Build and run on a physical device or emulator.

---
*Created by [Your Name] - [Your Portfolio/LinkedIn Link]*
