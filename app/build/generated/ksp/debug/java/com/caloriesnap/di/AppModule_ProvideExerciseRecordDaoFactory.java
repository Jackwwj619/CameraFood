package com.caloriesnap.di;

import com.caloriesnap.data.local.AppDatabase;
import com.caloriesnap.data.local.dao.ExerciseRecordDao;
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
public final class AppModule_ProvideExerciseRecordDaoFactory implements Factory<ExerciseRecordDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideExerciseRecordDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ExerciseRecordDao get() {
    return provideExerciseRecordDao(dbProvider.get());
  }

  public static AppModule_ProvideExerciseRecordDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideExerciseRecordDaoFactory(dbProvider);
  }

  public static ExerciseRecordDao provideExerciseRecordDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideExerciseRecordDao(db));
  }
}
