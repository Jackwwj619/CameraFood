package com.caloriesnap.data.remote;

import com.caloriesnap.data.local.PreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AiService_Factory implements Factory<AiService> {
  private final Provider<PreferencesManager> prefsProvider;

  public AiService_Factory(Provider<PreferencesManager> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public AiService get() {
    return newInstance(prefsProvider.get());
  }

  public static AiService_Factory create(Provider<PreferencesManager> prefsProvider) {
    return new AiService_Factory(prefsProvider);
  }

  public static AiService newInstance(PreferencesManager prefs) {
    return new AiService(prefs);
  }
}
