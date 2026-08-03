package com.shlok.tracker.presentation

import com.shlok.tracker.data.local.Subscription
import com.shlok.tracker.data.local.Transaction

data class DashboardUiState(
    val activeSubscriptions: List<Subscription> = emptyList(),
    val subscriptionCandidates: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
    val totalMonthlySpendINR: Double = 0.0 
)
