package com.example.data.repository

import com.example.data.local.LiliaDatabase
import com.example.data.model.ChatMessageEntity
import com.example.data.model.MealAnalysisResult
import com.example.data.model.MealEntity
import com.example.data.model.RecipeItem
import com.example.data.model.ShoppingItemEntity
import com.example.data.model.UserProfileEntity
import com.example.data.remote.LiliaAiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull

class LiliaRepository(
    private val db: LiliaDatabase,
    private val aiService: LiliaAiService = LiliaAiService()
) {
    val meals: Flow<List<MealEntity>> = db.mealDao().getAllMeals()
    val shoppingItems: Flow<List<ShoppingItemEntity>> = db.shoppingItemDao().getAllItems()
    val chatMessages: Flow<List<ChatMessageEntity>> = db.chatMessageDao().getAllMessages()
    val userProfile: Flow<UserProfileEntity?> = db.userProfileDao().getUserProfile()

    private val _recipes = MutableStateFlow(getInitialRecipes())
    val recipes: StateFlow<List<RecipeItem>> = _recipes.asStateFlow()

    suspend fun addMeal(meal: MealEntity) {
        db.mealDao().insertMeal(meal)
    }

    suspend fun deleteMeal(meal: MealEntity) {
        db.mealDao().deleteMeal(meal)
    }

    suspend fun toggleShoppingItem(item: ShoppingItemEntity) {
        db.shoppingItemDao().updateItem(item.copy(isChecked = !item.isChecked))
    }

    suspend fun addShoppingItem(name: String, quantity: String, category: String) {
        db.shoppingItemDao().insertItem(
            ShoppingItemEntity(name = name, quantity = quantity, category = category, isChecked = false)
        )
    }

    suspend fun deleteCheckedShoppingItems() {
        db.shoppingItemDao().deleteCheckedItems()
    }

    suspend fun addRecipeIngredientsToShoppingList(recipe: RecipeItem) {
        val items = recipe.ingredients.map { ing ->
            ShoppingItemEntity(
                name = ing,
                quantity = "1 porção",
                category = when {
                    ing.contains("abacate", true) || ing.contains("tomate", true) || ing.contains("aspargo", true) || ing.contains("banana", true) || ing.contains("mirtilo", true) -> "Hortifruti"
                    ing.contains("salmão", true) || ing.contains("frango", true) || ing.contains("ovo", true) -> "Proteínas"
                    ing.contains("quinoa", true) || ing.contains("chia", true) || ing.contains("aveia", true) -> "Grãos"
                    else -> "Outros"
                },
                isChecked = false
            )
        }
        db.shoppingItemDao().insertItems(items)
    }

    fun toggleFavoriteRecipe(recipeId: String) {
        _recipes.value = _recipes.value.map { recipe ->
            if (recipe.id == recipeId) recipe.copy(isFavorite = !recipe.isFavorite) else recipe
        }
    }

    suspend fun sendChatMessage(userText: String): String {
        // Insert user message
        db.chatMessageDao().insertMessage(
            ChatMessageEntity(sender = "user", text = userText)
        )

        // Fetch history
        val currentList = db.chatMessageDao().getAllMessages().firstOrNull() ?: emptyList()
        val history = currentList.map { it.sender to it.text }

        // Request response from AI
        val reply = aiService.chatWithLilia(history, userText)

        // Insert Lília response
        db.chatMessageDao().insertMessage(
            ChatMessageEntity(sender = "lilia", text = reply)
        )

        return reply
    }

    suspend fun analyzePlatePhoto(mealType: String): MealAnalysisResult {
        return aiService.analyzePlate("", mealType)
    }

    suspend fun logWater(amountMl: Int) {
        val profile = db.userProfileDao().getUserProfileDirect() ?: UserProfileEntity()
        val updatedAmount = (profile.waterIntakeMl + amountMl).coerceAtLeast(0)
        db.userProfileDao().insertOrUpdateProfile(profile.copy(waterIntakeMl = updatedAmount))
    }

    suspend fun updateProfile(updated: UserProfileEntity) {
        db.userProfileDao().insertOrUpdateProfile(updated)
    }

    suspend fun setPremium(active: Boolean) {
        val profile = db.userProfileDao().getUserProfileDirect() ?: UserProfileEntity()
        db.userProfileDao().insertOrUpdateProfile(profile.copy(isPremium = active))
    }

    suspend fun activatePlan(plan: com.example.data.stripe.LiliaPlan) {
        val profile = db.userProfileDao().getUserProfileDirect() ?: UserProfileEntity()
        val durationMillis = plan.durationDays.toLong() * 24L * 60L * 60L * 1000L
        val expiryTime = System.currentTimeMillis() + durationMillis
        db.userProfileDao().insertOrUpdateProfile(
            profile.copy(
                isPremium = true,
                activePlanId = plan.id,
                planName = plan.title,
                photosRemaining = plan.photoAnalysisLimit,
                planExpiresAt = expiryTime
            )
        )
    }

    suspend fun decrementPhotoCredit(): Boolean {
        val profile = db.userProfileDao().getUserProfileDirect() ?: UserProfileEntity()
        if (profile.photosRemaining > 0) {
            db.userProfileDao().insertOrUpdateProfile(
                profile.copy(photosRemaining = profile.photosRemaining - 1)
            )
            return true
        }
        return false
    }

    companion object {
        fun getInitialRecipes(): List<RecipeItem> = listOf(
            RecipeItem(
                id = "1",
                title = "Salada de Quinoa com Abacate",
                description = "Salada refrescante com abacate cremoso, quinoa real, tomate cereja e microverdes com toque de azeite e limão.",
                timeMinutes = 15,
                calories = 320,
                category = "Vegan",
                tags = listOf("Vegan", "Gluten-Free"),
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCLApRHLgIIuQorkGQKFpVESEkaU-NAwKYyCZITGMXBkixb2TSo65Vht55d7T2TJKE3q9IvRXjqb3tCfidE0pgqBDjpeETPycYHGa6nmtO8F1rXhiHxeyJRrxJlUHV2hxxpZzpfB8sAOfl-B4jWgwCPzHX7-By8cQaQZODrDDPWN3DwQ_68khtlj__vkMlCdmetVW5ta0QRHR_ivx_Y3JNcRJe9IjQriTglYkYMLN76TCdlaKCxwnN3",
                isFavorite = true,
                ingredients = listOf(
                    "1 xícara de quinoa cozida",
                    "1/2 abacate maduro em fatias",
                    "1/2 xícara de tomate cereja cortado",
                    "1 punhado de microverdes frescos",
                    "1 colher de sopa de azeite de oliva extra virgem",
                    "Suco de meio limão e sal a gosto"
                ),
                instructions = listOf(
                    "Em uma tigela funda, disponha a quinoa cozida como base.",
                    "Adicione os tomates cereja cortados ao meio e o abacate fatiado.",
                    "Decore com os microverdes e sementes de gergelim.",
                    "Tempere com o azeite, limão e uma pitada de sal marinho antes de servir."
                )
            ),
            RecipeItem(
                id = "2",
                title = "Grilled Salmon with Asparagus",
                description = "Filé de salmão grelhado na manteiga ghee e ervas finas com aspargos verdes crocantes ao vapor.",
                timeMinutes = 25,
                calories = 450,
                category = "Low Carb",
                tags = listOf("Low Carb", "High Protein"),
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCZ4UTNY94CuYXeWdJOFFyiu4A41SzvbEW3hHNso1-O1w-MOEpJqB6d3GbqAO43geoaWDrHgMeGkco-tI731_RjJrj2swtV5L8RMVsQYw4GixjurvvjWj3cqlu2oGf6vjwgcvBGJcWXnqdyLrlS1hXgK-nJx8Ysy1DhHkodBcBWsI8hDwjc56z88RjwxceGBe-CICfo7bvbbaSveeICoZ2-6k6q_FAcal7t7m7oi5g6hSKeBD4UTZh4",
                isFavorite = false,
                ingredients = listOf(
                    "1 filé de salmão fresco (180g)",
                    "1 maço de aspargos verdes frescos",
                    "1 dente de alho picado",
                    "1 colher de chá de manteiga ghee ou azeite",
                    "Fatias de limão siciliano e tomilho fresco"
                ),
                instructions = listOf(
                    "Aqueça a frigideira antiaderente com um fio de azeite.",
                    "Grelhe o salmão por 4 minutos de cada lado até dourar.",
                    "Em outra frigideira, salteie os aspargos com o alho por 3 minutos.",
                    "Sirva com raspas de limão e raminhos de tomilho fresco."
                )
            ),
            RecipeItem(
                id = "3",
                title = "Morning Acai Energy Bowl",
                description = "Tigela nutritiva de açaí puro sem xarope, com lâminas de banana fresca, mirtilos, sementes de chia e lascas de coco.",
                timeMinutes = 10,
                calories = 280,
                category = "Vegan",
                tags = listOf("Vegan", "Antioxidant"),
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBvZXWevs49ANvctfNHzmj73ar6xA1xBVFwLkqcqTMcv-naj0JaLtZC5Hyps1MaXIKMdioCq9rwjaw4Qt5oqETz9CtFHAgRrSjKQcfzhes-N1JienmpE-PHGkXtypypO86np-wLENfDsCNjTZ6m0Safll3VLQdeJTwRmyyFInPiUante1I8gySbQpxmB940U91jqIz8y8TIgyOBwnxV6-vQ_Qcwm5qFYcZ1cPi34vyUV8uHZcsls02S",
                isFavorite = false,
                ingredients = listOf(
                    "200g de polpa de açaí puro congelado",
                    "1 banana prata fatiada",
                    "1/4 xícara de mirtilos frescos",
                    "1 colher de sopa de sementes de chia",
                    "1 colher de sopa de coco ralado em lascas"
                ),
                instructions = listOf(
                    "Bata o açaí congelado com 50ml de água de coco até consistência de sorbet.",
                    "Transfira para uma tigela funda.",
                    "Distribua ordenadamente as fatias de banana, mirtilos, chia e coco.",
                    "Consuma imediatamente para máxima energia e frescor."
                )
            )
        )
    }
}
