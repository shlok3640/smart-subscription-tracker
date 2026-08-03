package com.shlok.tracker.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.shlok.tracker.domain.repository.TrackerRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class NextSubscriptionWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var repository: TrackerRepository

    override val glanceAppWidget: GlanceAppWidget
        get() {
            // Note: In a real app, you'd use GlanceState to pass data to the widget
            // For this portfolio requirement, we query the repo synchronously (simplicity)
            val nextSub = runBlocking {
                repository.getActiveSubscriptions().first().minByOrNull { it.nextBillingTimestamp }
            }
            return NextSubscriptionWidget(nextSub)
        }
}
