package com.shlok.tracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE subscription_id = :subscriptionId ORDER BY timestamp DESC")
    fun getTransactionsForSubscription(subscriptionId: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE is_subscription_candidate = 1 AND subscription_id IS NULL ORDER BY timestamp DESC")
    fun getSubscriptionCandidates(): Flow<List<Transaction>>
}
