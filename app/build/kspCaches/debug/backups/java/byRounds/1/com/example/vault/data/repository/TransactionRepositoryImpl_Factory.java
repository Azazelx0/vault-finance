package com.example.vault.data.repository;

import com.example.vault.data.local.dao.AccountDao;
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
public final class TransactionRepositoryImpl_Factory implements Factory<TransactionRepositoryImpl> {
  private final Provider<TransactionDao> transactionDaoProvider;

  private final Provider<AccountDao> accountDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  public TransactionRepositoryImpl_Factory(Provider<TransactionDao> transactionDaoProvider,
      Provider<AccountDao> accountDaoProvider, Provider<CategoryDao> categoryDaoProvider) {
    this.transactionDaoProvider = transactionDaoProvider;
    this.accountDaoProvider = accountDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
  }

  @Override
  public TransactionRepositoryImpl get() {
    return newInstance(transactionDaoProvider.get(), accountDaoProvider.get(), categoryDaoProvider.get());
  }

  public static TransactionRepositoryImpl_Factory create(
      Provider<TransactionDao> transactionDaoProvider, Provider<AccountDao> accountDaoProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    return new TransactionRepositoryImpl_Factory(transactionDaoProvider, accountDaoProvider, categoryDaoProvider);
  }

  public static TransactionRepositoryImpl newInstance(TransactionDao transactionDao,
      AccountDao accountDao, CategoryDao categoryDao) {
    return new TransactionRepositoryImpl(transactionDao, accountDao, categoryDao);
  }
}
