package com.caloriesnap.data.repository;

import com.caloriesnap.data.local.dao.FoodDao;
import com.caloriesnap.data.local.dao.FoodRecordDao;
import com.caloriesnap.data.local.dao.RecentFoodDao;
import com.caloriesnap.data.local.dao.WeightRecordDao;
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
public final class FoodRepository_Factory implements Factory<FoodRepository> {
  private final Provider<FoodRecordDao> foodRecordDaoProvider;

  private final Provider<WeightRecordDao> weightRecordDaoProvider;

  private final Provider<FoodDao> foodDaoProvider;

  private final Provider<RecentFoodDao> recentFoodDaoProvider;

  public FoodRepository_Factory(Provider<FoodRecordDao> foodRecordDaoProvider,
      Provider<WeightRecordDao> weightRecordDaoProvider, Provider<FoodDao> foodDaoProvider,
      Provider<RecentFoodDao> recentFoodDaoProvider) {
    this.foodRecordDaoProvider = foodRecordDaoProvider;
    this.weightRecordDaoProvider = weightRecordDaoProvider;
    this.foodDaoProvider = foodDaoProvider;
    this.recentFoodDaoProvider = recentFoodDaoProvider;
  }

  @Override
  public FoodRepository get() {
    return newInstance(foodRecordDaoProvider.get(), weightRecordDaoProvider.get(), foodDaoProvider.get(), recentFoodDaoProvider.get());
  }

  public static FoodRepository_Factory create(Provider<FoodRecordDao> foodRecordDaoProvider,
      Provider<WeightRecordDao> weightRecordDaoProvider, Provider<FoodDao> foodDaoProvider,
      Provider<RecentFoodDao> recentFoodDaoProvider) {
    return new FoodRepository_Factory(foodRecordDaoProvider, weightRecordDaoProvider, foodDaoProvider, recentFoodDaoProvider);
  }

  public static FoodRepository newInstance(FoodRecordDao foodRecordDao,
      WeightRecordDao weightRecordDao, FoodDao foodDao, RecentFoodDao recentFoodDao) {
    return new FoodRepository(foodRecordDao, weightRecordDao, foodDao, recentFoodDao);
  }
}
