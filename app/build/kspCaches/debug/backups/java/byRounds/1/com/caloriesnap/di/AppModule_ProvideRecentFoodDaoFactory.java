package com.caloriesnap.di;

import com.caloriesnap.data.local.AppDatabase;
import com.caloriesnap.data.local.dao.RecentFoodDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideRecentFoodDaoFactory implements Factory<RecentFoodDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideRecentFoodDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RecentFoodDao get() {
    return provideRecentFoodDao(dbProvider.get());
  }

  public static AppModule_ProvideRecentFoodDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideRecentFoodDaoFactory(dbProvider);
  }

  public static RecentFoodDao provideRecentFoodDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRecentFoodDao(db));
  }
}
