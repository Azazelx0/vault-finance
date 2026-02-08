package com.example.vault.domain.repository

import com.example.vault.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccounts(): Flow<List<Account>>
    suspend fun getAccount(id: String): Account?
    suspend fun createAccount(name: String, type: String, initialBalance: Double, color: String, icon: String)
    suspend fun updateAccountBalance(id: String, newBalance: Double)
}
