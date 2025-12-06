package com.caloriesnap.di;

import com.caloriesnap.data.local.AppDatabase;
import com.caloriesnap.data.local.dao.FoodDao;
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
public final class AppModule_ProvideFoodDaoFactory implements Factory<FoodDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideFoodDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FoodDao get() {
    return provideFoodDao(dbProvider.get());
  }

  public static AppModule_ProvideFoodDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideFoodDaoFactory(dbProvider);
  }

  public static FoodDao provideFoodDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFoodDao(db));
  }
}
