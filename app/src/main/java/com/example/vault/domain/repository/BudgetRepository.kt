package com.example.vault.domain.repository

import com.example.vault.data.local.entity.BudgetEntity
import com.example.vault.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAllBudgets(): Flow<List<Budget>>
    fun getBudgetById(id: String): Flow<Budget?>
    fun getBudgetForCategory(categoryId: String): Flow<Budget?>
    suspend fun createBudget(budget: BudgetEntity)
    suspend fun updateBudget(budget: BudgetEntity)
    suspend fun deleteBudget(id: String)
}
