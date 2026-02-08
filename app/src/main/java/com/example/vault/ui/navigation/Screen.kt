package com.example.vault.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object AddAccount : Screen("add_account")
    object Accounts : Screen("accounts")
    object AddTransaction : Screen("add_transaction")
    object Transactions : Screen("transactions")
    object Analytics : Screen("analytics")
    object Budgets : Screen("budgets")
    object AddBudget : Screen("add_budget")
    object Settings : Screen("settings")
}
