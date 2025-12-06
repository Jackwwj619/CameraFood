package com.caloriesnap.data.repository

import com.caloriesnap.data.local.dao.*
import com.caloriesnap.data.local.entity.*
import com.caloriesnap.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val foodRecordDao: FoodRecordDao,
    private val weightRecordDao: WeightRecordDao,
    private val foodDao: FoodDao,
    private val recentFoodDao: RecentFoodDao
) {
    fun getRecordsByDate(date: String): Flow<List<FoodRecord>> =
        foodRecordDao.getByDate(date).map { list -> list.map { it.toDomain() } }

    fun getRecordsByDateRange(start: String, end: String): Flow<List<FoodRecord>> =
        foodRecordDao.getByDateRange(start, end).map { list -> list.map { it.toDomain() } }

    suspend fun addRecord(record: FoodRecord): Long = foodRecordDao.insert(record.toEntity())

    suspend fun updateRecord(record: FoodRecord) = foodRecordDao.update(record.toEntity())

    suspend fun deleteRecord(id: Long) = foodRecordDao.deleteById(id)

    suspend fun getAllRecords() = foodRecordDao.getAll().map { it.toDomain() }

    suspend fun importRecords(records: List<FoodRecord>) {
        foodRecordDao.deleteAll()
        foodRecordDao.insertAll(records.map { it.toEntity() })
    }

    fun getWeightRecords(): Flow<List<WeightRecord>> =
        weightRecordDao.getAll().map { list -> list.map { WeightRecord(it.id, it.date, it.weight) } }

    fun getWeightByDateRange(start: String, end: String): Flow<List<WeightRecord>> =
        weightRecordDao.getByDateRange(start, end).map { list -> list.map { WeightRecord(it.id, it.date, it.weight) } }

    suspend fun addWeightRecord(date: String, weight: Float) =
        weightRecordDao.insert(WeightRecordEntity(date = date, weight = weight))

    suspend fun getAllWeightRecords() = weightRecordDao.getAllList().map { WeightRecord(it.id, it.date, it.weight) }

    suspend fun importWeightRecords(records: List<WeightRecord>) {
        weightRecordDao.deleteAll()
        weightRecordDao.insertAll(records.map { WeightRecordEntity(date = it.date, weight = it.weight) })
    }

    suspend fun searchFoods(query: String): List<Food> = foodDao.search(query).map { it.toDomain() }

    fun getFavoriteFoods(): Flow<List<Food>> = foodDao.getFavorites().map { list -> list.map { it.toDomain() } }

    fun getRecentFoods(): Flow<List<Food>> = recentFoodDao.getRecent().map { list -> list.map { it.toDomain() } }

    suspend fun addCustomFood(food: Food): Long = foodDao.insert(food.toEntity().copy(isCustom = true))

    suspend fun setFavorite(id: Long, isFavorite: Boolean) = foodDao.setFavorite(id, isFavorite)

    suspend fun addToRecent(foodId: Long) = recentFoodDao.insert(RecentFoodEntity(foodId, System.currentTimeMillis()))

    suspend fun getCustomFoods() = foodDao.getCustomFoods().map { it.toDomain() }

    suspend fun getFoodCount() = foodDao.count()

    suspend fun insertDefaultFoods(foods: List<FoodEntity>) = foodDao.insertAll(foods)

    private fun FoodRecordEntity.toDomain() = FoodRecord(id, date, MealType.valueOf(mealType), foodName, weight, calories, protein, carbs, fat, imageUri)
    private fun FoodRecord.toEntity() = FoodRecordEntity(id, date, mealType.name, foodName, weight, calories, protein, carbs, fat, imageUri)
    private fun FoodEntity.toDomain() = Food(id, name, calories, protein, carbs, fat)
    private fun Food.toEntity() = FoodEntity(id, name, calories, protein, carbs, fat)
}
