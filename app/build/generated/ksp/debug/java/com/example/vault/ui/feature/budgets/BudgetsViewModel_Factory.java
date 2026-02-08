package com.example.vault.ui.feature.budgets;

import com.example.vault.domain.repository.BudgetRepository;
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
public final class BudgetsViewModel_Factory implements Factory<BudgetsViewModel> {
  private final Provider<BudgetRepository> budgetRepositoryProvider;

  public BudgetsViewModel_Factory(Provider<BudgetRepository> budgetRepositoryProvider) {
    this.budgetRepositoryProvider = budgetRepositoryProvider;
  }

  @Override
  public BudgetsViewModel get() {
    return newInstance(budgetRepositoryProvider.get());
  }

  public static BudgetsViewModel_Factory create(
      Provider<BudgetRepository> budgetRepositoryProvider) {
    return new BudgetsViewModel_Factory(budgetRepositoryProvider);
  }

  public static BudgetsViewModel newInstance(BudgetRepository budgetRepository) {
    return new BudgetsViewModel(budgetRepository);
  }
}
