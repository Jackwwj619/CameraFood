package com.caloriesnap.presentation.viewmodel;

import com.caloriesnap.data.local.PreferencesManager;
import com.caloriesnap.data.remote.AiService;
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
public final class StatisticsViewModel_Factory implements Factory<StatisticsViewModel> {
  private final Provider<FoodRepository> repositoryProvider;

  private final Provider<PreferencesManager> prefsProvider;

  private final Provider<CalorieCalculator> calculatorProvider;

  private final Provider<AiService> aiServiceProvider;

  public StatisticsViewModel_Factory(Provider<FoodRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider, Provider<CalorieCalculator> calculatorProvider,
      Provider<AiService> aiServiceProvider) {
    this.repositoryProvider = repositoryProvider;
    this.prefsProvider = prefsProvider;
    this.calculatorProvider = calculatorProvider;
    this.aiServiceProvider = aiServiceProvider;
  }

  @Override
  public StatisticsViewModel get() {
    return newInstance(repositoryProvider.get(), prefsProvider.get(), calculatorProvider.get(), aiServiceProvider.get());
  }

  public static StatisticsViewModel_Factory create(Provider<FoodRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider, Provider<CalorieCalculator> calculatorProvider,
      Provider<AiService> aiServiceProvider) {
    return new StatisticsViewModel_Factory(repositoryProvider, prefsProvider, calculatorProvider, aiServiceProvider);
  }

  public static StatisticsViewModel newInstance(FoodRepository repository, PreferencesManager prefs,
      CalorieCalculator calculator, AiService aiService) {
    return new StatisticsViewModel(repository, prefs, calculator, aiService);
  }
}
