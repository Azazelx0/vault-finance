package com.example.vault.ui.feature.analytics;

import com.example.vault.domain.repository.TransactionRepository;
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
public final class AnalyticsViewModel_Factory implements Factory<AnalyticsViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public AnalyticsViewModel_Factory(Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public AnalyticsViewModel get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static AnalyticsViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new AnalyticsViewModel_Factory(transactionRepositoryProvider);
  }

  public static AnalyticsViewModel newInstance(TransactionRepository transactionRepository) {
    return new AnalyticsViewModel(transactionRepository);
  }
}
