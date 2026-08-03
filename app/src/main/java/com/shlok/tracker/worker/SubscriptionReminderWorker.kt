package com.shlok.tracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shlok.tracker.domain.repository.TrackerRepository
import com.shlok.tracker.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import androidx.hilt.work.HiltWorker
import java.util.concurrent.TimeUnit

@HiltWorker
class SubscriptionReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TrackerRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val currentTime = System.currentTimeMillis()
            val threeDaysFromNow = currentTime + TimeUnit.DAYS.toMillis(3)
            
            val upcomingSubscriptions = repository.getUpcomingSubscriptions(
                startTime = currentTime,
                endTime = threeDaysFromNow
            )

            for (sub in upcomingSubscriptions) {
                notificationHelper.showReminderNotification(
                    subscriptionId = sub.id,
                    serviceName = sub.serviceName,
                    amount = sub.recurringAmount,
                    currency = sub.currency
                )
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
