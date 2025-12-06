package com.caloriesnap;

import com.caloriesnap.data.local.PreferencesManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<PreferencesManager> prefsProvider;

  public MainActivity_MembersInjector(Provider<PreferencesManager> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  public static MembersInjector<MainActivity> create(Provider<PreferencesManager> prefsProvider) {
    return new MainActivity_MembersInjector(prefsProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPrefs(instance, prefsProvider.get());
  }

  @InjectedFieldSignature("com.caloriesnap.MainActivity.prefs")
  public static void injectPrefs(MainActivity instance, PreferencesManager prefs) {
    instance.prefs = prefs;
  }
}
