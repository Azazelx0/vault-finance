package com.example.vault.domain.repository

import com.example.vault.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<CategoryEntity>>
    fun getIncomeCategories(): Flow<List<CategoryEntity>>
    fun getExpenseCategories(): Flow<List<CategoryEntity>>
    suspend fun addCategory(category: CategoryEntity)
}
