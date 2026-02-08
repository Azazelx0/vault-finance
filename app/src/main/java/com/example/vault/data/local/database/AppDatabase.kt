package com.example.vault.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vault.data.local.dao.AccountDao
import com.example.vault.data.local.dao.BudgetDao
import com.example.vault.data.local.dao.CategoryDao
import com.example.vault.data.local.dao.GoalDao
import com.example.vault.data.local.dao.TransactionDao
import com.example.vault.data.local.entity.AccountEntity
import com.example.vault.data.local.entity.BudgetEntity
import com.example.vault.data.local.entity.CategoryEntity
import com.example.vault.data.local.entity.GoalEntity
import com.example.vault.data.local.entity.TransactionEntity

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        GoalEntity::class,
        BudgetEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun goalDao(): GoalDao
    abstract fun budgetDao(): BudgetDao
}
