package com.example.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MealEntity
import com.example.ui.components.LiliaTopAppBar
import com.example.ui.components.MacroRingCard
import com.example.ui.theme.LiliaBackground
import com.example.ui.theme.LiliaCarbGold
import com.example.ui.theme.LiliaFatCoral
import com.example.ui.theme.LiliaMintLight
import com.example.ui.theme.LiliaOnPrimary
import com.example.ui.theme.LiliaOnSecondaryContainer
import com.example.ui.theme.LiliaOutlineVariant
import com.example.ui.theme.LiliaPrimary
import com.example.ui.theme.LiliaPrimaryFixed
import com.example.ui.theme.LiliaPrimaryFixedDim
import com.example.ui.theme.LiliaSecondary
import com.example.ui.theme.LiliaSecondaryContainer
import com.example.ui.theme.LiliaSurfaceContainerHigh
import com.example.ui.theme.LiliaSurfaceContainerHighest
import com.example.ui.theme.LiliaSurfaceContainerLowest
import com.example.ui.theme.PillShape
import com.example.ui.viewmodel.LiliaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: LiliaViewModel,
    onNavigateToRecipes: () -> Unit,
    onNavigateToShoppingList: () -> Unit,
    onTriggerScanMeal: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    val meals by viewModel.meals.collectAsState()

    val totalCalories = meals.sumOf { it.calories }
    val totalProtein = meals.sumOf { it.protein }
    val totalCarbs = meals.sumOf { it.carbs }
    val totalFat = meals.sumOf { it.fat }

    val calProgress = (totalCalories.toFloat() / profile.targetCalories.toFloat()).coerceIn(0f, 1f)
    val proteinProgress = (totalProtein.toFloat() / profile.targetProtein.toFloat()).coerceIn(0f, 1f)
    val carbsProgress = (totalCarbs.toFloat() / profile.targetCarbs.toFloat()).coerceIn(0f, 1f)
    val fatProgress = (totalFat.toFloat() / profile.targetFat.toFloat()).coerceIn(0f, 1f)

    val todayFormatted = SimpleDateFormat("d 'de' MMMM, EEEE", Locale("pt", "BR")).format(Date())
        .replaceFirstChar { it.uppercase() }

    var todayCompleted3L by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LiliaTopAppBar(
                title = "Lília",
                onSettingsClick = onNavigateToSettings
            )
        },
        containerColor = LiliaBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Date & Streak Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Hoje",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 28.sp,
                                letterSpacing = (-0.01).sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = todayFormatted,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        )
                    }

                    // Streak Pill Badge
                    Surface(
                        shape = PillShape,
                        color = LiliaSurfaceContainerLowest,
                        modifier = Modifier
                            .shadow(2.dp, shape = PillShape, ambientColor = LiliaPrimary.copy(alpha = 0.05f))
                            .border(
                                width = 1.dp,
                                color = LiliaOutlineVariant.copy(alpha = 0.3f),
                                shape = PillShape
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Ofensiva",
                                tint = LiliaPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${profile.streakDays} dias de ofensiva",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = LiliaPrimary
                                )
                            )
                        }
                    }
                }
            }

            // 2. KPI Macro Grid (Bento Style 2x2)
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Row 1: Calorias & Proteínas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MacroRingCard(
                            title = "Calorias (kcal)",
                            currentValue = if (totalCalories >= 1000) String.format(Locale.US, "%.1fk", totalCalories / 1000f) else "$totalCalories",
                            targetValue = "Meta: ${profile.targetCalories}",
                            progress = calProgress,
                            ringColor = LiliaPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        MacroRingCard(
                            title = "Proteínas",
                            currentValue = "${totalProtein}g",
                            targetValue = "Meta: ${profile.targetProtein}g",
                            progress = proteinProgress,
                            ringColor = LiliaPrimaryFixedDim,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 2: Carboidratos & Gorduras
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MacroRingCard(
                            title = "Carboidratos",
                            currentValue = "${totalCarbs}g",
                            targetValue = "Meta: ${profile.targetCarbs}g",
                            progress = carbsProgress,
                            ringColor = LiliaCarbGold,
                            modifier = Modifier.weight(1f)
                        )
                        MacroRingCard(
                            title = "Gorduras",
                            currentValue = "${totalFat}g",
                            targetValue = "Meta: ${profile.targetFat}g",
                            progress = fatProgress,
                            ringColor = LiliaFatCoral,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 3. Core Action Button: Analisar Prato
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { onTriggerScanMeal("Almoço") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = PillShape,
                                ambientColor = LiliaPrimary.copy(alpha = 0.25f),
                                spotColor = LiliaPrimary.copy(alpha = 0.35f)
                            )
                            .testTag("dashboard_scan_plate_button"),
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
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Câmera",
                                modifier = Modifier.size(20.dp),
                                tint = LiliaOnPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Analisar Prato",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = LiliaOnPrimary
                                )
                            )
                        }
                    }
                }
            }

            // 4. Diário Alimentar (Daily Timeline)
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Diário Alimentar",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 22.sp
                        )
                    )
                }
            }

            // Meal items list or registered meals
            if (meals.isNotEmpty()) {
                items(meals, key = { it.id }) { meal ->
                    MealItemCard(
                        meal = meal,
                        onDelete = { viewModel.deleteMeal(meal) }
                    )
                }
            }

            // If lunch or other meals are not logged yet, show empty state slot
            val hasLunch = meals.any { it.title.contains("Almoço", ignoreCase = true) }
            if (!hasLunch) {
                item {
                    EmptyMealSlotCard(
                        mealType = "Almoço",
                        onClick = { onTriggerScanMeal("Almoço") }
                    )
                }
            }

            // 5. Desafios Section (Challenges & Water Tracker)
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Desafios",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 22.sp
                        )
                    )

                    // Challenge Card (Desafio da Semana)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.05f))
                            .border(
                                width = 1.dp,
                                color = LiliaOutlineVariant.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        color = LiliaSecondaryContainer
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            // Soft decorative blurred circle
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .align(Alignment.TopEnd)
                                    .blur(26.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                LiliaPrimary.copy(alpha = 0.15f),
                                                Color.Transparent
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                // Header: Trophy & Title
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = "Troféu",
                                        tint = LiliaPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Desafio da Semana",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = LiliaOnSecondaryContainer,
                                                fontSize = 16.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Beber 3L de água por 5 dias seguidos",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = LiliaOnSecondaryContainer.copy(alpha = 0.85f),
                                                fontSize = 13.sp
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Progress Bar
                                val waterProgress = (profile.waterIntakeMl.toFloat() / profile.waterGoalMl.toFloat()).coerceIn(0f, 1f)
                                val challengeDaysProgress = if (todayCompleted3L || waterProgress >= 1f) 0.6f else 0.4f

                                LinearProgressIndicator(
                                    progress = { challengeDaysProgress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(PillShape),
                                    color = LiliaPrimary,
                                    trackColor = LiliaSurfaceContainerLowest.copy(alpha = 0.5f),
                                    strokeCap = StrokeCap.Round
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (todayCompleted3L || waterProgress >= 1f) "Dia 3" else "Dia 2",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = LiliaOnSecondaryContainer.copy(alpha = 0.7f)
                                        )
                                    )
                                    Text(
                                        text = "Dia 5",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = LiliaOnSecondaryContainer.copy(alpha = 0.7f)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Interactive Checklist
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Segunda-feira (Checked)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = LiliaPrimary,
                                            modifier = Modifier.size(20.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = LiliaOnPrimary,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "Segunda-feira",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 13.sp,
                                                color = LiliaOnSecondaryContainer.copy(alpha = 0.7f),
                                                textDecoration = TextDecoration.LineThrough
                                            )
                                        )
                                    }

                                    // Terça-feira (Checked)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = LiliaPrimary,
                                            modifier = Modifier.size(20.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = LiliaOnPrimary,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "Terça-feira",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 13.sp,
                                                color = LiliaOnSecondaryContainer.copy(alpha = 0.7f),
                                                textDecoration = TextDecoration.LineThrough
                                            )
                                        )
                                    }

                                    // Hoje (3L) - Toggleable
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                todayCompleted3L = !todayCompleted3L
                                                if (todayCompleted3L) {
                                                    viewModel.logWater(profile.waterGoalMl - profile.waterIntakeMl)
                                                }
                                            },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            if (todayCompleted3L || waterProgress >= 1f) {
                                                Surface(
                                                    shape = CircleShape,
                                                    color = LiliaPrimary,
                                                    modifier = Modifier.size(20.dp)
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Icon(
                                                            imageVector = Icons.Default.Check,
                                                            contentDescription = null,
                                                            tint = LiliaOnPrimary,
                                                            modifier = Modifier.size(13.dp)
                                                        )
                                                    }
                                                }
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .size(20.dp)
                                                        .border(2.dp, LiliaOutlineVariant, CircleShape)
                                                )
                                            }
                                            Text(
                                                text = "Hoje (3L)",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = LiliaOnSecondaryContainer
                                                )
                                            )
                                        }

                                        Text(
                                            text = "${profile.waterIntakeMl} ml / ${profile.waterGoalMl} ml",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 11.sp,
                                                color = LiliaOnSecondaryContainer.copy(alpha = 0.8f)
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Water quick-log buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { viewModel.logWater(250) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(36.dp)
                                            .testTag("water_add_250"),
                                        shape = PillShape,
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.White.copy(alpha = 0.7f),
                                            contentColor = LiliaPrimary
                                        ),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, LiliaOutlineVariant.copy(alpha = 0.3f))
                                    ) {
                                        Text("+250ml", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.sp))
                                    }
                                    OutlinedButton(
                                        onClick = { viewModel.logWater(500) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(36.dp)
                                            .testTag("water_add_500"),
                                        shape = PillShape,
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.White.copy(alpha = 0.7f),
                                            contentColor = LiliaPrimary
                                        ),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, LiliaOutlineVariant.copy(alpha = 0.3f))
                                    ) {
                                        Text("+500ml", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.sp))
                                    }
                                    Button(
                                        onClick = { viewModel.logWater(1000) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(36.dp)
                                            .testTag("water_add_1000"),
                                        shape = PillShape,
                                        colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                                    ) {
                                        Text("+1L", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, color = LiliaOnPrimary, fontSize = 11.sp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 6. Quick Access Navigation (Receitas e Lista de Compras)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                            .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .clickable { onNavigateToRecipes() }
                            .testTag("dashboard_recipes_shortcut"),
                        shape = RoundedCornerShape(16.dp),
                        color = LiliaSurfaceContainerLowest
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = "Receitas",
                                tint = LiliaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Receitas da Lília",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "Ideias leves",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = LiliaSecondary
                                    )
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                            .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .clickable { onNavigateToShoppingList() }
                            .testTag("dashboard_shopping_shortcut"),
                        shape = RoundedCornerShape(16.dp),
                        color = LiliaSurfaceContainerLowest
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Compras",
                                tint = LiliaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Lista da Semana",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "Compras saudáveis",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = LiliaSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MealItemCard(
    meal: MealEntity,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
            .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = LiliaSurfaceContainerLowest
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food Image or Fallback
            if (meal.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = meal.imageUrl,
                    contentDescription = meal.title,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LiliaMintLight,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = meal.title,
                            tint = LiliaPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = meal.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = meal.time.ifBlank { "08:30" },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = meal.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Macronutrient pill tags
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Calories Tag
                    Surface(
                        shape = PillShape,
                        color = LiliaSurfaceContainerHigh
                    ) {
                        Text(
                            text = "${meal.calories} kcal",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Protein Tag
                    Surface(
                        shape = PillShape,
                        color = LiliaSurfaceContainerHigh
                    ) {
                        Text(
                            text = "${meal.protein}g Prot",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LiliaPrimary
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Carbs Tag
                    Surface(
                        shape = PillShape,
                        color = LiliaSurfaceContainerHigh
                    ) {
                        Text(
                            text = "${meal.carbs}g Carb",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFC7A044)
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover",
                    tint = LiliaSecondary.copy(alpha = 0.5f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyMealSlotCard(
    mealType: String,
    onClick: () -> Unit
) {
    val outlineVariantColor = LiliaOutlineVariant.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LiliaBackground)
            .drawBehind {
                val stroke = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                )
                drawRoundRect(
                    color = outlineVariantColor,
                    style = stroke,
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
            .clickable { onClick() }
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = LiliaSurfaceContainerHighest,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = mealType,
                        tint = LiliaPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = mealType,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Toque no botão de câmera para tirar foto do seu almoço e calcular macros automaticamente!",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}
