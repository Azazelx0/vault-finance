package com.example.vault.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vault.ui.feature.dashboard.DashboardScreen

@Composable
fun VaultNavGraph() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.AddAccount.route) {
            com.example.vault.ui.feature.accounts.AddAccountScreen(navController = navController)
        }
        composable(Screen.Transactions.route) {
            com.example.vault.ui.feature.transactions.TransactionsScreen(navController = navController)
        }
        composable(Screen.AddTransaction.route) {
            com.example.vault.ui.feature.transactions.AddTransactionScreen(navController = navController)
        }
        composable(Screen.Analytics.route) {
            com.example.vault.ui.feature.analytics.AnalyticsScreen(navController = navController)
        }
        composable(Screen.Budgets.route) {
            com.example.vault.ui.feature.budgets.BudgetsScreen(navController = navController)
        }
        composable(Screen.AddBudget.route) {
            com.example.vault.ui.feature.budgets.AddBudgetScreen(navController = navController)
        }
    }
}
