package com.caloriesnap.data.local

import com.caloriesnap.data.local.entity.FoodEntity

object DefaultFoods {
    // 分类食物数据
    val categories = mapOf(
        "主食" to listOf(
            FoodEntity(name = "米饭", calories = 116f, protein = 2.6f, carbs = 25.6f, fat = 0.3f),
            FoodEntity(name = "面条", calories = 137f, protein = 4.5f, carbs = 28.4f, fat = 0.8f),
            FoodEntity(name = "馒头", calories = 223f, protein = 7f, carbs = 47f, fat = 1.1f),
            FoodEntity(name = "面包", calories = 312f, protein = 8.3f, carbs = 58.6f, fat = 5.1f),
            FoodEntity(name = "燕麦", calories = 377f, protein = 13.5f, carbs = 67.7f, fat = 6.5f),
            FoodEntity(name = "粥", calories = 46f, protein = 1.1f, carbs = 9.8f, fat = 0.3f),
            FoodEntity(name = "饺子", calories = 183f, protein = 7.6f, carbs = 25.3f, fat = 5.8f),
            FoodEntity(name = "包子", calories = 227f, protein = 7.2f, carbs = 39.2f, fat = 4.5f)
        ),
        "肉蛋" to listOf(
            FoodEntity(name = "鸡胸肉", calories = 133f, protein = 31f, carbs = 0f, fat = 1.2f),
            FoodEntity(name = "鸡蛋", calories = 144f, protein = 13.3f, carbs = 1.1f, fat = 9.5f),
            FoodEntity(name = "牛肉", calories = 125f, protein = 19.9f, carbs = 0f, fat = 4.2f),
            FoodEntity(name = "猪肉", calories = 143f, protein = 20.3f, carbs = 0f, fat = 6.2f),
            FoodEntity(name = "鱼肉", calories = 103f, protein = 17.6f, carbs = 0f, fat = 3.8f),
            FoodEntity(name = "虾", calories = 87f, protein = 18.6f, carbs = 0f, fat = 0.8f),
            FoodEntity(name = "鸡腿", calories = 181f, protein = 16f, carbs = 0f, fat = 13f),
            FoodEntity(name = "鸭肉", calories = 240f, protein = 15.5f, carbs = 0f, fat = 19.7f)
        ),
        "蔬菜" to listOf(
            FoodEntity(name = "西兰花", calories = 34f, protein = 4.3f, carbs = 4f, fat = 0.4f),
            FoodEntity(name = "胡萝卜", calories = 37f, protein = 1f, carbs = 8.8f, fat = 0.2f),
            FoodEntity(name = "番茄", calories = 19f, protein = 0.9f, carbs = 4f, fat = 0.2f),
            FoodEntity(name = "黄瓜", calories = 15f, protein = 0.8f, carbs = 2.9f, fat = 0.2f),
            FoodEntity(name = "生菜", calories = 13f, protein = 1.3f, carbs = 2f, fat = 0.3f),
            FoodEntity(name = "土豆", calories = 81f, protein = 2.6f, carbs = 17.8f, fat = 0.2f),
            FoodEntity(name = "白菜", calories = 17f, protein = 1.5f, carbs = 3.2f, fat = 0.2f),
            FoodEntity(name = "菠菜", calories = 24f, protein = 2.6f, carbs = 3.6f, fat = 0.3f)
        ),
        "水果" to listOf(
            FoodEntity(name = "苹果", calories = 52f, protein = 0.3f, carbs = 13.8f, fat = 0.2f),
            FoodEntity(name = "香蕉", calories = 93f, protein = 1.4f, carbs = 22.8f, fat = 0.2f),
            FoodEntity(name = "橙子", calories = 47f, protein = 0.8f, carbs = 11.8f, fat = 0.2f),
            FoodEntity(name = "葡萄", calories = 43f, protein = 0.5f, carbs = 10.3f, fat = 0.2f),
            FoodEntity(name = "西瓜", calories = 25f, protein = 0.5f, carbs = 5.8f, fat = 0.1f),
            FoodEntity(name = "草莓", calories = 32f, protein = 1f, carbs = 7.1f, fat = 0.2f),
            FoodEntity(name = "猕猴桃", calories = 56f, protein = 0.8f, carbs = 14.5f, fat = 0.6f),
            FoodEntity(name = "梨", calories = 44f, protein = 0.4f, carbs = 11.8f, fat = 0.2f)
        ),
        "奶豆" to listOf(
            FoodEntity(name = "牛奶", calories = 54f, protein = 3f, carbs = 3.4f, fat = 3.2f),
            FoodEntity(name = "酸奶", calories = 72f, protein = 2.5f, carbs = 9.3f, fat = 2.7f),
            FoodEntity(name = "豆腐", calories = 81f, protein = 8.1f, carbs = 4.2f, fat = 3.7f),
            FoodEntity(name = "豆浆", calories = 31f, protein = 2.9f, carbs = 1.2f, fat = 1.6f),
            FoodEntity(name = "奶酪", calories = 328f, protein = 20f, carbs = 3.5f, fat = 26f),
            FoodEntity(name = "豆干", calories = 140f, protein = 16.2f, carbs = 4.9f, fat = 6.5f)
        ),
        "零食" to listOf(
            FoodEntity(name = "花生", calories = 574f, protein = 24.8f, carbs = 21.7f, fat = 44.3f),
            FoodEntity(name = "核桃", calories = 646f, protein = 14.9f, carbs = 19.1f, fat = 58.8f),
            FoodEntity(name = "薯片", calories = 547f, protein = 5.3f, carbs = 51.5f, fat = 35.2f),
            FoodEntity(name = "巧克力", calories = 586f, protein = 4.9f, carbs = 60f, fat = 37.4f),
            FoodEntity(name = "饼干", calories = 433f, protein = 8.3f, carbs = 71.7f, fat = 12.7f),
            FoodEntity(name = "蛋糕", calories = 348f, protein = 7.4f, carbs = 53.6f, fat = 11.9f)
        ),
        "饮品" to listOf(
            FoodEntity(name = "可乐", calories = 43f, protein = 0f, carbs = 10.6f, fat = 0f),
            FoodEntity(name = "橙汁", calories = 45f, protein = 0.7f, carbs = 10.4f, fat = 0.2f),
            FoodEntity(name = "咖啡(黑)", calories = 2f, protein = 0.1f, carbs = 0.4f, fat = 0f),
            FoodEntity(name = "奶茶", calories = 52f, protein = 0.8f, carbs = 8.5f, fat = 1.8f),
            FoodEntity(name = "啤酒", calories = 32f, protein = 0.4f, carbs = 2.5f, fat = 0f),
            FoodEntity(name = "豆奶", calories = 30f, protein = 2.4f, carbs = 2.5f, fat = 1.4f)
        ),
        "家常菜" to listOf(
            FoodEntity(name = "红烧肉", calories = 358f, protein = 12.3f, carbs = 5.2f, fat = 32.4f),
            FoodEntity(name = "宫保鸡丁", calories = 197f, protein = 15.8f, carbs = 8.6f, fat = 11.2f),
            FoodEntity(name = "麻婆豆腐", calories = 135f, protein = 8.5f, carbs = 5.3f, fat = 9.1f),
            FoodEntity(name = "炒青菜", calories = 45f, protein = 2.1f, carbs = 3.8f, fat = 2.5f),
            FoodEntity(name = "蛋炒饭", calories = 186f, protein = 6.2f, carbs = 28.5f, fat = 5.8f),
            FoodEntity(name = "番茄炒蛋", calories = 86f, protein = 5.8f, carbs = 5.2f, fat = 5.1f),
            FoodEntity(name = "糖醋排骨", calories = 264f, protein = 13.5f, carbs = 18.2f, fat = 15.3f),
            FoodEntity(name = "鱼香肉丝", calories = 178f, protein = 12.4f, carbs = 9.8f, fat = 10.5f)
        )
    )

    val list = categories.values.flatten()
}
