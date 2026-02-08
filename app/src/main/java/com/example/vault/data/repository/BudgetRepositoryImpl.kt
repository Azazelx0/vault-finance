package com.example.vault.data.repository

import com.example.vault.data.local.dao.BudgetDao
import com.example.vault.data.local.dao.CategoryDao
import com.example.vault.data.local.dao.TransactionDao
import com.example.vault.data.local.entity.BudgetEntity
import com.example.vault.data.local.entity.BudgetPeriod
import com.example.vault.data.local.entity.TransactionType
import com.example.vault.domain.model.Budget
import com.example.vault.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) : BudgetRepository {

    override fun getAllBudgets(): Flow<List<Budget>> {
        return combine(
            budgetDao.getAllBudgets(),
            categoryDao.getAllCategories(),
            transactionDao.getAllTransactions()
        ) { budgets, categories, transactions ->
            budgets.map { budgetEntity ->
                val category = categories.find { it.id == budgetEntity.categoryId }
                val spentAmount = calculateSpentAmount(
                    budgetEntity.categoryId,
                    budgetEntity.startDate,
                    budgetEntity.period,
                    transactions
                )
                
                Budget(
                    id = budgetEntity.id,
                    categoryId = budgetEntity.categoryId,
                    categoryName = category?.name ?: "Unknown",
                    limitAmount = budgetEntity.limitAmount,
                    spentAmount = spentAmount,
                    period = budgetEntity.period,
                    startDate = Date(budgetEntity.startDate),
                    endDate = budgetEntity.endDate?.let { Date(it) },
                    notifyThresholdPercent = budgetEntity.notifyThresholdPercent
                )
            }
        }
    }

    override fun getBudgetById(id: String): Flow<Budget?> {
        return combine(
            budgetDao.getAllBudgets(),
            categoryDao.getAllCategories(),
            transactionDao.getAllTransactions()
        ) { budgets, categories, transactions ->
            budgets.find { it.id == id }?.let { budgetEntity ->
                val category = categories.find { it.id == budgetEntity.categoryId }
                val spentAmount = calculateSpentAmount(
                    budgetEntity.categoryId,
                    budgetEntity.startDate,
                    budgetEntity.period,
                    transactions
                )
                
                Budget(
                    id = budgetEntity.id,
                    categoryId = budgetEntity.categoryId,
                    categoryName = category?.name ?: "Unknown",
                    limitAmount = budgetEntity.limitAmount,
                    spentAmount = spentAmount,
                    period = budgetEntity.period,
                    startDate = Date(budgetEntity.startDate),
                    endDate = budgetEntity.endDate?.let { Date(it) },
                    notifyThresholdPercent = budgetEntity.notifyThresholdPercent
                )
            }
        }
    }

    override fun getBudgetForCategory(categoryId: String): Flow<Budget?> {
        return combine(
            budgetDao.getAllBudgets(),
            categoryDao.getAllCategories(),
            transactionDao.getAllTransactions()
        ) { budgets, categories, transactions ->
            budgets.find { it.categoryId == categoryId }?.let { budgetEntity ->
                val category = categories.find { it.id == budgetEntity.categoryId }
                val spentAmount = calculateSpentAmount(
                    budgetEntity.categoryId,
                    budgetEntity.startDate,
                    budgetEntity.period,
                    transactions
                )
                
                Budget(
                    id = budgetEntity.id,
                    categoryId = budgetEntity.categoryId,
                    categoryName = category?.name ?: "Unknown",
                    limitAmount = budgetEntity.limitAmount,
                    spentAmount = spentAmount,
                    period = budgetEntity.period,
                    startDate = Date(budgetEntity.startDate),
                    endDate = budgetEntity.endDate?.let { Date(it) },
                    notifyThresholdPercent = budgetEntity.notifyThresholdPercent
                )
            }
        }
    }

    override suspend fun createBudget(budget: BudgetEntity) {
        budgetDao.insertBudget(budget)
    }

    override suspend fun updateBudget(budget: BudgetEntity) {
        budgetDao.updateBudget(budget)
    }

    override suspend fun deleteBudget(id: String) {
        // Note: This is not ideal - we need a delete by ID method in the DAO
        // For now, this is a placeholder
    }

    private fun calculateSpentAmount(
        categoryId: String,
        startDate: Long,
        period: BudgetPeriod,
        transactions: List<com.example.vault.data.local.entity.TransactionEntity>
    ): Double {
        val periodStart = Date(startDate)
        val periodEnd = calculatePeriodEnd(periodStart, period)
        
        return transactions
            .filter { it.categoryId == categoryId && it.type == TransactionType.EXPENSE }
            .filter { Date(it.date).after(periodStart) && Date(it.date).before(periodEnd) }
            .sumOf { it.amount }
    }

    private fun calculatePeriodEnd(startDate: Date, period: BudgetPeriod): Date {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        
        when (period) {
            BudgetPeriod.WEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
            BudgetPeriod.MONTHLY -> calendar.add(Calendar.MONTH, 1)
            BudgetPeriod.YEARLY -> calendar.add(Calendar.YEAR, 1)
            BudgetPeriod.CUSTOM -> {
                // For custom, use endDate from entity or default to 1 month
                calendar.add(Calendar.MONTH, 1)
            }
        }
        
        return calendar.time
    }
}
