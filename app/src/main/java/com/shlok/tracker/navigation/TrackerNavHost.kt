package com.shlok.tracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.shlok.tracker.presentation.DashboardScreen
import com.shlok.tracker.presentation.SettingsScreen
import com.shlok.tracker.presentation.SubscriptionDetailsScreen
import com.shlok.tracker.presentation.TrackerViewModel

@Composable
fun TrackerNavHost(
    navController: NavHostController,
    viewModel: TrackerViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.SubscriptionDetails.route,
            arguments = listOf(navArgument("subscriptionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val subscriptionId = backStackEntry.arguments?.getLong("subscriptionId") ?: 0L
            SubscriptionDetailsScreen(
                subscriptionId = subscriptionId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
