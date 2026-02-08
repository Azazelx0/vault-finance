package com.example.vault.ui.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vault.domain.model.Transaction
import com.example.vault.data.local.entity.TransactionType
import com.example.vault.ui.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Locale

// Stitch Design Colors - Exact from HTML
private val StitchPrimary = Color(0xFF6764F2)
private val StitchBackground = Color(0xFF0A0A14)
private val StitchBackgroundGradient = Color(0xFF2A2A4A)
private val StitchEmerald = Color(0xFF10B981)
private val StitchRose = Color(0xFFF43F5E)
private val StitchTextMuted = Color(0xFF9D9DB9)
private val StitchGlass = Color.White.copy(alpha = 0.03f)
private val StitchGlassBorder = Color.White.copy(alpha = 0.1f)

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        StitchBackgroundGradient,
                        StitchBackground
                    ),
                    center = androidx.compose.ui.geometry.Offset(0f, 0f)
                )
            )
    ) {
        // Glow overlay at top - exact from Stitch HTML
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            StitchPrimary.copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        radius = 800f
                    )
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddTransaction.route) },
                    containerColor = StitchPrimary,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(top = 32.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    StitchHeader()
                }

                item {
                    StitchBalanceCard(
                        balance = state.totalBalance,
                        growth = 2.4
                    )
                }

                item {
                    StitchIncomeExpenseGrid(
                        income = state.monthlyIncome,
                        expenses = state.monthlySpend
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Activity",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color.White
                        )
                        TextButton(onClick = { navController.navigate(Screen.Transactions.route) }) {
                            Text(
                                text = "View All",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = StitchPrimary
                            )
                        }
                    }
                }

                items(state.recentTransactions.take(4)) { transaction ->
                    StitchTransactionItem(transaction)
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun StitchHeader() {
    Column {
        Text(
            text = "WELCOME BACK",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            ),
            color = StitchTextMuted
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Your Vault",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.White
        )
    }
}

@Composable
fun StitchBalanceCard(balance: Double, growth: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = StitchGlass
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, StitchGlassBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            // Abstract glow inside card - exact from Stitch
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = (-40).dp)
                    .blur(48.dp)
                    .background(StitchPrimary.copy(alpha = 0.2f), CircleShape)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Wealth",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = StitchTextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${String.format("%,.2f", balance)}",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 42.sp,
                        letterSpacing = (-1).sp
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Growth indicator - exact from Stitch
                Surface(
                    shape = RoundedCornerShape(99.dp),
                    color = StitchEmerald.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        StitchEmerald.copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = "Growth",
                            tint = StitchEmerald,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "+${growth}% this month",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = StitchEmerald
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StitchIncomeExpenseGrid(income: Double, expenses: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StitchSummaryCard(
            modifier = Modifier.weight(1f),
            label = "INCOME",
            amount = income,
            color = StitchEmerald,
            icon = Icons.Default.ArrowDownward
        )
        StitchSummaryCard(
            modifier = Modifier.weight(1f),
            label = "EXPENSES",
            amount = expenses,
            color = StitchRose,
            icon = Icons.Default.ArrowUpward
        )
    }
}

@Composable
fun StitchSummaryCard(
    modifier: Modifier = Modifier,
    label: String,
    amount: Double,
    color: Color,
    icon: ImageVector
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = StitchGlass
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, StitchGlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color
                )
            }
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.5.sp
                    ),
                    color = StitchTextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${String.format("%,.2f", amount)}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun StitchTransactionItem(transaction: Transaction) {
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val formattedDate = dateFormat.format(transaction.date)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = StitchGlass
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon box - exact from Stitch
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(StitchGlass, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = transaction.categoryName?.firstOrNull()?.toString() ?: "?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = when (transaction.type) {
                        TransactionType.INCOME -> StitchEmerald
                        TransactionType.EXPENSE -> StitchRose
                        else -> StitchPrimary
                    }
                )
            }

            // Details column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.categoryName ?: "Other",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "${transaction.accountName} • $formattedDate",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    color = StitchTextMuted
                )
            }

            // Amount column
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"}$${
                        String.format(
                            "%,.2f",
                            transaction.amount
                        )
                    }",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = if (transaction.type == TransactionType.INCOME) StitchEmerald else Color.White
                )
                Text(
                    text = "COMPLETED",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.sp
                    ),
                    color = StitchTextMuted
                )
            }
        }
    }
}
