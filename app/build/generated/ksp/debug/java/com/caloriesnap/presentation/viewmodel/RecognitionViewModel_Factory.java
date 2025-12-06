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
public final class RecognitionViewModel_Factory implements Factory<RecognitionViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<AiService> aiServiceProvider;

  private final Provider<FoodRepository> repositoryProvider;

  private final Provider<PreferencesManager> prefsProvider;

  public RecognitionViewModel_Factory(Provider<Context> contextProvider,
      Provider<AiService> aiServiceProvider, Provider<FoodRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider) {
    this.contextProvider = contextProvider;
    this.aiServiceProvider = aiServiceProvider;
    this.repositoryProvider = repositoryProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public RecognitionViewModel get() {
    return newInstance(contextProvider.get(), aiServiceProvider.get(), repositoryProvider.get(), prefsProvider.get());
  }

  public static RecognitionViewModel_Factory create(Provider<Context> contextProvider,
      Provider<AiService> aiServiceProvider, Provider<FoodRepository> repositoryProvider,
      Provider<PreferencesManager> prefsProvider) {
    return new RecognitionViewModel_Factory(contextProvider, aiServiceProvider, repositoryProvider, prefsProvider);
  }

  public static RecognitionViewModel newInstance(Context context, AiService aiService,
      FoodRepository repository, PreferencesManager prefs) {
    return new RecognitionViewModel(context, aiService, repository, prefs);
  }
}
