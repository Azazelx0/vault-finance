package com.example.vault.ui.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vault.data.local.entity.AccountType
import com.example.vault.data.local.entity.CategoryEntity
import com.example.vault.data.local.entity.CategoryType
import com.example.vault.data.local.entity.TransactionType
import com.example.vault.domain.model.Account
import com.example.vault.domain.repository.AccountRepository
import com.example.vault.domain.repository.CategoryRepository
import com.example.vault.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

import java.util.Date
import javax.inject.Inject

data class AddTransactionState(
    val accounts: List<Account> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    // Filter categories based on selected type
    private val _transactionType = MutableStateFlow(TransactionType.EXPENSE)
    
    val uiState: StateFlow<AddTransactionState> = combine(
        accountRepository.getAccounts(),
        categoryRepository.getCategories(),
        _transactionType
    ) { accounts, allCategories, type ->
        // Filter categories matching the type
        val filteredCategories = allCategories.filter { 
            (type == TransactionType.EXPENSE && it.type == CategoryType.EXPENSE) ||
            (type == TransactionType.INCOME && it.type == CategoryType.INCOME)
        }
        
        AddTransactionState(
            accounts = accounts,
            categories = filteredCategories,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AddTransactionState()
    )

    init {
        seedCategoriesIfNeeded()
        seedAccountsIfNeeded()
    }

    fun setTransactionType(type: TransactionType) {
        _transactionType.value = type
    }

    fun saveTransaction(
        amount: String,
        type: TransactionType,
        accountId: String,
        categoryId: String?,
        date: Long,
        note: String
    ) {
        viewModelScope.launch {
            val amountValue = amount.toDoubleOrNull() ?: return@launch
            // Negate amount for expenses if logic requires it, strictly passing value here, 
            // usually repo or formatting handles sign. Let's store absolute value and use Type to determine sign in UI.
            // However, typical accounting uses negative for expense. Let's store signed value in Repo logic usually, 
            // but for this MVP I'll store signed here.
            
            val finalAmount = if (type == TransactionType.EXPENSE) -kotlin.math.abs(amountValue) else kotlin.math.abs(amountValue)

            transactionRepository.addTransaction(
                amount = finalAmount,
                type = type,
                accountId = accountId,
                categoryId = categoryId,
                date = date,
                note = note
            )
            
            // Also need to update Account Balance
            // This transaction logic should ideally be in a UseCase to be atomic
            val account = accountRepository.getAccount(accountId)
            if (account != null) {
                accountRepository.updateAccountBalance(accountId, account.balance + finalAmount)
            }
        }
    }
    
    // Seeding Logic - Seeds default categories on first run
    private fun seedCategoriesIfNeeded() {
        viewModelScope.launch {
            // Seed default categories - IGNORE strategy will prevent duplicates
            val defaults = listOf(
                CategoryEntity(name = "Food", type = CategoryType.EXPENSE, colorHex = "#F59E0B", iconName = "restaurant", isSystemDefault = true),
                CategoryEntity(name = "Transport", type = CategoryType.EXPENSE, colorHex = "#3B82F6", iconName = "directions_car", isSystemDefault = true),
                CategoryEntity(name = "Housing", type = CategoryType.EXPENSE, colorHex = "#EF4444", iconName = "home", isSystemDefault = true),
                CategoryEntity(name = "Shopping", type = CategoryType.EXPENSE, colorHex = "#8B5CF6", iconName = "shopping_bag", isSystemDefault = true),
                CategoryEntity(name = "Entertainment", type = CategoryType.EXPENSE, colorHex = "#EC4899", iconName = "movie", isSystemDefault = true),
                CategoryEntity(name = "Salary", type = CategoryType.INCOME, colorHex = "#10B981", iconName = "attach_money", isSystemDefault = true),
                CategoryEntity(name = "Freelance", type = CategoryType.INCOME, colorHex = "#06B6D4", iconName = "work", isSystemDefault = true),
                CategoryEntity(name = "Investment", type = CategoryType.INCOME, colorHex = "#14B8A6", iconName = "trending_up", isSystemDefault = true)
            )
            defaults.forEach { categoryRepository.addCategory(it) }
        }
    }
    

    // Seed default account if none exists
    private fun seedAccountsIfNeeded() {
        viewModelScope.launch {
            try {
                // Collect first emission to check if empty logic
                val currentAccounts = accountRepository.getAccounts()
                
                // We can't block. Let's just launch a separate collector
                launch {
                    currentAccounts.take(1).collect { accounts ->
                        if (accounts.isEmpty()) {
                            accountRepository.createAccount(
                                name = "Cash",
                                type = "CASH", 
                                initialBalance = 0.0,
                                color = "#10B981", // Emerald
                                icon = "wallet"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
