package com.caloriesnap.presentation.viewmodel;

import com.caloriesnap.data.local.dao.ExerciseRecordDao;
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
public final class ExerciseViewModel_Factory implements Factory<ExerciseViewModel> {
  private final Provider<ExerciseRecordDao> exerciseRecordDaoProvider;

  public ExerciseViewModel_Factory(Provider<ExerciseRecordDao> exerciseRecordDaoProvider) {
    this.exerciseRecordDaoProvider = exerciseRecordDaoProvider;
  }

  @Override
  public ExerciseViewModel get() {
    return newInstance(exerciseRecordDaoProvider.get());
  }

  public static ExerciseViewModel_Factory create(
      Provider<ExerciseRecordDao> exerciseRecordDaoProvider) {
    return new ExerciseViewModel_Factory(exerciseRecordDaoProvider);
  }

  public static ExerciseViewModel newInstance(ExerciseRecordDao exerciseRecordDao) {
    return new ExerciseViewModel(exerciseRecordDao);
  }
}
