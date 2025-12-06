package com.caloriesnap.di;

import com.caloriesnap.data.local.AppDatabase;
import com.caloriesnap.data.local.dao.WeightRecordDao;
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
public final class AppModule_ProvideWeightRecordDaoFactory implements Factory<WeightRecordDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideWeightRecordDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public WeightRecordDao get() {
    return provideWeightRecordDao(dbProvider.get());
  }

  public static AppModule_ProvideWeightRecordDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideWeightRecordDaoFactory(dbProvider);
  }

  public static WeightRecordDao provideWeightRecordDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideWeightRecordDao(db));
  }
}
