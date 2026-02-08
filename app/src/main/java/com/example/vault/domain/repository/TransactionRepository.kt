package com.example.vault.domain.repository

import com.example.vault.data.local.entity.TransactionType
import com.example.vault.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TransactionRepository {
    fun getRecentTransactions(): Flow<List<Transaction>>
    fun getTransactionsByAccount(accountId: String): Flow<List<Transaction>>
    suspend fun addTransaction(amount: Double, type: TransactionType, accountId: String, categoryId: String?, date: Long, note: String?)
}
