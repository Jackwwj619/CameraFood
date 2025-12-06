package com.caloriesnap.presentation.viewmodel;

import com.caloriesnap.data.repository.FoodRepository;
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
public final class AddFoodViewModel_Factory implements Factory<AddFoodViewModel> {
  private final Provider<FoodRepository> repositoryProvider;

  public AddFoodViewModel_Factory(Provider<FoodRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddFoodViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddFoodViewModel_Factory create(Provider<FoodRepository> repositoryProvider) {
    return new AddFoodViewModel_Factory(repositoryProvider);
  }

  public static AddFoodViewModel newInstance(FoodRepository repository) {
    return new AddFoodViewModel(repository);
  }
}
