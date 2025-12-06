package com.caloriesnap.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CalorieCalculator_Factory implements Factory<CalorieCalculator> {
  @Override
  public CalorieCalculator get() {
    return newInstance();
  }

  public static CalorieCalculator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CalorieCalculator newInstance() {
    return new CalorieCalculator();
  }

  private static final class InstanceHolder {
    private static final CalorieCalculator_Factory INSTANCE = new CalorieCalculator_Factory();
  }
}
