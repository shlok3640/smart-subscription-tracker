package com.shlok.tracker.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Settings : Screen("settings")
    object SubscriptionDetails : Screen("subscription_details/{subscriptionId}") {
        fun createRoute(subscriptionId: Long) = "subscription_details/$subscriptionId"
    }
}
