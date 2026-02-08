package com.example.vault

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vault.ui.navigation.Screen
import com.example.vault.ui.navigation.VaultNavGraph

// Colors
private val StitchDarkBackground = Color(0xFF0F172A)
private val StitchViolet = Color(0xFF8B5CF6)
private val StitchTextMuted = Color(0xFF94A3B8)

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : BottomNavItem(Screen.Dashboard.route, "Home", Icons.Default.Home)
    object Accounts : BottomNavItem(Screen.Accounts.route, "Accounts", Icons.Default.AccountBalanceWallet)
    object Transactions : BottomNavItem(Screen.Transactions.route, "History", Icons.Default.Receipt)
    object Budgets : BottomNavItem(Screen.Budgets.route, "Budgets", Icons.Default.PieChart)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Accounts,
        BottomNavItem.Transactions,
        BottomNavItem.Budgets
    )
    
    // Only show bottom bar on top-level screens
    val showBottomBar = items.any { it.route == currentDestination?.route }

    Scaffold(
        containerColor = StitchDarkBackground,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = StitchDarkBackground, // Or slightly lighter?
                    contentColor = Color.White
                ) {
                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = selected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = StitchViolet,
                                unselectedIconColor = StitchTextMuted,
                                unselectedTextColor = StitchTextMuted
                            ),
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        VaultNavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
