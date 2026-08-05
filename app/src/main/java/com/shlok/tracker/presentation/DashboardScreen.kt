package com.shlok.tracker.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import java.util.concurrent.TimeUnit
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.NavController
import com.shlok.tracker.navigation.Screen
import androidx.compose.ui.platform.LocalContext
import com.shlok.tracker.notification.NotificationHelper
import com.shlok.tracker.data.local.Subscription
import com.shlok.tracker.data.local.Transaction
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: TrackerViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Subscription Tracker", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Settings.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = {
                        NotificationHelper(context).showRenewalReminder("Test Subscription", 499.0, "INR")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Test Notification",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header: Total Spend
                item {
                    PredictiveCashflowGraph(
                        subscriptions = uiState.activeSubscriptions,
                        totalSpend = uiState.totalMonthlySpendINR
                    )
                }

                // Section 1: Candidates (If any)
                if (uiState.subscriptionCandidates.isNotEmpty()) {
                    item {
                        Text(
                            text = "Potential Subscriptions Found",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }
                    
                    items(uiState.subscriptionCandidates) { candidate ->
                        CandidateCard(
                            transaction = candidate,
                            onConfirm = { 
                                // In a real app, you might show a dialog here to ask for the exact service name and cycle days
                                viewModel.confirmCandidateAsSubscription(candidate, candidate.senderAddress, 30) 
                            },
                            onDismiss = { viewModel.dismissCandidate(candidate) }
                        )
                    }
                }

                // Section 2: Active Subscriptions
                item {
                    Text(
                        text = "Active Subscriptions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }

                if (uiState.activeSubscriptions.isEmpty()) {
                    item {
                        Text("No active subscriptions being tracked yet.", color = Color.Gray)
                    }
                } else {
                    items(uiState.activeSubscriptions) { subscription ->
                        SubscriptionCard(
                            subscription = subscription,
                            onClick = {
                                navController.navigate(Screen.SubscriptionDetails.createRoute(subscription.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PredictiveCashflowGraph(subscriptions: List<Subscription>, totalSpend: Double) {
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.labelSmall.copy(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    )
    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Total Monthly Spend",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "₹${String.format("%.2f", totalSpend)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val labelPaddingX = 50.dp.toPx()
                val labelPaddingY = 25.dp.toPx()
                val width = size.width - labelPaddingX
                val height = size.height - labelPaddingY
                
                // 1. Corrected Data Processing: 30-day projection
                val days = 30
                val dailySpend = DoubleArray(days + 1)
                val currentTime = System.currentTimeMillis()
                val oneDayMillis = TimeUnit.DAYS.toMillis(1)

                subscriptions.forEach { sub ->
                    // Find which specific day the subscription is due
                    val diff = sub.nextBillingTimestamp - currentTime
                    val dayIndex = (diff / oneDayMillis).toInt()
                    
                    if (dayIndex in 0..days && sub.currency == "INR") {
                        dailySpend[dayIndex] += sub.recurringAmount
                    }
                }

                // Calculate cumulative spend points
                val cumulativePoints = FloatArray(days + 1)
                var runningSum = 0f
                dailySpend.forEachIndexed { index, amount ->
                    runningSum += amount.toFloat()
                    cumulativePoints[index] = runningSum
                }

                // Requirement: Top label must be exactly totalSpend
                val maxSpend = totalSpend.toFloat().takeIf { it > 0 } ?: 100f
                val xSpacing = width / days

                // Draw Y-Axis Labels and Grid Lines (Corrected Math)
                val ySteps = listOf(0f, 0.5f, 1f)
                ySteps.forEach { ratio ->
                    val y = height - (ratio * height)
                    val labelValue = (ratio * maxSpend).toInt()
                    val labelText = "₹$labelValue"
                    
                    drawText(
                        textMeasurer = textMeasurer,
                        text = labelText,
                        style = labelStyle,
                        topLeft = Offset(0f, y - 8.dp.toPx())
                    )

                    drawLine(
                        color = gridColor,
                        start = Offset(labelPaddingX, y),
                        end = Offset(size.width, y),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }

                withTransform({
                    translate(left = labelPaddingX)
                }) {
                    // 2. Build the Path with Bezier Math
                    val strokePath = Path().apply {
                        val startY = height - (cumulativePoints[0] / maxSpend * height).coerceIn(0f, height)
                        moveTo(0f, startY)
                        
                        for (i in 0 until days) {
                            val x1 = i * xSpacing
                            val y1 = height - (cumulativePoints[i] / maxSpend * height).coerceIn(0f, height)
                            val x2 = (i + 1) * xSpacing
                            val y2 = height - (cumulativePoints[i + 1] / maxSpend * height).coerceIn(0f, height)
                            
                            val cx = (x1 + x2) / 2f
                            cubicTo(cx, y1, cx, y2, x2, y2)
                        }
                    }

                    // 3. Gradient Fill Path
                    val fillPath = Path().apply {
                        addPath(strokePath)
                        lineTo(width, height)
                        lineTo(0f, height)
                        close()
                    }

                    // 4. Aesthetics: Draw Gradient Fill
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = height
                        )
                    )

                    // 5. Draw the smooth curve stroke
                    drawPath(
                        path = strokePath,
                        color = primaryColor,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }

                // X-Axis Labels
                val xLabels = listOf("Today" to 0f, "15d" to 0.5f, "30d" to 1f)
                xLabels.forEach { (text, ratio) ->
                    val x = labelPaddingX + (ratio * width)
                    drawText(
                        textMeasurer = textMeasurer,
                        text = text,
                        style = labelStyle,
                        topLeft = Offset(x - 15.dp.toPx(), size.height - 15.dp.toPx())
                    )
                }
            }
        }
    }
}

@Composable
fun TotalSpendCard(total: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Monthly Spend Overview", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "₹${String.format("%.2f", total)}",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun CandidateCard(
    transaction: Transaction,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Detected from: ${transaction.senderAddress}", fontWeight = FontWeight.SemiBold)
                Text("${transaction.currency} ${transaction.amount}", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = transaction.rawSmsBody,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Dismiss")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Dismiss")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onConfirm) {
                    Icon(Icons.Default.Check, contentDescription = "Track")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Track It")
                }
            }
        }
    }
}

@Composable
fun SubscriptionCard(
    subscription: Subscription,
    onClick: () -> Unit
) {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val nextBilling = formatter.format(Date(subscription.nextBillingTimestamp))
    
    val daysLeft = TimeUnit.MILLISECONDS.toDays(
        subscription.nextBillingTimestamp - System.currentTimeMillis()
    ).toInt().coerceAtLeast(0)
    
    val dueText = when (daysLeft) {
        0 -> "Due today"
        1 -> "Due in 1 day"
        else -> "Due in $daysLeft days"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(subscription.serviceName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$dueText ($nextBilling)", 
                    style = MaterialTheme.typography.bodyMedium, 
                    color = if (daysLeft <= 3) MaterialTheme.colorScheme.error else Color.Gray
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${subscription.currency} ${subscription.recurringAmount}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Every ${subscription.billingCycleDays} days",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
