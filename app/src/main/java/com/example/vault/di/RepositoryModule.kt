package com.example.vault.di

import com.example.vault.data.repository.AccountRepositoryImpl
import com.example.vault.data.repository.BudgetRepositoryImpl
import com.example.vault.data.repository.CategoryRepositoryImpl
import com.example.vault.data.repository.TransactionRepositoryImpl
import com.example.vault.domain.repository.AccountRepository
import com.example.vault.domain.repository.BudgetRepository
import com.example.vault.domain.repository.CategoryRepository
import com.example.vault.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
    
    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryImpl: BudgetRepositoryImpl
    ): BudgetRepository
}
