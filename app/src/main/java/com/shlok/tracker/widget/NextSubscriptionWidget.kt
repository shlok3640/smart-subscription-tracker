package com.shlok.tracker.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.shlok.tracker.data.local.Subscription
import java.util.concurrent.TimeUnit

class NextSubscriptionWidget(
    private val nextSub: Subscription? = null
) : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent(nextSub)
        }
    }

    @Composable
    private fun WidgetContent(sub: Subscription?) {
        val backgroundColor = Color(0xFF1A1C1E) // Premium Dark Blue-Gray
        val primaryText = Color.White
        val secondaryText = Color(0xFFC4C7C5) // Light Gray

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(backgroundColor)
                .cornerRadius(16.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (sub != null) {
                val daysLeft = TimeUnit.MILLISECONDS.toDays(
                    sub.nextBillingTimestamp - System.currentTimeMillis()
                ).coerceAtLeast(0)

                Text(
                    text = sub.serviceName,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ColorProvider(primaryText)
                    )
                )
                Spacer(modifier = GlanceModifier.height(8.dp))
                Text(
                    text = "${sub.currency} ${sub.recurringAmount}",
                    style = TextStyle(
                        fontSize = 16.sp, 
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(primaryText)
                    )
                )
                Spacer(modifier = GlanceModifier.height(8.dp))
                Text(
                    text = "Due in $daysLeft days",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = ColorProvider(secondaryText)
                    )
                )
            } else {
                Text(
                    text = "No upcoming subs", 
                    style = TextStyle(color = ColorProvider(primaryText))
                )
            }
        }
    }
}
