package com.caloriesnap.data.local.dao

import androidx.room.*
import com.caloriesnap.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodRecordDao {
    @Query("SELECT * FROM food_records WHERE date = :date ORDER BY id DESC")
    fun getByDate(date: String): Flow<List<FoodRecordEntity>>

    @Query("SELECT * FROM food_records WHERE date = :date")
    suspend fun getByDateSync(date: String): List<FoodRecordEntity>

    @Query("SELECT * FROM food_records WHERE date BETWEEN :start AND :end")
    fun getByDateRange(start: String, end: String): Flow<List<FoodRecordEntity>>

    @Insert
    suspend fun insert(record: FoodRecordEntity): Long

    @Update
    suspend fun update(record: FoodRecordEntity)

    @Delete
    suspend fun delete(record: FoodRecordEntity)

    @Query("DELETE FROM food_records WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM food_records")
    suspend fun getAll(): List<FoodRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<FoodRecordEntity>)

    @Query("DELETE FROM food_records")
    suspend fun deleteAll()
}

@Dao
interface WeightRecordDao {
    @Query("SELECT * FROM weight_records ORDER BY date DESC")
    fun getAll(): Flow<List<WeightRecordEntity>>

    @Query("SELECT * FROM weight_records WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): WeightRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: WeightRecordEntity)

    @Query("SELECT * FROM weight_records WHERE date BETWEEN :start AND :end ORDER BY date")
    fun getByDateRange(start: String, end: String): Flow<List<WeightRecordEntity>>

    @Query("SELECT * FROM weight_records")
    suspend fun getAllList(): List<WeightRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<WeightRecordEntity>)

    @Query("DELETE FROM weight_records")
    suspend fun deleteAll()
}

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods WHERE name LIKE '%' || :query || '%' LIMIT 50")
    suspend fun search(query: String): List<FoodEntity>

    @Query("SELECT * FROM foods WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<FoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: FoodEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(foods: List<FoodEntity>)

    @Update
    suspend fun update(food: FoodEntity)

    @Query("UPDATE foods SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("SELECT * FROM foods WHERE isCustom = 1")
    suspend fun getCustomFoods(): List<FoodEntity>

    @Query("SELECT COUNT(*) FROM foods")
    suspend fun count(): Int
}

@Dao
interface RecentFoodDao {
    @Query("SELECT f.* FROM foods f INNER JOIN recent_foods r ON f.id = r.foodId ORDER BY r.usedAt DESC LIMIT 20")
    fun getRecent(): Flow<List<FoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recent: RecentFoodEntity)
}
