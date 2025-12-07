package com.caloriesnap.di

import android.content.Context
import androidx.room.Room
import com.caloriesnap.data.local.AppDatabase
import com.caloriesnap.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "caloriesnap.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideFoodRecordDao(db: AppDatabase): FoodRecordDao = db.foodRecordDao()
    @Provides fun provideWeightRecordDao(db: AppDatabase): WeightRecordDao = db.weightRecordDao()
    @Provides fun provideFoodDao(db: AppDatabase): FoodDao = db.foodDao()
    @Provides fun provideRecentFoodDao(db: AppDatabase): RecentFoodDao = db.recentFoodDao()
    @Provides fun provideExerciseRecordDao(db: AppDatabase): ExerciseRecordDao = db.exerciseRecordDao()
}
