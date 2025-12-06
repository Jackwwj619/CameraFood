package com.caloriesnap.presentation.viewmodel;

import android.content.Context;
import com.caloriesnap.data.local.PreferencesManager;
import com.caloriesnap.data.remote.AiService;
import com.caloriesnap.data.repository.FoodRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<PreferencesManager> prefsProvider;

  private final Provider<AiService> aiServiceProvider;

  private final Provider<FoodRepository> repositoryProvider;

  public SettingsViewModel_Factory(Provider<Context> contextProvider,
      Provider<PreferencesManager> prefsProvider, Provider<AiService> aiServiceProvider,
      Provider<FoodRepository> repositoryProvider) {
    this.contextProvider = contextProvider;
    this.prefsProvider = prefsProvider;
    this.aiServiceProvider = aiServiceProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(contextProvider.get(), prefsProvider.get(), aiServiceProvider.get(), repositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<PreferencesManager> prefsProvider, Provider<AiService> aiServiceProvider,
      Provider<FoodRepository> repositoryProvider) {
    return new SettingsViewModel_Factory(contextProvider, prefsProvider, aiServiceProvider, repositoryProvider);
  }

  public static SettingsViewModel newInstance(Context context, PreferencesManager prefs,
      AiService aiService, FoodRepository repository) {
    return new SettingsViewModel(context, prefs, aiService, repository);
  }
}
