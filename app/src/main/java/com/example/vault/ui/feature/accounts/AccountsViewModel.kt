package com.example.vault.ui.feature.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vault.domain.model.Account
import com.example.vault.domain.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    val accounts: StateFlow<List<Account>> = accountRepository.getAccounts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun createAccount(name: String, type: String, initialBalance: String, color: String, currencyCode: String) {
        viewModelScope.launch {
            val balance = initialBalance.toDoubleOrNull() ?: 0.0
            accountRepository.createAccount(
                name = name,
                type = type,
                initialBalance = balance,
                color = color,
                icon = "wallet", // Default icon for now
                currencyCode = currencyCode
            )
        }
    }
}
