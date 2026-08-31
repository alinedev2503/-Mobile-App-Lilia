package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ChatMessageEntity
import com.example.data.model.MealEntity
import com.example.data.model.ShoppingItemEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MealEntity::class,
        ShoppingItemEntity::class,
        ChatMessageEntity::class,
        UserProfileEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class LiliaDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun shoppingItemDao(): ShoppingItemDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: LiliaDatabase? = null

        fun getInstance(context: Context): LiliaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LiliaDatabase::class.java,
                    "lilia_diet_db"
                ).fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            seedDatabase(database)
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedDatabase(db: LiliaDatabase) {
            // Seed User Profile
            db.userProfileDao().insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Ana Carolina",
                    email = "ana.carolina@exemplo.com",
                    birthDate = "15/05/1996",
                    gender = "Feminino",
                    healthHistory = "Sem restrições crônicas diagnosticadas",
                    allergies = "Intolerância leve a lactose",
                    dietaryPrefs = "Sem Lactose, Baixo Carboidrato, Vegetariano (Ocasional)",
                    currentGoal = "Perda de Peso",
                    goalDescription = "Foco em déficit calórico moderado com alta saciedade.",
                    currentWeight = 64.5f,
                    targetWeight = 60.0f,
                    heightCm = 163f,
                    imc = 24.2f,
                    tmb = 1450,
                    targetCalories = 1800,
                    targetProtein = 120,
                    targetCarbs = 200,
                    targetFat = 60,
                    streakDays = 3,
                    waterIntakeMl = 1800,
                    waterGoalMl = 3000,
                    anamnesisPhase = 1,
                    isAnamnesisCompleted = true,
                    isPremium = false
                )
            )

            // Seed Initial Breakfast Meal matching design
            db.mealDao().insertMeal(
                MealEntity(
                    title = "Café da Manhã",
                    description = "Café com leite e pão integral",
                    mealType = "Café da Manhã",
                    time = "08:30",
                    calories = 250,
                    protein = 12,
                    carbs = 35,
                    fat = 6,
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDXXkh_XZvs7IfodrRj6PpdbvcH4giu8HmN4snBXEhCGY-9tHs-RAEy_clYtTTfY9ZH5oSig7OfLPZp9eiKZG7nawV6TPf4EsoId96ORBi2yWDy2lChg1zttUgu_ZAac0h8tWM-SLN6Y4ILdSFLmNp06GawPPXBUQenFiVe7bEkngpT1DNz8VPxAM-f1ZjhSB0BYxwTMc-1WYDpfI26MUOoHcV4L5FgzpVCU1vmVctTQIKRnO1tIG56"
                )
            )

            // Seed Initial Shopping List items matching design
            val shoppingItems = listOf(
                ShoppingItemEntity(name = "Maçã Gala", quantity = "6 unid.", category = "Hortifruti", isChecked = false),
                ShoppingItemEntity(name = "Alface Americana", quantity = "1 pé", category = "Hortifruti", isChecked = true),
                ShoppingItemEntity(name = "Cebola Roxa", quantity = "3 unid.", category = "Hortifruti", isChecked = false),
                ShoppingItemEntity(name = "Peito de Frango", quantity = "800g", category = "Proteínas", isChecked = false),
                ShoppingItemEntity(name = "Ovos", quantity = "1 dúzia", category = "Proteínas", isChecked = false),
                ShoppingItemEntity(name = "Aveia em Flocos", quantity = "500g", category = "Grãos", isChecked = true)
            )
            db.shoppingItemDao().insertItems(shoppingItems)

            // Seed Initial Chat Welcome Message from Lília
            db.chatMessageDao().insertMessage(
                ChatMessageEntity(
                    sender = "lilia",
                    text = "Olá! Sou a Lília. Vamos começar sua jornada? Qual o seu nome completo e data de nascimento?",
                    phase = 1
                )
            )
        }
    }
}
