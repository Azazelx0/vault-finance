package com.example.vault.data.repository

import com.example.vault.data.local.dao.AccountDao
import com.example.vault.data.local.dao.CategoryDao
import com.example.vault.data.local.dao.TransactionDao
import com.example.vault.data.local.entity.TransactionEntity
import com.example.vault.data.local.entity.TransactionType
import com.example.vault.domain.model.Transaction
import com.example.vault.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Date
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao
) : TransactionRepository {

    // Composing data from multiple generic flows to create the UI model
    // In a real app, might use a Relation POJO in Room or a complex query
    override fun getRecentTransactions(): Flow<List<Transaction>> {
        return combine(
            transactionDao.getAllTransactions(),
            accountDao.getAllAccounts(),
            categoryDao.getAllCategories()
        ) { transactions, accounts, categories ->
            transactions.map { txn ->
                val account = accounts.find { it.id == txn.accountId }
                val category = categories.find { it.id == txn.categoryId }
                
                Transaction(
                    id = txn.id,
                    amount = txn.amount,
                    type = txn.type,
                    accountId = txn.accountId,
                    accountName = account?.name ?: "Unknown Account",
                    categoryId = txn.categoryId,
                    categoryName = category?.name ?: "Uncategorized",
                    categoryColor = category?.colorHex,
                    categoryIcon = category?.iconName,
                    currencyCode = account?.currencyCode ?: "USD",
                    date = Date(txn.date),
                    note = txn.note
                )
            }
        }
    }

    override fun getTransactionsByAccount(accountId: String): Flow<List<Transaction>> {
        // Similar mapping logic, filtered by account
        return combine(
            transactionDao.getTransactionsForAccount(accountId),
            accountDao.getAllAccounts(),
            categoryDao.getAllCategories()
        ) { transactions, accounts, categories ->
            transactions.map { txn ->
                val account = accounts.find { it.id == txn.accountId }
                val category = categories.find { it.id == txn.categoryId }
                
                Transaction(
                    id = txn.id,
                    amount = txn.amount,
                    type = txn.type,
                    accountId = txn.accountId,
                    accountName = account?.name ?: "Unknown Account",
                    categoryId = txn.categoryId,
                    categoryName = category?.name ?: "Uncategorized",
                    categoryColor = category?.colorHex,
                    categoryIcon = category?.iconName,
                    currencyCode = account?.currencyCode ?: "USD",
                    date = Date(txn.date),
                    note = txn.note
                )
            }
        }
    }

    override suspend fun addTransaction(
        amount: Double,
        type: TransactionType,
        accountId: String,
        categoryId: String?,
        date: Long,
        note: String?
    ) {
        val entity = TransactionEntity(
            amount = amount,
            type = type,
            accountId = accountId,
            categoryId = categoryId,
            date = date,
            note = note
        )
        transactionDao.insertTransaction(entity)
    }
}
