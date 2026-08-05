package com.shlok.tracker.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun isBiometricEnabled(): Flow<Boolean>
    suspend fun setBiometricEnabled(enabled: Boolean)
}
