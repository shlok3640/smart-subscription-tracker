package com.shlok.tracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shlok.tracker.data.local.Subscription
import com.shlok.tracker.data.local.Transaction
import com.shlok.tracker.domain.repository.SettingsRepository
import com.shlok.tracker.domain.repository.TrackerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val repository: TrackerRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    val isBiometricEnabled: StateFlow<Boolean> = settingsRepository.isBiometricEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    init {
        loadDashboardData()
        injectMockDataForGraph()
    }

    private fun injectMockDataForGraph() {
        viewModelScope.launch {
            // First, clean up any duplicates that might have been created
            repository.deleteDuplicateSubscriptions()

            val currentTime = System.currentTimeMillis()
            val dayInMillis = TimeUnit.DAYS.toMillis(1)
            val mocks = listOf(
                Subscription(
                    serviceName = "Spotify",
                    recurringAmount = 119.0,
                    currency = "INR",
                    billingCycleDays = 30,
                    lastBilledTimestamp = currentTime,
                    nextBillingTimestamp = currentTime + (dayInMillis * 5),
                    isActive = true
                ),
                Subscription(
                    serviceName = "Amazon Prime",
                    recurringAmount = 299.0,
                    currency = "INR",
                    billingCycleDays = 30,
                    lastBilledTimestamp = currentTime,
                    nextBillingTimestamp = currentTime + (dayInMillis * 12),
                    isActive = true
                ),
                Subscription(
                    serviceName = "Gym Membership",
                    recurringAmount = 1500.0,
                    currency = "INR",
                    billingCycleDays = 30,
                    lastBilledTimestamp = currentTime,
                    nextBillingTimestamp = currentTime + (dayInMillis * 18),
                    isActive = true
                ),
                Subscription(
                    serviceName = "Disney+",
                    recurringAmount = 899.0,
                    currency = "INR",
                    billingCycleDays = 365,
                    lastBilledTimestamp = currentTime,
                    nextBillingTimestamp = currentTime + (dayInMillis * 26),
                    isActive = true
                )
            )
            mocks.forEach { repository.insertSubscription(it) }
        }
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            combine(
                repository.getActiveSubscriptions(),
                repository.getSubscriptionCandidates()
            ) { activeSubs, candidates ->
                val totalSpend = activeSubs.sumOf { 
                    if (it.currency == "INR") it.recurringAmount else 0.0
                }

                DashboardUiState(
                    activeSubscriptions = activeSubs,
                    subscriptionCandidates = candidates,
                    isLoading = false,
                    totalMonthlySpendINR = totalSpend
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun confirmCandidateAsSubscription(transaction: Transaction, serviceName: String, billingCycleDays: Int) {
        viewModelScope.launch {
            val nextBillingTime = transaction.timestamp + TimeUnit.DAYS.toMillis(billingCycleDays.toLong())
            
            val newSubscription = Subscription(
                serviceName = serviceName,
                recurringAmount = transaction.amount,
                currency = transaction.currency,
                billingCycleDays = billingCycleDays,
                lastBilledTimestamp = transaction.timestamp,
                nextBillingTimestamp = nextBillingTime,
                isActive = true
            )
            
            val subscriptionId = repository.insertSubscription(newSubscription)
            
            // Mark the transaction as no longer a candidate and link it to the new subscription
            val updatedTransaction = transaction.copy(
                isSubscriptionCandidate = false,
                subscriptionId = subscriptionId
            )
            repository.insertTransaction(updatedTransaction)
        }
    }
    
    fun dismissCandidate(transaction: Transaction) {
        viewModelScope.launch {
            val updatedTransaction = transaction.copy(isSubscriptionCandidate = false)
            repository.insertTransaction(updatedTransaction)
        }
    }

    fun deleteSubscription(id: Long) {
        viewModelScope.launch {
            repository.deleteSubscription(id)
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setBiometricEnabled(enabled)
        }
    }
}
