package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.LiliaDatabase
import com.example.data.model.ChatMessageEntity
import com.example.data.model.MealAnalysisResult
import com.example.data.model.MealEntity
import com.example.data.model.PlateAnalysisUiState
import com.example.data.model.RecipeItem
import com.example.data.model.ShoppingItemEntity
import com.example.data.model.UserProfileEntity
import com.example.data.repository.LiliaRepository
import com.example.data.stripe.LiliaPlan
import com.example.data.stripe.LiliaPlanCatalog
import com.example.data.stripe.StripePaymentState
import com.example.data.stripe.StripeService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LiliaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = LiliaDatabase.getInstance(application)
    private val repository = LiliaRepository(database)

    val userProfile: StateFlow<UserProfileEntity> = repository.userProfile
        .map { it ?: UserProfileEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfileEntity())

    val meals: StateFlow<List<MealEntity>> = repository.meals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shoppingItems: StateFlow<List<ShoppingItemEntity>> = repository.shoppingItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRecipes: StateFlow<List<RecipeItem>> = repository.recipes

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedDietFilter = MutableStateFlow("All Recipes")
    val selectedDietFilter: StateFlow<String> = _selectedDietFilter.asStateFlow()

    val filteredRecipes: StateFlow<List<RecipeItem>> = combine(
        allRecipes,
        _searchQuery,
        _selectedDietFilter
    ) { recipes, query, filter ->
        recipes.filter { recipe ->
            val matchesQuery = query.isBlank() ||
                    recipe.title.contains(query, ignoreCase = true) ||
                    recipe.description.contains(query, ignoreCase = true) ||
                    recipe.tags.any { it.contains(query, ignoreCase = true) }

            val matchesFilter = when (filter) {
                "All Recipes" -> true
                "Vegan" -> recipe.tags.any { it.equals("Vegan", ignoreCase = true) } || recipe.category.equals("Vegan", ignoreCase = true)
                "Gluten-Free" -> recipe.tags.any { it.equals("Gluten-Free", ignoreCase = true) }
                "Low Carb" -> recipe.tags.any { it.equals("Low Carb", ignoreCase = true) } || recipe.category.equals("Low Carb", ignoreCase = true)
                "High Protein" -> recipe.tags.any { it.equals("High Protein", ignoreCase = true) }
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedRecipe = MutableStateFlow<RecipeItem?>(null)
    val selectedRecipe: StateFlow<RecipeItem?> = _selectedRecipe.asStateFlow()

    private val _isAnalyzingPlate = MutableStateFlow(false)
    val isAnalyzingPlate: StateFlow<Boolean> = _isAnalyzingPlate.asStateFlow()

    private val _currentAnalysisResult = MutableStateFlow<MealAnalysisResult?>(null)
    val currentAnalysisResult: StateFlow<MealAnalysisResult?> = _currentAnalysisResult.asStateFlow()

    private val _analysisUiState = MutableStateFlow<PlateAnalysisUiState>(PlateAnalysisUiState.Idle)
    val analysisUiState: StateFlow<PlateAnalysisUiState> = _analysisUiState.asStateFlow()

    private val _isAiTyping = MutableStateFlow(false)
    val isAiTyping: StateFlow<Boolean> = _isAiTyping.asStateFlow()

    private val _isRecordingVoice = MutableStateFlow(false)
    val isRecordingVoice: StateFlow<Boolean> = _isRecordingVoice.asStateFlow()

    // Web3 / Eat-to-Earn States for Hackathon
    private val _web3WalletState = MutableStateFlow(
        com.example.data.web3.Web3WalletState(
            transactions = com.example.data.web3.Web3RewardsManager.getInitialTransactions()
        )
    )
    val web3WalletState: StateFlow<com.example.data.web3.Web3WalletState> = _web3WalletState.asStateFlow()
    val web3Perks = com.example.data.web3.Web3RewardsManager.getAvailablePerks()

    private val stripeService = StripeService()

    private val _selectedPlan = MutableStateFlow<LiliaPlan>(LiliaPlanCatalog.PLAN_TRANSFORMATION)
    val selectedPlan: StateFlow<LiliaPlan> = _selectedPlan.asStateFlow()

    private val _paymentState = MutableStateFlow<StripePaymentState>(StripePaymentState.Idle)
    val paymentState: StateFlow<StripePaymentState> = _paymentState.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun setSelectedPlan(plan: LiliaPlan) {
        _selectedPlan.value = plan
    }

    fun resetPaymentState() {
        _paymentState.value = StripePaymentState.Idle
    }

    fun processStripeCardPayment(
        plan: LiliaPlan,
        cardNumber: String,
        cardExpMonth: String,
        cardExpYear: String,
        cardCvc: String,
        cardHolderName: String
    ) {
        viewModelScope.launch {
            _paymentState.value = StripePaymentState.Processing("Validando com a adquirente Stripe...")
            val result = stripeService.processCardPayment(
                plan = plan,
                cardNumber = cardNumber,
                cardExpMonth = cardExpMonth,
                cardExpYear = cardExpYear,
                cardCvc = cardCvc,
                cardHolderName = cardHolderName
            )
            result.onSuccess { txId ->
                repository.activatePlan(plan)
                _paymentState.value = StripePaymentState.Success(plan, txId)
                showToast("Pagamento de ${plan.priceFormatted} aprovado via Stripe! 🎉")
            }.onFailure { error ->
                _paymentState.value = StripePaymentState.Error(error.message ?: "Falha ao processar pagamento")
                showToast("Não foi possível processar o pagamento no momento.")
            }
        }
    }

    fun generateStripePixPayment(plan: LiliaPlan) {
        viewModelScope.launch {
            _paymentState.value = StripePaymentState.Processing("Gerando QR Code Pix instantâneo...")
            val result = stripeService.generatePixPayment(plan)
            result.onSuccess { payload ->
                _paymentState.value = StripePaymentState.PixGenerated(
                    plan = plan,
                    qrCodePayload = payload.copyPasteCode,
                    copyPasteCode = payload.copyPasteCode
                )
            }.onFailure { error ->
                _paymentState.value = StripePaymentState.Error(error.message ?: "Falha ao gerar Pix")
            }
        }
    }

    fun confirmPixPaymentReceived(plan: LiliaPlan) {
        viewModelScope.launch {
            _paymentState.value = StripePaymentState.Processing("Confirmando liquidação instantânea via Pix...")
            delay(1200)
            repository.activatePlan(plan)
            _paymentState.value = StripePaymentState.Success(plan, "pix_tx_${System.currentTimeMillis()}")
            showToast("Pix confirmado! ${plan.title} desbloqueado! ✨")
        }
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setDietFilter(filter: String) {
        _selectedDietFilter.value = filter
    }

    fun selectRecipe(recipe: RecipeItem?) {
        _selectedRecipe.value = recipe
    }

    fun toggleFavorite(recipeId: String) {
        repository.toggleFavoriteRecipe(recipeId)
    }

    fun addRecipeToShoppingList(recipe: RecipeItem) {
        viewModelScope.launch {
            repository.addRecipeIngredientsToShoppingList(recipe)
            showToast("Ingredientes adicionados à Lista da Semana!")
        }
    }

    fun sendUserMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _isAiTyping.value = true
            try {
                repository.sendChatMessage(text)
            } finally {
                _isAiTyping.value = false
            }
        }
    }

    fun startVoiceRecording() {
        _isRecordingVoice.value = true
    }

    fun stopVoiceRecordingAndSend(simulatedText: String = "Lília, comi um pedaço de chocolate e quero ajustar meu jantar com tranquilidade.") {
        _isRecordingVoice.value = false
        sendUserMessage(simulatedText)
    }

    fun cancelVoiceRecording() {
        _isRecordingVoice.value = false
    }

    fun triggerPlateAnalysis(mealType: String = "Almoço") {
        viewModelScope.launch {
            _isAnalyzingPlate.value = true
            _analysisUiState.value = PlateAnalysisUiState.Loading(
                mealType = mealType,
                stepMessage = "Conectando com a Visão Computacional Nutricional...",
                progress = 0.25f
            )

            delay(600)
            _analysisUiState.value = PlateAnalysisUiState.Loading(
                mealType = mealType,
                stepMessage = "Identificando preparações, texturas e porções do prato...",
                progress = 0.60f
            )

            try {
                val result = repository.analyzePlatePhoto(mealType)
                delay(400)
                _analysisUiState.value = PlateAnalysisUiState.Loading(
                    mealType = mealType,
                    stepMessage = "Lília está preparando um feedback acolhedor para você...",
                    progress = 0.90f
                )
                delay(300)

                if (!result.isFoodDetected || result.identifiedFoods.isEmpty()) {
                    _analysisUiState.value = PlateAnalysisUiState.NoFoodDetected(result, mealType)
                    _currentAnalysisResult.value = result
                } else {
                    _analysisUiState.value = PlateAnalysisUiState.Success(result, mealType)
                    _currentAnalysisResult.value = result
                }
            } catch (e: Exception) {
                _analysisUiState.value = PlateAnalysisUiState.Error(
                    mealType = mealType,
                    title = "Não conseguimos concluir a análise agora",
                    message = "Tudo bem, acontece! Pode ter havido uma oscilação na conexão. Você pode tentar novamente em instantes ou me contar pelo chat."
                )
            } finally {
                _isAnalyzingPlate.value = false
            }
        }
    }

    fun dismissAnalysisModal() {
        _currentAnalysisResult.value = null
        _analysisUiState.value = PlateAnalysisUiState.Idle
    }

    fun manualLogMeal(
        mealType: String,
        description: String,
        calories: Int,
        protein: Int,
        carbs: Int,
        fat: Int
    ) {
        viewModelScope.launch {
            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val newMeal = MealEntity(
                title = mealType,
                description = description.ifBlank { "Refeição registrada manualmente" },
                mealType = mealType,
                time = currentTime,
                calories = calories.coerceAtLeast(0),
                protein = protein.coerceAtLeast(0),
                carbs = carbs.coerceAtLeast(0),
                fat = fat.coerceAtLeast(0),
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCLApRHLgIIuQorkGQKFpVESEkaU-NAwKYyCZITGMXBkixb2TSo65Vht55d7T2TJKE3q9IvRXjqb3tCfidE0pgqBDjpeETPycYHGa6nmtO8F1rXhiHxeyJRrxJlUHV2hxxpZzpfB8sAOfl-B4jWgwCPzHX7-By8cQaQZODrDDPWN3DwQ_68khtlj__vkMlCdmetVW5ta0QRHR_ivx_Y3JNcRJe9IjQriTglYkYMLN76TCdlaKCxwnN3"
            )
            repository.addMeal(newMeal)
            dismissAnalysisModal()
            showToast("Refeição registrada com sucesso no seu Diário! ✨")
        }
    }

    fun confirmAndLogMeal(result: MealAnalysisResult, mealType: String = "Almoço") {
        viewModelScope.launch {
            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val newMeal = MealEntity(
                title = mealType,
                description = result.identifiedFoods.joinToString(", "),
                mealType = mealType,
                time = currentTime,
                calories = result.calories,
                protein = result.protein,
                carbs = result.carbs,
                fat = result.fat,
                imageUrl = result.plateImageUrl
            )
            repository.addMeal(newMeal)
            _currentAnalysisResult.value = null
            showToast("Prato confirmado e registrado no seu Diário! ✨")

            // Eat-to-Earn Web3 automatic emission on healthy verified meal
            emitWeb3Reward(
                actionTitle = "Prato $mealType Validado pela IA",
                category = "Refeição Saudável",
                amount = 15.0
            )
        }
    }

    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
            showToast("Refeição removida.")
        }
    }

    fun toggleShoppingItem(item: ShoppingItemEntity) {
        viewModelScope.launch {
            repository.toggleShoppingItem(item)
        }
    }

    fun addShoppingItem(name: String, quantity: String, category: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addShoppingItem(name, quantity, category)
            showToast("Item adicionado à lista!")
        }
    }

    fun clearCheckedShoppingItems() {
        viewModelScope.launch {
            repository.deleteCheckedShoppingItems()
            showToast("Itens concluídos removidos!")
        }
    }

    fun logWater(amountMl: Int) {
        viewModelScope.launch {
            repository.logWater(amountMl)
            showToast("+${amountMl}ml de água registrados! 💧")
        }
    }

    fun updateGoal(newGoal: String, desc: String) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.updateProfile(
                current.copy(currentGoal = newGoal, goalDescription = desc)
            )
            showToast("Meta atualizada com sucesso!")
        }
    }

    fun updateDietaryPrefs(newPrefs: String) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.updateProfile(
                current.copy(dietaryPrefs = newPrefs)
            )
            showToast("Preferências alimentares salvas!")
        }
    }

    fun syncWithHealthConnect() {
        viewModelScope.launch {
            showToast("Sincronizando passos e queima com o Google Health Connect...")
            try {
                val data = com.example.data.health.HealthConnectManager.syncHealthData(getApplication())
                repository.syncHealthConnect(
                    steps = data.steps,
                    caloriesBurned = data.activeCaloriesBurned,
                    lastSync = data.lastSyncTime
                )
                showToast("Health Connect sincronizado: ${data.steps} passos • ${data.activeCaloriesBurned} kcal gastas! 🏃‍♀️")
            } catch (e: Exception) {
                showToast("Não foi possível conectar ao Health Connect no momento.")
            }
        }
    }

    fun toggleHealthConnectSync(enable: Boolean) {
        viewModelScope.launch {
            repository.setHealthConnectSyncStatus(enable)
            if (enable) {
                syncWithHealthConnect()
            } else {
                showToast("Sincronização com Google Health Connect pausada.")
            }
        }
    }

    fun exportNutritionPdf(onPdfReady: (java.io.File) -> Unit) {
        viewModelScope.launch {
            showToast("Gerando Relatório Nutricional em PDF (v1.1)...")
            val currentProfile = userProfile.value
            val currentMeals = meals.value
            val result = com.example.data.export.NutritionReportExporter.generatePdfReport(
                context = getApplication(),
                profile = currentProfile,
                meals = currentMeals
            )
            result.onSuccess { file ->
                showToast("Relatório PDF gerado com sucesso! 📄✨")
                onPdfReady(file)
            }.onFailure { err ->
                showToast("Erro ao exportar PDF: ${err.localizedMessage ?: "Tente novamente"}")
            }
        }
    }

    fun activatePremium() {
        viewModelScope.launch {
            repository.setPremium(true)
            showToast("Parabéns! Plano Lília Premium ativado com sucesso! 🌟")
        }
    }

    // Web3 / Eat-to-Earn Rewards Functions
    fun emitWeb3Reward(
        actionTitle: String,
        category: String,
        amount: Double
    ) {
        viewModelScope.launch {
            val current = _web3WalletState.value
            val txHash = com.example.data.web3.Web3RewardsManager.generateTxHash(actionTitle)
            val newTx = com.example.data.web3.Web3RewardTransaction(
                id = "tx_${System.currentTimeMillis()}",
                actionTitle = actionTitle,
                category = category,
                tokenAmount = amount,
                txHash = txHash,
                blockNumber = 59842100L + (1..1500).random(),
                timestamp = System.currentTimeMillis(),
                isEarning = true
            )
            val updatedBalance = current.tokenBalance + amount
            val updatedTotalEarned = current.totalEarned + amount
            val updatedTxList = listOf(newTx) + current.transactions

            _web3WalletState.value = current.copy(
                tokenBalance = updatedBalance,
                totalEarned = updatedTotalEarned,
                transactions = updatedTxList
            )
            showToast("🎁 Recompensa Web3: +${amount.toInt()} \$LILIA adicionados à sua carteira!")
        }
    }

    fun claimDailyStreakWeb3Reward() {
        viewModelScope.launch {
            emitWeb3Reward(
                actionTitle = "Bônus Ofensiva Consistente",
                category = "Streak Ofensiva",
                amount = 50.0
            )
        }
    }

    fun redeemWeb3Perk(perk: com.example.data.web3.Web3PerkItem, onClaimSuccess: (String) -> Unit) {
        viewModelScope.launch {
            val current = _web3WalletState.value
            if (current.tokenBalance >= perk.tokenCost) {
                val txHash = com.example.data.web3.Web3RewardsManager.generateTxHash(perk.id)
                val newTx = com.example.data.web3.Web3RewardTransaction(
                    id = "claim_${System.currentTimeMillis()}",
                    actionTitle = "Resgate: ${perk.title}",
                    category = "Resgate",
                    tokenAmount = perk.tokenCost,
                    txHash = txHash,
                    blockNumber = 59842200L + (1..1500).random(),
                    timestamp = System.currentTimeMillis(),
                    isEarning = false
                )
                val updatedBalance = current.tokenBalance - perk.tokenCost
                val updatedTotalClaimed = current.totalClaimed + perk.tokenCost
                val updatedTxList = listOf(newTx) + current.transactions

                _web3WalletState.value = current.copy(
                    tokenBalance = updatedBalance,
                    totalClaimed = updatedTotalClaimed,
                    transactions = updatedTxList
                )

                if (perk.id == "perk_1") {
                    repository.setPremium(true)
                }

                showToast("Resgate concluído com sucesso na Polygon PoS! 🎁")
                onClaimSuccess(perk.voucherCode)
            } else {
                showToast("Saldo insuficiente de \$LILIA.")
            }
        }
    }
}
