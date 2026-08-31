package com.example.ui.screens.profile

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.LiliaTopAppBar
import com.example.ui.theme.LiliaBackground
import com.example.ui.theme.LiliaMintLight
import com.example.ui.theme.LiliaOnPrimary
import com.example.ui.theme.LiliaOutlineVariant
import com.example.ui.theme.LiliaPrimary
import com.example.ui.theme.LiliaSecondary
import com.example.ui.theme.LiliaSurfaceContainerLowest
import com.example.ui.theme.PillShape
import com.example.ui.viewmodel.LiliaViewModel

@Composable
fun ProfileScreen(
    viewModel: LiliaViewModel,
    onNavigateToTerms: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onLogout: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()

    var showEditGoalDialog by remember { mutableStateOf(false) }
    var showEditPrefsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LiliaTopAppBar(
                title = "Lília AI Assistant",
                onSettingsClick = { viewModel.showToast("Configurações rápidas abertas") }
            )
        },
        containerColor = LiliaBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Profile Header Section (Centered Avatar & Name)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(112.dp)
                            .shadow(6.dp, shape = CircleShape, ambientColor = LiliaPrimary.copy(alpha = 0.08f))
                            .border(3.dp, LiliaSurfaceContainerLowest, CircleShape)
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDLoDOvmY_kX0z8B-kU2M-e3-5JCoTJbYSQQciviQG150MCyCtsNjkSPYjG4CSDLYmvjwV3eZKyOq8C_PsLpnJc0ZfB1sdtG8NGpdhvBXkwEM_i8qY_QZrogn2SB9eC2KnuDdKpXasADu9tI4gK3RaS5rdhv0OQY6WGLvhhDELtHQzuwTb1SDPskMCxeTMps7tcfoJ7krxlGugKMho6bjORGcE1HkvIlrbOLje2FXxGVT5My_tuUF2t",
                            contentDescription = "Avatar de ${profile.name}",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 24.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = profile.email,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = LiliaSecondary,
                            fontSize = 14.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = PillShape,
                        color = if (profile.isPremium) LiliaMintLight else Color(0xFFF1F5F9),
                        modifier = Modifier
                            .clickable { onNavigateToPremium() }
                            .testTag("profile_plan_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (profile.isPremium) Icons.Default.WorkspacePremium else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (profile.isPremium) LiliaPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = if (profile.isPremium) profile.planName else "Plano Gratuito • Ver Planos",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (profile.isPremium) LiliaPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }

            // 2. Primary Goal Card (Card 1)
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = LiliaMintLight,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = "Objetivo",
                                        tint = LiliaPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "OBJETIVO ATUAL",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        letterSpacing = 0.8.sp,
                                        color = LiliaSecondary
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = profile.currentGoal,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = profile.goalDescription,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = LiliaSecondary,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        }

                        // Edit Goal Button
                        Surface(
                            shape = PillShape,
                            color = LiliaMintLight,
                            modifier = Modifier
                                .clickable { showEditGoalDialog = true }
                                .testTag("profile_edit_goal_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = LiliaPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Editar Meta",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = LiliaPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 3. IMC Card (Card 2)
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "IMC (ÍNDICE DE MASSA CORPORAL)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.8.sp,
                                    color = LiliaSecondary
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.Scale,
                                contentDescription = "IMC",
                                tint = LiliaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${profile.imc}",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "Normal",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = LiliaSecondary,
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

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
            }

            // 4. TMB Card (Card 3)
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TMB (TAXA METABÓLICA BASAL)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.8.sp,
                                    color = LiliaSecondary
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "TMB",
                                tint = LiliaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "${profile.tmb}",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "kcal / dia",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LiliaSecondary,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }

            // 5. Preferências Alimentares Card (Card 4)
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PREFERÊNCIAS ALIMENTARES",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.8.sp,
                                    color = LiliaSecondary
                                )
                            )
                            IconButton(
                                onClick = { showEditPrefsDialog = true },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar preferências",
                                    tint = LiliaPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val prefs = profile.dietaryPrefs.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            prefs.forEach { pref ->
                                Surface(
                                    shape = PillShape,
                                    color = LiliaMintLight
                                ) {
                                    Text(
                                        text = pref,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = LiliaPrimary
                                        ),
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. Settings List Section
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaSurfaceContainerLowest
                ) {
                    Column {
                        Text(
                            text = "Configurações da Conta",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)
                        )

                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                        ProfileModernOptionItem(
                            icon = Icons.Default.Notifications,
                            title = "Notificações",
                            onClick = { viewModel.showToast("Notificações da Lília ativas!") }
                        )

                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                        ProfileModernOptionItem(
                            icon = Icons.Default.ManageAccounts,
                            title = "Conta e Segurança",
                            onClick = onNavigateToPrivacy
                        )

                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                        ProfileModernOptionItem(
                            icon = Icons.Default.Diamond,
                            title = if (profile.isPremium) "Gerenciar Plano (${profile.planName})" else "Desbloquear Planos (Stripe)",
                            highlight = !profile.isPremium,
                            onClick = onNavigateToPremium
                        )

                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                        ProfileModernOptionItem(
                            icon = Icons.Default.Gavel,
                            title = "Termos de Uso",
                            onClick = onNavigateToTerms
                        )

                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                        ProfileModernOptionItem(
                            icon = Icons.Default.Logout,
                            title = "Sair da Conta",
                            isDestructive = true,
                            onClick = onLogout
                        )
                    }
                }
            }
        }
    }

    if (showEditGoalDialog) {
        var goalText by remember { mutableStateOf(profile.currentGoal) }
        var descText by remember { mutableStateOf(profile.goalDescription) }

        AlertDialog(
            onDismissRequest = { showEditGoalDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = LiliaSurfaceContainerLowest,
            title = {
                Text(
                    text = "Editar Meta Atual",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = goalText,
                        onValueChange = { goalText = it },
                        label = { Text("Objetivo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LiliaPrimary,
                            unfocusedBorderColor = LiliaOutlineVariant.copy(alpha = 0.4f)
                        )
                    )
                    OutlinedTextField(
                        value = descText,
                        onValueChange = { descText = it },
                        label = { Text("Detalhes do plano") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
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
                        viewModel.updateGoal(goalText, descText)
                        showEditGoalDialog = false
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                ) {
                    Text("Salvar", color = LiliaOnPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditGoalDialog = false }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    if (showEditPrefsDialog) {
        var prefsText by remember { mutableStateOf(profile.dietaryPrefs) }

        AlertDialog(
            onDismissRequest = { showEditPrefsDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = LiliaSurfaceContainerLowest,
            title = {
                Text(
                    text = "Preferências e Intolerâncias",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Separadas por vírgula:", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    OutlinedTextField(
                        value = prefsText,
                        onValueChange = { prefsText = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
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
                        viewModel.updateDietaryPrefs(prefsText)
                        showEditPrefsDialog = false
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                ) {
                    Text("Salvar", color = LiliaOnPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditPrefsDialog = false }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
private fun ProfileModernOptionItem(
    icon: ImageVector,
    title: String,
    highlight: Boolean = false,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = CircleShape,
                color = when {
                    highlight -> Color(0xFFFFF8E1)
                    isDestructive -> Color(0xFFFFEBEE)
                    else -> LiliaMintLight
                },
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = when {
                            highlight -> Color(0xFFFFA000)
                            isDestructive -> Color(0xFFD32F2F)
                            else -> LiliaPrimary
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = if (isDestructive) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = LiliaSecondary.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}
