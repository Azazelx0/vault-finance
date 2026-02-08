package com.example.vault.ui.feature.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vault.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.math.abs

data class AnalyticsState(
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val categoryBreakdown: Map<String, Double> = emptyMap(),
    val isLoading: Boolean = true
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val uiState: StateFlow<AnalyticsState> = transactionRepository.getRecentTransactions()
        .map { transactions ->
            val income = transactions.filter { it.amount > 0 }.sumOf { it.amount }
            val expenses = abs(transactions.filter { it.amount < 0 }.sumOf { it.amount })
            
            // Category breakdown for pie chart (expenses only)
            val breakdown = transactions
                .filter { it.amount < 0 }
                .groupBy { it.categoryName ?: "Other" }
                .mapValues { abs(it.value.sumOf { txn -> txn.amount }) }
            
            AnalyticsState(
                totalIncome = income,
                totalExpenses = expenses,
                categoryBreakdown = breakdown,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AnalyticsState()
        )
}
