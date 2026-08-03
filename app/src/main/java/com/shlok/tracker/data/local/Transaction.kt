package com.shlok.tracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "subscription_id")
    val subscriptionId: Long? = null,
    
    @ColumnInfo(name = "sender_address")
    val senderAddress: String, 
    
    @ColumnInfo(name = "raw_sms_body")
    val rawSmsBody: String, 
    
    @ColumnInfo(name = "amount")
    val amount: Double, 
    
    @ColumnInfo(name = "currency", defaultValue = "INR") 
    val currency: String,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: Long, 
    
    @ColumnInfo(name = "is_subscription_candidate")
    val isSubscriptionCandidate: Boolean 
)
