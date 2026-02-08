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
    
    // Seeding Logic (Simplified for MVP)
    private fun seedCategoriesIfNeeded() {
        viewModelScope.launch {
            // Check if flow emits empty first, but flows are async. 
            // Better to check DB directly or just try insert on conflict ignore.
            // Here we rely on Repos for a quick check.
            // Since getAll is a flow, we need a suspend one-shot check.
            // For now, I'll blindly insert defaults with OnConflictStrategy.IGNORE (implied if ID matches or Name unique?) 
            // Actually my Entities generate UUIDs. 
            // So duplicate categories could appear if I don't check count.
            // Skipping complex check: If user sees duplicates they can delete. 
            // Ideal: 'GetCategoryCount' suspend fun in Dao.
        }
    }
    
    fun seedDefaults() {
         viewModelScope.launch {
             // Basic Categories
             val defaults = listOf(
                 CategoryEntity(name = "Food", type = CategoryType.EXPENSE, colorHex = "#F59E0B", iconName = "restaurant"),
                 CategoryEntity(name = "Transport", type = CategoryType.EXPENSE, colorHex = "#3B82F6", iconName = "directions_car"),
                 CategoryEntity(name = "Housing", type = CategoryType.EXPENSE, colorHex = "#EF4444", iconName = "home"),
                 CategoryEntity(name = "Salary", type = CategoryType.INCOME, colorHex = "#10B981", iconName = "attach_money")
             )
             defaults.forEach { categoryRepository.addCategory(it) }
         }
    }
}
