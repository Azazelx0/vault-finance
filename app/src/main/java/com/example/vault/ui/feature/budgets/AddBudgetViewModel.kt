package com.example.vault.ui.feature.budgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vault.data.local.entity.BudgetEntity
import com.example.vault.data.local.entity.BudgetPeriod
import com.example.vault.data.local.entity.CategoryEntity
import com.example.vault.domain.repository.BudgetRepository
import com.example.vault.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class AddBudgetUiState(
    val categories: List<CategoryEntity> = emptyList(),
    val selectedCategoryId: String? = null,
    val limitAmount: String = "",
    val selectedPeriod: BudgetPeriod = BudgetPeriod.MONTHLY,
    val notifyThreshold: Int = 80,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddBudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBudgetUiState(isLoading = true))
    val uiState: StateFlow<AddBudgetUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                _uiState.update { it.copy(categories = categories, isLoading = false) }
            }
        }
    }

    fun onCategorySelected(categoryId: String) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun onLimitAmountChanged(amount: String) {
        _uiState.update { it.copy(limitAmount = amount) }
    }

    fun onPeriodSelected(period: BudgetPeriod) {
        _uiState.update { it.copy(selectedPeriod = period) }
    }

    fun onThresholdChanged(threshold: Int) {
        _uiState.update { it.copy(notifyThreshold = threshold) }
    }

    fun createBudget(onSuccess: () -> Unit) {
        val state = _uiState.value
        
        if (state.selectedCategoryId == null) {
            _uiState.update { it.copy(errorMessage = "Please select a category") }
            return
        }
        
        val limitAmount = state.limitAmount.toDoubleOrNull()
        if (limitAmount == null || limitAmount <= 0) {
            _uiState.update { it.copy(errorMessage = "Please enter a valid amount") }
            return
        }

        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                // Start from beginning of current period
                when (state.selectedPeriod) {
                    BudgetPeriod.MONTHLY -> {
                        calendar.set(Calendar.DAY_OF_MONTH, 1)
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                    }
                    BudgetPeriod.WEEKLY -> {
                        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                    }
                    else -> {
                        // For yearly/custom, start from today
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                    }
                }

                val budget = BudgetEntity(
                    categoryId = state.selectedCategoryId,
                    limitAmount = limitAmount,
                    period = state.selectedPeriod,
                    startDate = calendar.timeInMillis,
                    notifyThresholdPercent = state.notifyThreshold
                )

                budgetRepository.createBudget(budget)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to create budget: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
