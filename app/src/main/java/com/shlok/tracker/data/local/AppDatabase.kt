package com.shlok.tracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Transaction::class, Subscription::class], 
    version = 2, 
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun subscriptionDao(): SubscriptionDao
}
