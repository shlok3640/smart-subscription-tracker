package com.shlok.tracker.domain.repository

import com.shlok.tracker.data.local.Subscription
import com.shlok.tracker.data.local.Transaction
import kotlinx.coroutines.flow.Flow

interface TrackerRepository {
    suspend fun insertTransaction(transaction: Transaction)
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsForSubscription(subscriptionId: Long): Flow<List<Transaction>>
    fun getSubscriptionCandidates(): Flow<List<Transaction>>

    suspend fun insertSubscription(subscription: Subscription): Long
    suspend fun updateSubscription(subscription: Subscription)
    fun getActiveSubscriptions(): Flow<List<Subscription>>
    
    suspend fun getUpcomingSubscriptions(startTime: Long, endTime: Long): List<Subscription>

    suspend fun deleteSubscription(id: Long)

    suspend fun deleteDuplicateSubscriptions()
}
