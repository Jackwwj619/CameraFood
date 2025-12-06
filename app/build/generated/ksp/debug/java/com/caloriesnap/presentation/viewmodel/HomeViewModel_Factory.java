package com.caloriesnap.presentation.viewmodel;

import com.caloriesnap.data.local.PreferencesManager;
import com.caloriesnap.data.repository.FoodRepository;
import com.caloriesnap.domain.usecase.CalorieCalculator;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<FoodRepository> repositoryProvider;

  private final Provider<PreferencesManager> prefsProvider;

  private final Provider<CalorieCalculator> calculatorProvider;

  public HomeViewModel_Factory(Provider<FoodRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider, Provider<CalorieCalculator> calculatorProvider) {
    this.repositoryProvider = repositoryProvider;
    this.prefsProvider = prefsProvider;
    this.calculatorProvider = calculatorProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(repositoryProvider.get(), prefsProvider.get(), calculatorProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<FoodRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider, Provider<CalorieCalculator> calculatorProvider) {
    return new HomeViewModel_Factory(repositoryProvider, prefsProvider, calculatorProvider);
  }

  public static HomeViewModel newInstance(FoodRepository repository, PreferencesManager prefs,
      CalorieCalculator calculator) {
    return new HomeViewModel(repository, prefs, calculator);
  }
}
