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
    val monthlyIncome: Double = 0.0,
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
        
        // Calculate monthly income and expenses
        val monthlyIncome = transactions
            .filter { it.amount > 0 }
            .sumOf { it.amount }
            
        val monthlySpend = transactions
            .filter { it.amount < 0 }
            .sumOf { it.amount }

        DashboardState(
            totalBalance = totalBalance,
            monthlyIncome = monthlyIncome,
            monthlySpend = kotlin.math.abs(monthlySpend),
            recentTransactions = transactions.take(5),
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )
}
