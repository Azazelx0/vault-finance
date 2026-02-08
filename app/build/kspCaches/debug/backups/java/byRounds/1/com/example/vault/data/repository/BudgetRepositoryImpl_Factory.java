package com.example.vault.data.repository;

import com.example.vault.data.local.dao.BudgetDao;
import com.example.vault.data.local.dao.CategoryDao;
import com.example.vault.data.local.dao.TransactionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class BudgetRepositoryImpl_Factory implements Factory<BudgetRepositoryImpl> {
  private final Provider<BudgetDao> budgetDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<TransactionDao> transactionDaoProvider;

  public BudgetRepositoryImpl_Factory(Provider<BudgetDao> budgetDaoProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<TransactionDao> transactionDaoProvider) {
    this.budgetDaoProvider = budgetDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.transactionDaoProvider = transactionDaoProvider;
  }

  @Override
  public BudgetRepositoryImpl get() {
    return newInstance(budgetDaoProvider.get(), categoryDaoProvider.get(), transactionDaoProvider.get());
  }

  public static BudgetRepositoryImpl_Factory create(Provider<BudgetDao> budgetDaoProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<TransactionDao> transactionDaoProvider) {
    return new BudgetRepositoryImpl_Factory(budgetDaoProvider, categoryDaoProvider, transactionDaoProvider);
  }

  public static BudgetRepositoryImpl newInstance(BudgetDao budgetDao, CategoryDao categoryDao,
      TransactionDao transactionDao) {
    return new BudgetRepositoryImpl(budgetDao, categoryDao, transactionDao);
  }
}
