package com.example.ui.screens.meal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MealAnalysisResult
import com.example.data.model.PlateAnalysisUiState
import com.example.ui.theme.LiliaBackground
import com.example.ui.theme.LiliaCarbGold
import com.example.ui.theme.LiliaMintLight
import com.example.ui.theme.LiliaOnPrimary
import com.example.ui.theme.LiliaOutlineVariant
import com.example.ui.theme.LiliaPrimary
import com.example.ui.theme.LiliaPrimaryFixed
import com.example.ui.theme.LiliaSecondary
import com.example.ui.theme.LiliaSurfaceContainerLowest
import com.example.ui.theme.PillShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealAnalysisBottomSheet(
    uiState: PlateAnalysisUiState,
    mealType: String = "Almoço",
    onConfirm: (MealAnalysisResult) -> Unit,
    onRetry: (String) -> Unit = {},
    onManualLog: (String, String, Int, Int, Int, Int) -> Unit = { _, _, _, _, _, _ -> },
    onNavigateToChat: () -> Unit = {},
    onDismiss: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showManualEntryDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = LiliaSurfaceContainerLowest,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = null
    ) {
        when (uiState) {
            is PlateAnalysisUiState.Loading -> {
                MealAnalysisLoadingView(
                    mealType = uiState.mealType,
                    stepMessage = uiState.stepMessage,
                    progress = uiState.progress,
                    onDismiss = onDismiss
                )
            }
            is PlateAnalysisUiState.NoFoodDetected -> {
                MealAnalysisNoFoodView(
                    result = uiState.result,
                    mealType = uiState.mealType,
                    onRetry = { onRetry(uiState.mealType) },
                    onOpenManualEntry = { showManualEntryDialog = true },
                    onNavigateToChat = onNavigateToChat,
                    onDismiss = onDismiss
                )
            }
            is PlateAnalysisUiState.Error -> {
                MealAnalysisErrorView(
                    title = uiState.title,
                    message = uiState.message,
                    mealType = uiState.mealType,
                    onRetry = { onRetry(uiState.mealType) },
                    onOpenManualEntry = { showManualEntryDialog = true },
                    onDismiss = onDismiss
                )
            }
            is PlateAnalysisUiState.Success -> {
                MealAnalysisSuccessView(
                    result = uiState.result,
                    mealType = uiState.mealType,
                    onConfirm = onConfirm,
                    onEditClick = { showEditDialog = true },
                    onDismiss = onDismiss
                )
            }
            is PlateAnalysisUiState.Idle -> {
                // Idle placeholder
                Box(modifier = Modifier.fillMaxWidth().height(100.dp))
            }
        }
    }

    // Manual Food Registration Dialog
    if (showManualEntryDialog) {
        var dishName by remember { mutableStateOf("") }
        var calString by remember { mutableStateOf("450") }
        var protString by remember { mutableStateOf("28") }
        var carbString by remember { mutableStateOf("40") }
        var fatString by remember { mutableStateOf("14") }

        AlertDialog(
            onDismissRequest = { showManualEntryDialog = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = LiliaSurfaceContainerLowest,
            title = {
                Text(
                    text = "Registrar $mealType Manualmente",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Digite os alimentos e os valores aproximados que você consumiu:",
                        style = MaterialTheme.typography.bodySmall.copy(color = LiliaSecondary)
                    )
                    OutlinedTextField(
                        value = dishName,
                        onValueChange = { dishName = it },
                        label = { Text("Nome da refeição ou alimentos") },
                        placeholder = { Text("Ex: Arroz, feijão, frango grelhado e salada") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LiliaPrimary,
                            unfocusedBorderColor = LiliaOutlineVariant.copy(alpha = 0.4f)
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = calString,
                            onValueChange = { calString = it },
                            label = { Text("Calorias (kcal)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = protString,
                            onValueChange = { protString = it },
                            label = { Text("Proteínas (g)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = carbString,
                            onValueChange = { carbString = it },
                            label = { Text("Carboidratos (g)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = fatString,
                            onValueChange = { fatString = it },
                            label = { Text("Gorduras (g)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val calories = calString.toIntOrNull() ?: 450
                        val protein = protString.toIntOrNull() ?: 28
                        val carbs = carbString.toIntOrNull() ?: 40
                        val fat = fatString.toIntOrNull() ?: 14
                        onManualLog(mealType, dishName, calories, protein, carbs, fat)
                        showManualEntryDialog = false
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                ) {
                    Text("Salvar no Diário", color = LiliaOnPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showManualEntryDialog = false }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    // Success Edit Dialog
    if (showEditDialog && uiState is PlateAnalysisUiState.Success) {
        val result = uiState.result
        var foodString by remember { mutableStateOf(result.identifiedFoods.joinToString(", ")) }
        var calString by remember { mutableStateOf(result.calories.toString()) }
        var protString by remember { mutableStateOf(result.protein.toString()) }
        var carbString by remember { mutableStateOf(result.carbs.toString()) }
        var fatString by remember { mutableStateOf(result.fat.toString()) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = LiliaSurfaceContainerLowest,
            title = {
                Text(
                    text = "Ajustar Alimentos e Macros",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Alimentos detectados (separados por vírgula):", style = MaterialTheme.typography.bodySmall)
                    OutlinedTextField(
                        value = foodString,
                        onValueChange = { foodString = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LiliaPrimary,
                            unfocusedBorderColor = LiliaOutlineVariant.copy(alpha = 0.4f)
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = calString,
                            onValueChange = { calString = it },
                            label = { Text("Calorias (kcal)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = protString,
                            onValueChange = { protString = it },
                            label = { Text("Proteínas (g)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = carbString,
                            onValueChange = { carbString = it },
                            label = { Text("Carboidratos (g)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = fatString,
                            onValueChange = { fatString = it },
                            label = { Text("Gorduras (g)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updatedFoods = foodString.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        val updatedCalories = calString.toIntOrNull() ?: result.calories
                        val updatedProtein = protString.toIntOrNull() ?: result.protein
                        val updatedCarbs = carbString.toIntOrNull() ?: result.carbs
                        val updatedFat = fatString.toIntOrNull() ?: result.fat

                        val updatedResult = result.copy(
                            identifiedFoods = updatedFoods,
                            calories = updatedCalories,
                            protein = updatedProtein,
                            carbs = updatedCarbs,
                            fat = updatedFat
                        )
                        onConfirm(updatedResult)
                        showEditDialog = false
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                ) {
                    Text("Salvar Ajustes", color = LiliaOnPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

// Backwards-compatible overload
@Composable
fun MealAnalysisBottomSheet(
    result: MealAnalysisResult,
    mealType: String = "Almoço",
    onConfirm: (MealAnalysisResult) -> Unit,
    onDismiss: () -> Unit
) {
    val uiState = if (!result.isFoodDetected || result.identifiedFoods.isEmpty()) {
        PlateAnalysisUiState.NoFoodDetected(result, mealType)
    } else {
        PlateAnalysisUiState.Success(result, mealType)
    }

    MealAnalysisBottomSheet(
        uiState = uiState,
        mealType = mealType,
        onConfirm = onConfirm,
        onRetry = { /* Retry handled by caller */ },
        onDismiss = onDismiss
    )
}

// 1. Loading View
@Composable
private fun MealAnalysisLoadingView(
    mealType: String,
    stepMessage: String,
    progress: Float,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_scan")
    val scanProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "loading_scan_line"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        // Image Viewfinder Box with Laser Scan HUD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(LiliaBackground)
        ) {
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCAq-KIaazIYB4gEwCsh1diRFqavivtPRc9fLEJ_tqA73YIKsUoPAks_e7-n1tXIXmG3KJvnQ8R1bDZOZd2opsgsFHe_ljpR3X5sKafRi8NA6_yZOg5PbFKfjq4dC-ueBKNRCudyx8QqkdXn3ufUHbuOJq6tIAOs-VXs1Xa5REDWaDUvkrhJIUBieqz4LNgGzSMiW9HP6rViHqlK11OVLdbp9EcpUJjp7cfq4mnbXgRXwb4d06FelW",
                contentDescription = "Foto do Prato",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark/Mint overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            )

            // Scanning laser HUD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(scanProgress)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    LiliaPrimaryFixed,
                                    Color.White,
                                    LiliaPrimaryFixed,
                                    Color.Transparent
                                )
                            )
                        )
                        .shadow(8.dp, spotColor = LiliaPrimaryFixed, ambientColor = LiliaPrimaryFixed)
                )
            }

            // Central Scanning Crosshair Box
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(160.dp)
                    .border(2.dp, LiliaPrimaryFixed.copy(alpha = 0.85f), RoundedCornerShape(16.dp))
                    .background(LiliaPrimaryFixed.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            )

            // Top drag handle & Close
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(48.dp)
                    .height(5.dp)
                    .clip(PillShape)
                    .background(Color.White.copy(alpha = 0.8f))
                    .align(Alignment.TopCenter)
            )

            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(36.dp)
                    .clickable { onDismiss() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancelar",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Loading Content Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = LiliaPrimary,
                    strokeWidth = 2.5.dp
                )
                Text(
                    text = "Analisando seu $mealType...",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    )
                )
            }

            // Progress Bar
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(PillShape),
                    color = LiliaPrimary,
                    trackColor = LiliaMintLight,
                    strokeCap = StrokeCap.Round
                )
            }

            // Reassuring status message card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = LiliaMintLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = LiliaPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stepMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = LiliaPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            Text(
                text = "Nossa IA de visão identifica alimentos e calcula estimativas nutricionais informativas de suporte e aprendizado contínuo.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = LiliaSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// 2. Empathetic No-Food View
@Composable
private fun MealAnalysisNoFoodView(
    result: MealAnalysisResult,
    mealType: String,
    onRetry: () -> Unit,
    onOpenManualEntry: () -> Unit,
    onNavigateToChat: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Image Header with gentle indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(LiliaBackground)
        ) {
            AsyncImage(
                model = result.plateImageUrl.ifBlank {
                    "https://lh3.googleusercontent.com/aida-public/AB6AXuCLApRHLgIIuQorkGQKFpVESEkaU-NAwKYyCZITGMXBkixb2TSo65Vht55d7T2TJKE3q9IvRXjqb3tCfidE0pgqBDjpeETPycYHGa6nmtO8F1rXhiHxeyJRrxJlUHV2hxxpZzpfB8sAOfl-B4jWgwCPzHX7-By8cQaQZODrDDPWN3DwQ_68khtlj__vkMlCdmetVW5ta0QRHR_ivx_Y3JNcRJe9IjQriTglYkYMLN76TCdlaKCxwnN3"
                },
                contentDescription = "Foto capturada",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Warm Amber/Neutral overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.40f))
            )

            // Top drag handle & Close
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(48.dp)
                    .height(5.dp)
                    .clip(PillShape)
                    .background(Color.White.copy(alpha = 0.8f))
                    .align(Alignment.TopCenter)
            )

            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(36.dp)
                    .clickable { onDismiss() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Reassuring Badge
            Surface(
                shape = PillShape,
                color = Color.White.copy(alpha = 0.92f),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = LiliaCarbGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Foto Inconclusiva",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        // Empathetic Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Empathetic Lília Message Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = LiliaMintLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = CircleShape,
                        color = LiliaPrimary.copy(alpha = 0.15f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = LiliaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = result.feedbackTitle.ifBlank { "Não consegui ver os alimentos com clareza" },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = LiliaPrimary,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = result.feedbackParagraph1.ifBlank {
                                "Tudo bem, acontece! Às vezes a iluminação do ambiente, a distância da câmera ou um reflexo no prato dificultam a leitura dos ingredientes."
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = result.feedbackParagraph2.ifBlank {
                                "Não se preocupe: você pode tirar uma nova foto mais de perto e com boa luz, ou me contar o que comeu para registrarmos juntos!"
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // Quick Tips Card for Better Photo
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = LiliaSurfaceContainerLowest,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(18.dp))
                    .shadow(1.dp, RoundedCornerShape(18.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = LiliaCarbGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Dicas para uma análise perfeita:",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                        )
                    }

                    // Tip 1
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = LiliaSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Boa Iluminação: Prefira luz frontal, evitando sombras fortes.",
                            style = MaterialTheme.typography.bodySmall.copy(color = LiliaSecondary)
                        )
                    }

                    // Tip 2
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CenterFocusWeak,
                            contentDescription = null,
                            tint = LiliaSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Ângulo Superior: Mostre o prato inteiro a cerca de 30 cm de distância.",
                            style = MaterialTheme.typography.bodySmall.copy(color = LiliaSecondary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Primary Action: Retry Photo
                Button(
                    onClick = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = PillShape,
                            ambientColor = LiliaPrimary.copy(alpha = 0.2f),
                            spotColor = LiliaPrimary.copy(alpha = 0.3f)
                        )
                        .testTag("modal_retry_scan_button"),
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LiliaPrimary,
                        contentColor = LiliaOnPrimary
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = LiliaOnPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tentar Novamente (Tirar Nova Foto)",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = LiliaOnPrimary
                            )
                        )
                    }
                }

                // Secondary Action: Manual Entry
                OutlinedButton(
                    onClick = onOpenManualEntry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("modal_manual_entry_button"),
                    shape = PillShape,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, LiliaPrimary.copy(alpha = 0.7f))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = LiliaPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Digitar Refeição Manualmente",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = LiliaPrimary
                            )
                        )
                    }
                }

                // Tertiary Action: Dismiss
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                ) {
                    Text(
                        text = "Fechar com Calma",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

// 3. Empathetic Error View
@Composable
private fun MealAnalysisErrorView(
    title: String,
    message: String,
    mealType: String,
    onRetry: () -> Unit,
    onOpenManualEntry: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = LiliaMintLight,
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = LiliaPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = LiliaSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = LiliaOnPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tentar Novamente", color = LiliaOnPrimary, fontWeight = FontWeight.Bold)
            }
        }

        OutlinedButton(
            onClick = onOpenManualEntry,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = PillShape
        ) {
            Text("Registrar Manualmente", color = LiliaPrimary, fontWeight = FontWeight.SemiBold)
        }

        TextButton(onClick = onDismiss) {
            Text("Fechar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// 4. Success View
@Composable
private fun MealAnalysisSuccessView(
    result: MealAnalysisResult,
    mealType: String,
    onConfirm: (MealAnalysisResult) -> Unit,
    onEditClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_transition")
    val scanProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scan_line"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Top Half: Image with Scan HUD & Bounding Boxes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(LiliaBackground)
        ) {
            AsyncImage(
                model = result.plateImageUrl.ifBlank {
                    "https://lh3.googleusercontent.com/aida-public/AB6AXuCAq-KIaazIYB4gEwCsh1diRFqavivtPRc9fLEJ_tqA73YIKsUoPAks_e7-n1tXIXmG3KJvnQ8R1bDZOZd2opsgsFHe_ljpR3X5sKafRi8NA6_yZOg5PbFKfjq4dC-ueBKNRCudyx8QqkdXn3ufUHbuOJq6tIAOs-VXs1Xa5REDWaDUvkrhJIUBieqz4LNgGzSMiW9HP6rViHqlK11OVLdbp9EcpUJjp7cfq4mnbXgRXwb4d06FelW"
                },
                contentDescription = "Foto do Prato",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Soft Green Tint Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LiliaPrimary.copy(alpha = 0.08f))
            )

            // Drag handle pill
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(48.dp)
                    .height(5.dp)
                    .clip(PillShape)
                    .background(Color.White.copy(alpha = 0.8f))
                    .align(Alignment.TopCenter)
            )

            // Animated Scan Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(scanProgress)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.5.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    LiliaPrimaryFixed,
                                    Color.White,
                                    LiliaPrimaryFixed,
                                    Color.Transparent
                                )
                            )
                        )
                        .shadow(6.dp, ambientColor = LiliaPrimaryFixed, spotColor = LiliaPrimaryFixed)
                )
            }

            // AI Food Bounding Boxes
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp, top = 20.dp)
                    .size(width = 150.dp, height = 110.dp)
                    .border(
                        width = 2.dp,
                        color = LiliaPrimaryFixed.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(LiliaPrimaryFixed.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 24.dp, top = 36.dp)
                    .size(width = 130.dp, height = 95.dp)
                    .border(
                        width = 2.dp,
                        color = LiliaPrimaryFixed.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(LiliaPrimaryFixed.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
            )

            // Close Button
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(38.dp)
                    .shadow(4.dp, shape = CircleShape)
                    .clickable { onDismiss() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 2. Bottom Half: Details & Nutrients
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Column {
                Text(
                    text = "Análise Concluída",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Identificamos os seguintes alimentos no seu prato.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = LiliaSecondary,
                        fontSize = 14.sp
                    )
                )
            }

            // Identified Foods (Pills)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                result.identifiedFoods.forEach { food ->
                    Surface(
                        shape = PillShape,
                        color = LiliaMintLight,
                        modifier = Modifier.shadow(1.dp, PillShape, ambientColor = LiliaPrimary.copy(alpha = 0.05f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = LiliaPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = food,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = LiliaPrimary
                                )
                            )
                        }
                    }
                }
            }

            // Nutrients Bento Grid (2x2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Calorias Box
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "CALORIAS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = LiliaSecondary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${result.calories}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = LiliaPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "kcal",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    color = LiliaSecondary
                                ),
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                    }
                }

                // Proteínas Box
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "PROTEÍNAS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = LiliaSecondary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${result.protein}g",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.8f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(PillShape),
                            color = LiliaPrimary,
                            trackColor = LiliaMintLight,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }

            // Row 2 of Nutrients: Carboidratos & Gorduras
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Carboidratos Box
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "CARBOIDRATOS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = LiliaSecondary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${result.carbs}g",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.6f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(PillShape),
                            color = LiliaPrimary,
                            trackColor = LiliaMintLight,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }

                // Gorduras Box
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "GORDURAS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = LiliaSecondary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${result.fat}g",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.35f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(PillShape),
                            color = LiliaPrimary,
                            trackColor = LiliaMintLight,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }

            // AI Feedback Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = LiliaMintLight
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Feedback",
                        tint = LiliaPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = result.feedbackTitle.ifBlank { "Excelente escolha!" },
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = LiliaPrimary,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = result.feedbackMessage.ifBlank {
                                "Um prato muito bem balanceado em proteínas e fibras. Adicionei ao seu diário."
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sticky Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onConfirm(result) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = PillShape,
                            ambientColor = LiliaPrimary.copy(alpha = 0.25f),
                            spotColor = LiliaPrimary.copy(alpha = 0.35f)
                        )
                        .testTag("modal_confirm_meal_button"),
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LiliaPrimary,
                        contentColor = LiliaOnPrimary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddTask,
                            contentDescription = null,
                            tint = LiliaOnPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Confirmar e Registrar no Diário",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = LiliaOnPrimary
                            )
                        )
                    }
                }

                TextButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("modal_edit_meal_button")
                ) {
                    Text(
                        text = "Corrigir Alimentos ou Valores",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = LiliaPrimary
                        )
                    )
                }
            }
        }
    }
}
