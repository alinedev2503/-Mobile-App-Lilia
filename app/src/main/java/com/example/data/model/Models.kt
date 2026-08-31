package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val mealType: String, // "Café da Manhã", "Almoço", "Jantar", "Lanche"
    val time: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: String,
    val category: String, // "Hortifruti", "Proteínas", "Grãos", "Outros"
    val isChecked: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "lilia" or "user"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val phase: Int = 0 // 1: Personal Data, 2: Health, 3: Habits, 4: Lifestyle, 5: Goals, 0: Ongoing Chat
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Ana Carolina",
    val email: String = "ana.carolina@exemplo.com",
    val birthDate: String = "15/05/1996",
    val gender: String = "Feminino",
    val healthHistory: String = "Sem condições crônicas",
    val allergies: String = "Lactose leve",
    val dietaryPrefs: String = "Sem Lactose, Baixo Carboidrato, Vegetariano (Ocasional)",
    val currentGoal: String = "Perda de Peso",
    val goalDescription: String = "Foco em déficit calórico moderado com alta saciedade.",
    val currentWeight: Float = 64.5f,
    val targetWeight: Float = 60.0f,
    val heightCm: Float = 163f,
    val imc: Float = 24.2f,
    val tmb: Int = 1450,
    val targetCalories: Int = 1800,
    val targetProtein: Int = 120,
    val targetCarbs: Int = 200,
    val targetFat: Int = 60,
    val streakDays: Int = 3,
    val waterIntakeMl: Int = 1800,
    val waterGoalMl: Int = 3000,
    val anamnesisPhase: Int = 1,
    val isAnamnesisCompleted: Boolean = true,
    val isPremium: Boolean = false,
    val activePlanId: String = "free",
    val planName: String = "Gratuito",
    val photosRemaining: Int = 3,
    val planExpiresAt: Long = 0L
)

data class RecipeItem(
    val id: String,
    val title: String,
    val description: String,
    val timeMinutes: Int,
    val calories: Int,
    val category: String, // "Vegan", "Gluten-Free", "Low Carb", "High Protein"
    val tags: List<String>,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val ingredients: List<String>,
    val instructions: List<String>
)

data class FoodComponent(
    val name: String,
    val estimatedGrams: Int = 100,
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fat: Int = 0,
    val notes: String = ""
)

data class MealAnalysisResult(
    val plateImageUrl: String = "",
    val identifiedFoods: List<String> = emptyList(),
    val components: List<FoodComponent> = emptyList(),
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fat: Int = 0,
    val allergens: List<String> = emptyList(),
    val healthTags: List<String> = emptyList(),
    val feedbackTitle: String = "",
    val feedbackMessage: String = "",
    val feedbackParagraph1: String = "",
    val feedbackParagraph2: String = "",
    val isFoodDetected: Boolean = true,
    val noFoodReason: String = ""
)

sealed interface PlateAnalysisUiState {
    object Idle : PlateAnalysisUiState
    data class Loading(
        val mealType: String,
        val stepMessage: String = "Lília está conectando com a visão computacional...",
        val progress: Float = 0.3f
    ) : PlateAnalysisUiState
    data class Success(
        val result: MealAnalysisResult,
        val mealType: String
    ) : PlateAnalysisUiState
    data class NoFoodDetected(
        val result: MealAnalysisResult,
        val mealType: String
    ) : PlateAnalysisUiState
    data class Error(
        val mealType: String,
        val title: String = "Não conseguimos concluir a análise agora",
        val message: String = "Tudo bem, acontece! Pode ter havido uma oscilação na conexão. Que tal tentar novamente com calma ou registrar os alimentos por texto?"
    ) : PlateAnalysisUiState
}
