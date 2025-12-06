package com.caloriesnap.di;

import com.caloriesnap.data.local.AppDatabase;
import com.caloriesnap.data.local.dao.FoodRecordDao;
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
public final class AppModule_ProvideFoodRecordDaoFactory implements Factory<FoodRecordDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideFoodRecordDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FoodRecordDao get() {
    return provideFoodRecordDao(dbProvider.get());
  }

  public static AppModule_ProvideFoodRecordDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideFoodRecordDaoFactory(dbProvider);
  }

  public static FoodRecordDao provideFoodRecordDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFoodRecordDao(db));
  }
}
