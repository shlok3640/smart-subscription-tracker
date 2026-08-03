package com.shlok.tracker.di

import com.shlok.tracker.data.repository.TrackerRepositoryImpl
import com.shlok.tracker.domain.repository.TrackerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTrackerRepository(
        trackerRepositoryImpl: TrackerRepositoryImpl
    ): TrackerRepository
}
