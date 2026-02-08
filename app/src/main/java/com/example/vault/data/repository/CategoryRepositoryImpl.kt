package com.example.vault.data.repository

import com.example.vault.data.local.dao.CategoryDao
import com.example.vault.data.local.entity.CategoryEntity
import com.example.vault.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository {
    override fun getCategories(): Flow<List<CategoryEntity>> = dao.getAllCategories()
    override fun getIncomeCategories(): Flow<List<CategoryEntity>> = dao.getIncomeCategories()
    override fun getExpenseCategories(): Flow<List<CategoryEntity>> = dao.getExpenseCategories()
    override suspend fun addCategory(category: CategoryEntity) = dao.insertCategory(category)
}
