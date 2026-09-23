package com.example.ui.screens.insights

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiliaTopAppBar
import com.example.ui.theme.LiliaBackground
import com.example.ui.theme.LiliaCarbGold
import com.example.ui.theme.LiliaFatCoral
import com.example.ui.theme.LiliaMintLight
import com.example.ui.theme.LiliaOnPrimary
import com.example.ui.theme.LiliaOnSecondaryContainer
import com.example.ui.theme.LiliaOutlineVariant
import com.example.ui.theme.LiliaPrimary
import com.example.ui.theme.LiliaPrimaryFixedDim
import com.example.ui.theme.LiliaSecondary
import com.example.ui.theme.LiliaSecondaryContainer
import com.example.ui.theme.LiliaSurfaceContainerHigh
import com.example.ui.theme.LiliaSurfaceContainerLowest
import com.example.ui.theme.PillShape
import com.example.ui.viewmodel.LiliaViewModel
import java.util.Locale

import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Sync
import androidx.compose.ui.platform.LocalContext
import com.example.data.export.NutritionReportExporter

@Composable
fun InsightsScreen(
    viewModel: LiliaViewModel,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val profile by viewModel.userProfile.collectAsState()
    var showWeightDialog by remember { mutableStateOf(false) }
    var selectedPeriodIndex by remember { mutableIntStateOf(0) }
    val periodOptions = listOf("Esta Semana", "Último Mês", "3 Meses")

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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Header: Title & Period Selector + PDF Export Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Evolução",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 28.sp,
                                letterSpacing = (-0.01).sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Hábitos & Métricas",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        )
                    }

                    // Period selector chip
                    Surface(
                        shape = PillShape,
                        color = LiliaSurfaceContainerLowest,
                        modifier = Modifier
                            .shadow(2.dp, shape = PillShape, ambientColor = LiliaPrimary.copy(alpha = 0.05f))
                            .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.3f), PillShape)
                            .clickable {
                                selectedPeriodIndex = (selectedPeriodIndex + 1) % periodOptions.size
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = periodOptions[selectedPeriodIndex],
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = LiliaPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Mudar período",
                                tint = LiliaPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // 1.5. PDF Report Export Action Banner
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.05f))
                        .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaMintLight
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = LiliaPrimary,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = "PDF Report",
                                        tint = LiliaOnPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Relatório Nutricional em PDF (v1.1)",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = LiliaPrimary
                                    )
                                )
                                Text(
                                    text = "Exporte gráficos, fotos e histórico clínico para seu nutricionista",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = LiliaSecondary
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.exportNutritionPdf { generatedPdf ->
                                    NutritionReportExporter.sharePdf(context, generatedPdf)
                                }
                            },
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary),
                            modifier = Modifier.testTag("export_pdf_button")
                        ) {
                            Text(
                                "Exportar",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = LiliaOnPrimary,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }

            // 1.8. Google Health Connect Live Sync Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFE8F5E9),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.DirectionsRun,
                                            contentDescription = "Health Connect",
                                            tint = Color(0xFF2E7D32),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Google Health Connect",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    Text(
                                        text = if (profile.isHealthConnectSynced) "Sincronizado: ${profile.healthConnectLastSync}" else "Não conectado",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = LiliaSecondary
                                        )
                                    )
                                }
                            }

                            // Sync Button
                            Surface(
                                shape = PillShape,
                                color = LiliaMintLight,
                                modifier = Modifier
                                    .clickable { viewModel.syncWithHealthConnect() }
                                    .testTag("health_connect_sync_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Sync,
                                        contentDescription = "Sincronizar",
                                        tint = LiliaPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Sincronizar",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = LiliaPrimary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Steps Metric
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = LiliaBackground
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Passos Hoje",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${profile.dailySteps}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = LiliaPrimary,
                                            fontSize = 18.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val progress = (profile.dailySteps.toFloat() / profile.stepsGoal.toFloat()).coerceIn(0f, 1f)
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(PillShape),
                                        color = LiliaPrimary,
                                        trackColor = LiliaOutlineVariant.copy(alpha = 0.3f),
                                        strokeCap = StrokeCap.Round
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Meta: ${profile.stepsGoal}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 9.5.sp,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    )
                                }
                            }

                            // Calories Burned Metric
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = LiliaBackground
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Queima Ativa",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${profile.activeCaloriesBurned} kcal",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE65100),
                                            fontSize = 18.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { (profile.activeCaloriesBurned / 500f).coerceIn(0f, 1f) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(PillShape),
                                        color = Color(0xFFE65100),
                                        trackColor = LiliaOutlineVariant.copy(alpha = 0.3f),
                                        strokeCap = StrokeCap.Round
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Meta: 500 kcal",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 9.5.sp,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Weight Evolution Card with Canvas Line Graph
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = LiliaMintLight,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Scale,
                                            contentDescription = "Peso",
                                            tint = LiliaPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Peso Corporal",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    Text(
                                        text = "Meta: ${profile.targetWeight} kg",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 12.sp,
                                            color = LiliaSecondary
                                        )
                                    )
                                }
                            }

                            // Delta Badge
                            Surface(
                                shape = PillShape,
                                color = LiliaSecondaryContainer
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingDown,
                                        contentDescription = "Queda",
                                        tint = LiliaPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "-3.5 kg",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = LiliaPrimary
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Current weight big number
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "${profile.currentWeight}",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = LiliaPrimary,
                                    fontSize = 34.sp
                                )
                            )
                            Text(
                                text = "kg atuais",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = LiliaSecondary,
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Polished Canvas Chart with gradient under the line
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(LiliaBackground)
                                .padding(12.dp)
                        ) {
                            val points = listOf(68.0f, 67.2f, 66.4f, 65.3f, 64.8f, 64.5f)
                            val minVal = 63.5f
                            val maxVal = 68.5f
                            val stepX = size.width / (points.size - 1)

                            val path = Path()
                            val fillPath = Path()

                            points.forEachIndexed { index, value ->
                                val x = index * stepX
                                val normalizedY = (value - minVal) / (maxVal - minVal)
                                val y = size.height - (normalizedY * size.height)
                                if (index == 0) {
                                    path.moveTo(x, y)
                                    fillPath.moveTo(x, size.height)
                                    fillPath.lineTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                    fillPath.lineTo(x, y)
                                }
                            }

                            fillPath.lineTo(size.width, size.height)
                            fillPath.close()

                            // Draw shaded gradient underneath
                            drawPath(
                                path = fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        LiliaPrimary.copy(alpha = 0.18f),
                                        Color.Transparent
                                    )
                                )
                            )

                            // Draw Stroke
                            drawPath(
                                path = path,
                                color = LiliaPrimary,
                                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                            )

                            // Draw points
                            points.forEachIndexed { index, value ->
                                val x = index * stepX
                                val normalizedY = (value - minVal) / (maxVal - minVal)
                                val y = size.height - (normalizedY * size.height)

                                drawCircle(
                                    color = Color.White,
                                    radius = 5.dp.toPx(),
                                    center = Offset(x, y)
                                )
                                drawCircle(
                                    color = LiliaPrimary,
                                    radius = 3.5.dp.toPx(),
                                    center = Offset(x, y)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showWeightDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("insights_log_weight_button"),
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LiliaMintLight,
                                contentColor = LiliaPrimary
                            )
                        ) {
                            Text(
                                text = "Atualizar Peso de Hoje",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = LiliaPrimary
                                )
                            )
                        }
                    }
                }
            }

            // 3. Adherence & Hydration Bento (2 Cards side by side)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1: Adesão Calórica
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                            .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        color = LiliaSurfaceContainerLowest
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Adesão à Dieta",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = LiliaPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "94%",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = LiliaPrimary,
                                    fontSize = 24.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = { 0.94f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(PillShape),
                                color = LiliaPrimary,
                                trackColor = LiliaOutlineVariant.copy(alpha = 0.25f),
                                strokeCap = StrokeCap.Round
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "6 de 7 dias na meta",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            )
                        }
                    }

                    // Card 2: Meta de Hidratação
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                            .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        color = LiliaSurfaceContainerLowest
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Meta de Água",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = LiliaPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "88%",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = LiliaPrimary,
                                    fontSize = 24.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = { 0.88f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(PillShape),
                                color = LiliaPrimary,
                                trackColor = LiliaOutlineVariant.copy(alpha = 0.25f),
                                strokeCap = StrokeCap.Round
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Média de 2.6L/dia",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            )
                        }
                    }
                }
            }

            // 4. Média de Macronutrientes da Semana
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Distribuição Nutricional Semanal",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 15.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Proteína
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = LiliaBackground
                            ) {
                                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Proteína",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "115g/dia",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = LiliaPrimary
                                        )
                                    )
                                }
                            }

                            // Carboidratos
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = LiliaBackground
                            ) {
                                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Carboidratos",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "180g/dia",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Color(0xFFC7A044)
                                        )
                                    )
                                }
                            }

                            // Gorduras
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = LiliaBackground
                            ) {
                                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Gorduras",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "48g/dia",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = LiliaFatCoral
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. AI Diagnostic Card from Lília
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.05f))
                        .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSecondaryContainer
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Blurred soft glow in the corner
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .align(Alignment.TopEnd)
                                .blur(28.dp)
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

                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = LiliaPrimary,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = LiliaOnPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Diagnóstico da Semana por Lília",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LiliaOnSecondaryContainer
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "“Parabéns, ${profile.name.split(" ").firstOrNull() ?: "Ana"}! Sua constância com o café da manhã proteico e os 3L de água ajudou a estabilizar sua energia ao longo de todo o dia. Você teve um déficit leve e super sustentável. Mantenha essa leveza: cada dia é um tijolinho em direção ao seu bem-estar!”",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = LiliaOnSecondaryContainer,
                                    fontSize = 13.sp,
                                    lineHeight = 21.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    if (showWeightDialog) {
        var newWeightInput by remember { mutableStateOf(profile.currentWeight.toString()) }

        AlertDialog(
            onDismissRequest = { showWeightDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = LiliaSurfaceContainerLowest,
            title = {
                Text(
                    "Registrar Novo Peso",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Informe seu peso aferido hoje (em kg):",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    OutlinedTextField(
                        value = newWeightInput,
                        onValueChange = { newWeightInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LiliaPrimary,
                            unfocusedBorderColor = LiliaOutlineVariant.copy(alpha = 0.4f)
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val weight = newWeightInput.toFloatOrNull()
                        if (weight != null) {
                            viewModel.updateGoal(profile.currentGoal, profile.goalDescription)
                            showWeightDialog = false
                        }
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                ) {
                    Text("Salvar", color = LiliaOnPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWeightDialog = false }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}
