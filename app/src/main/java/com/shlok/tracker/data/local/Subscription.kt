package com.shlok.tracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subscriptions",
    indices = [Index(value = ["service_name"], unique = true)]
)
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "service_name")
    val serviceName: String,
    
    @ColumnInfo(name = "recurring_amount")
    val recurringAmount: Double,
    
    @ColumnInfo(name = "currency", defaultValue = "INR")
    val currency: String,
    
    @ColumnInfo(name = "billing_cycle_days")
    val billingCycleDays: Int,
    
    @ColumnInfo(name = "last_billed_timestamp")
    val lastBilledTimestamp: Long,
    
    @ColumnInfo(name = "next_billing_timestamp")
    val nextBillingTimestamp: Long,
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean
)
