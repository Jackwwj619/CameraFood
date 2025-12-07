package com.caloriesnap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.caloriesnap.data.local.dao.*
import com.caloriesnap.data.local.entity.*

@Database(
    entities = [FoodRecordEntity::class, WeightRecordEntity::class, FoodEntity::class, RecentFoodEntity::class, ExerciseRecordEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodRecordDao(): FoodRecordDao
    abstract fun weightRecordDao(): WeightRecordDao
    abstract fun foodDao(): FoodDao
    abstract fun recentFoodDao(): RecentFoodDao
    abstract fun exerciseRecordDao(): ExerciseRecordDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: android.content.Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: androidx.room.Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "caloriesnap.db")
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
