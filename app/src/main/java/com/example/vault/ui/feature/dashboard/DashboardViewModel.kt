package com.example.vault.ui.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vault.domain.model.Transaction
import com.example.vault.domain.repository.AccountRepository
import com.example.vault.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DashboardState(
    val totalBalance: Double = 0.0,
    val monthlySpend: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardState> = combine(
        accountRepository.getAccounts(),
        transactionRepository.getRecentTransactions()
    ) { accounts, transactions ->
        val totalBalance = accounts.sumOf { it.balance }
        
        // Simple logic for "Monthly Spend" - just summing negative transactions for now
        // In real app, filter by current month date
        val monthlySpend = transactions
            .filter { it.amount < 0 } // Assuming expense is negative or check Type
            .sumOf { it.amount }

        DashboardState(
            totalBalance = totalBalance,
            monthlySpend = kotlin.math.abs(monthlySpend), // Display as positive number
            recentTransactions = transactions.take(5),
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )
}
