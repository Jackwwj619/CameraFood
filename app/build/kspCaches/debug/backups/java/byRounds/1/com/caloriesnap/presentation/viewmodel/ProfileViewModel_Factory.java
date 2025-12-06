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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<PreferencesManager> prefsProvider;

  private final Provider<FoodRepository> repositoryProvider;

  private final Provider<CalorieCalculator> calculatorProvider;

  public ProfileViewModel_Factory(Provider<PreferencesManager> prefsProvider,
      Provider<FoodRepository> repositoryProvider, Provider<CalorieCalculator> calculatorProvider) {
    this.prefsProvider = prefsProvider;
    this.repositoryProvider = repositoryProvider;
    this.calculatorProvider = calculatorProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(prefsProvider.get(), repositoryProvider.get(), calculatorProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<PreferencesManager> prefsProvider,
      Provider<FoodRepository> repositoryProvider, Provider<CalorieCalculator> calculatorProvider) {
    return new ProfileViewModel_Factory(prefsProvider, repositoryProvider, calculatorProvider);
  }

  public static ProfileViewModel newInstance(PreferencesManager prefs, FoodRepository repository,
      CalorieCalculator calculator) {
    return new ProfileViewModel(prefs, repository, calculator);
  }
}
