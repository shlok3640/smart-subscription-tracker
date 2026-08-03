package com.shlok.tracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: Subscription): Long

    @Update
    suspend fun updateSubscription(subscription: Subscription): Unit

    @Query("SELECT * FROM subscriptions WHERE is_active = 1 ORDER BY next_billing_timestamp ASC")
    fun getActiveSubscriptions(): Flow<List<Subscription>>

    @Query("SELECT * FROM subscriptions WHERE is_active = 1 AND next_billing_timestamp BETWEEN :startTime AND :endTime")
    suspend fun getUpcomingSubscriptions(startTime: Long, endTime: Long): List<Subscription>

    @Query("DELETE FROM subscriptions WHERE id NOT IN (SELECT MIN(id) FROM subscriptions GROUP BY service_name)")
    suspend fun deleteDuplicateSubscriptions()
}
