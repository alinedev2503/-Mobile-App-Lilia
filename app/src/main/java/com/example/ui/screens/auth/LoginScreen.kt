package com.example.ui.screens.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.ui.theme.LiliaBackground
import com.example.ui.theme.LiliaBorderLight
import com.example.ui.theme.LiliaMintLight
import com.example.ui.theme.LiliaOnPrimary
import com.example.ui.theme.LiliaOutlineVariant
import com.example.ui.theme.LiliaPrimary
import com.example.ui.theme.LiliaPrimaryFixed
import com.example.ui.theme.LiliaSecondary
import com.example.ui.theme.LiliaSecondaryContainer
import com.example.ui.theme.LiliaSurfaceContainerLowest
import com.example.ui.theme.LiliaSurfaceDim
import com.example.ui.theme.PillShape

private const val GOOGLE_LOGO_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuDj3RO2hn9I44f4PIfqNa6uTny3BMugJRXF2iGxWVsxuRYCT_pBLM6vo3aiC64ByU1uaHEHRrs3ztGbg3J9558k1wI8nkC74QWLof_4hHb3vmxa4Fyi9evpr9n6bzAVEnR9sThyTJr-1CyKkDMYTnJuXW5Te6OEgQfrmfa0zLc3aayddWFXv5h2SOKzumKPXKxSOFHogZLaJq8t6rnRbmiK8piIJRFrs02RLyBeqlcKswlXGNTDn7oY"

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToPrivacy: () -> Unit
) {
    var email by remember { mutableStateOf("ana.carolina@exemplo.com") }
    var password by remember { mutableStateOf("••••••••") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Modals state
    var showSignUpModal by remember { mutableStateOf(false) }
    var showForgotPasswordModal by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LiliaBackground)
    ) {
        // Subtle Polka-Dot Canvas Pattern matching the design system
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 40.dp.toPx()
            val dotRadius = 2.dp.toPx()
            val dotColor = Color(0xFFD5E3FC).copy(alpha = 0.5f)

            var x = step / 2
            while (x < size.width) {
                var y = step / 2
                while (y < size.height) {
                    drawCircle(
                        color = dotColor,
                        radius = dotRadius,
                        center = Offset(x, y)
                    )
                    y += step
                }
                x += step
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Branding Header
            Text(
                text = "Lília Personal Diet",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = LiliaPrimary,
                    fontSize = 28.sp,
                    letterSpacing = (-0.01).sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("login_app_title")
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Bem-vindo(a) de volta ao seu controle de saúde.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Glass Card Container
            Box(
                modifier = Modifier
                    .widthIn(max = 440.dp)
                    .fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = LiliaPrimary.copy(alpha = 0.06f),
                            spotColor = LiliaPrimary.copy(alpha = 0.08f)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.92f)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Decorative Soft Glow Element in upper right
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .align(Alignment.TopEnd)
                                .blur(28.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            LiliaPrimaryFixed.copy(alpha = 0.45f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            // Email Field
                            Text(
                                text = "Email",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 13.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_email_input"),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email",
                                        tint = LiliaOutlineVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                placeholder = {
                                    Text(
                                        "seu@email.com",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                        )
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LiliaPrimary,
                                    unfocusedBorderColor = Color(0xFFF1F5F9),
                                    focusedContainerColor = LiliaSurfaceContainerLowest,
                                    unfocusedContainerColor = LiliaSurfaceContainerLowest
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Password Field
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Senha",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "Esqueceu a senha?",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = LiliaPrimary,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp
                                    ),
                                    modifier = Modifier
                                        .clickable { showForgotPasswordModal = true }
                                        .testTag("login_forgot_password_link")
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_password_input"),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Senha",
                                        tint = LiliaOutlineVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Alternar visibilidade",
                                            tint = LiliaOutlineVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                },
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LiliaPrimary,
                                    unfocusedBorderColor = Color(0xFFF1F5F9),
                                    focusedContainerColor = LiliaSurfaceContainerLowest,
                                    unfocusedContainerColor = LiliaSurfaceContainerLowest
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                            )

                            Spacer(modifier = Modifier.height(22.dp))

                            // Entrar Primary Button (Sage Green, Pill Shape)
                            Button(
                                onClick = onLoginSuccess,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = PillShape,
                                        ambientColor = LiliaPrimary.copy(alpha = 0.25f),
                                        spotColor = LiliaPrimary.copy(alpha = 0.35f)
                                    )
                                    .testTag("login_submit_button"),
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LiliaPrimary,
                                    contentColor = LiliaOnPrimary
                                )
                            ) {
                                Text(
                                    text = "Entrar",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        color = LiliaOnPrimary
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Or Divider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFF1F5F9))
                                Text(
                                    text = "  ou  ",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = LiliaSecondary,
                                        fontSize = 13.sp
                                    )
                                )
                                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFF1F5F9))
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Entrar com Google Button (White background, 1px border, Google logo)
                            Button(
                                onClick = onLoginSuccess,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .border(1.dp, Color(0xFFF1F5F9), PillShape)
                                    .testTag("login_google_button"),
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LiliaSurfaceContainerLowest,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                AsyncImage(
                                    model = GOOGLE_LOGO_URL,
                                    contentDescription = "Google Logo",
                                    modifier = Modifier.size(20.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Entrar com Google",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Criar Conta Button (Soft Mint #E8F5E9 background, Sage Green text)
                            Button(
                                onClick = { showSignUpModal = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("login_create_account_button"),
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LiliaMintLight,
                                    contentColor = LiliaPrimary
                                )
                            ) {
                                Text(
                                    text = "Criar Conta",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = LiliaPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Links to Terms and Privacy Policy
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onNavigateToTerms) {
                    Text(
                        text = "Termos de Uso",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = LiliaPrimary,
                            fontSize = 13.sp
                        )
                    )
                }
                Text(" • ", color = LiliaSecondary, fontSize = 13.sp)
                TextButton(onClick = onNavigateToPrivacy) {
                    Text(
                        text = "Política de Privacidade",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = LiliaPrimary,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ==========================================
        // Modal: Criar Conta (Sign Up Modal Dialog)
        // ==========================================
        if (showSignUpModal) {
            SignUpModal(
                onDismiss = { showSignUpModal = false },
                onSignUpSuccess = {
                    showSignUpModal = false
                    onLoginSuccess()
                },
                onNavigateToTerms = {
                    showSignUpModal = false
                    onNavigateToTerms()
                }
            )
        }

        // ==========================================
        // Modal: Esqueceu a Senha (Password Reset Modal)
        // ==========================================
        if (showForgotPasswordModal) {
            ForgotPasswordModal(
                initialEmail = email,
                onDismiss = { showForgotPasswordModal = false }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignUpModal(
    onDismiss: () -> Unit,
    onSignUpSuccess: () -> Unit,
    onNavigateToTerms: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf("Equilíbrio & Longevidade") }
    var acceptedTerms by remember { mutableStateOf(true) }

    val goals = listOf(
        "Emagrecimento Saudável",
        "Hipertrofia & Massa Magra",
        "Equilíbrio & Longevidade",
        "Nutrição Anti-inflamatória"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 440.dp)
                    .fillMaxWidth()
                    .shadow(24.dp, shape = RoundedCornerShape(24.dp), ambientColor = LiliaPrimary.copy(alpha = 0.1f))
                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.96f)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Soft green decorative glow in top-right
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopEnd)
                            .blur(24.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        LiliaPrimaryFixed.copy(alpha = 0.45f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Header with close button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Criar Nova Conta",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = LiliaPrimary,
                                        fontSize = 22.sp
                                    )
                                )
                                Text(
                                    text = "Inicie sua jornada personalizada com a Lília",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Fechar",
                                    tint = LiliaSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Nome
                        Text(
                            text = "Seu Nome",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = LiliaOutlineVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            placeholder = { Text("Ex: Ana Carolina") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LiliaPrimary,
                                unfocusedBorderColor = Color(0xFFF1F5F9),
                                focusedContainerColor = LiliaSurfaceContainerLowest,
                                unfocusedContainerColor = LiliaSurfaceContainerLowest
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Email
                        Text(
                            text = "Email",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = LiliaOutlineVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            placeholder = { Text("seu@email.com") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LiliaPrimary,
                                unfocusedBorderColor = Color(0xFFF1F5F9),
                                focusedContainerColor = LiliaSurfaceContainerLowest,
                                unfocusedContainerColor = LiliaSurfaceContainerLowest
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Senha
                        Text(
                            text = "Senha de Acesso",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = LiliaOutlineVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Alternar visibilidade",
                                        tint = LiliaOutlineVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            placeholder = { Text("Mínimo de 8 caracteres") },
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LiliaPrimary,
                                unfocusedBorderColor = Color(0xFFF1F5F9),
                                focusedContainerColor = LiliaSurfaceContainerLowest,
                                unfocusedContainerColor = LiliaSurfaceContainerLowest
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Objetivo Nutricional
                        Text(
                            text = "Seu Objetivo Principal",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            goals.forEach { goal ->
                                val isSelected = selectedGoal == goal
                                Surface(
                                    shape = PillShape,
                                    color = if (isSelected) LiliaSecondaryContainer else Color(0xFFF8F9FF),
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = 1.dp,
                                        color = if (isSelected) LiliaPrimary else Color(0xFFEFF4FF)
                                    ),
                                    modifier = Modifier.clickable { selectedGoal = goal }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = LiliaPrimary,
                                                modifier = Modifier
                                                    .size(14.dp)
                                                    .padding(end = 4.dp)
                                            )
                                        }
                                        Text(
                                            text = goal,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) LiliaPrimary else LiliaSecondary
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Termos Checkbox
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = acceptedTerms,
                                onCheckedChange = { acceptedTerms = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = LiliaPrimary,
                                    checkmarkColor = LiliaOnPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Li e concordo com os Termos e Política",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.clickable { onNavigateToTerms() }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Submit Button
                        Button(
                            onClick = onSignUpSuccess,
                            enabled = acceptedTerms && email.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .shadow(8.dp, shape = PillShape, ambientColor = LiliaPrimary.copy(alpha = 0.25f)),
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LiliaPrimary,
                                contentColor = LiliaOnPrimary,
                                disabledContainerColor = LiliaSecondaryContainer.copy(alpha = 0.6f),
                                disabledContentColor = LiliaSecondary
                            )
                        ) {
                            Text(
                                text = "Concluir Cadastro",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordModal(
    initialEmail: String,
    onDismiss: () -> Unit
) {
    var recoveryEmail by remember { mutableStateOf(initialEmail) }
    var emailSent by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .shadow(24.dp, shape = RoundedCornerShape(24.dp), ambientColor = LiliaPrimary.copy(alpha = 0.1f))
                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.96f)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Soft green decorative glow in top-right
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopEnd)
                            .blur(24.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        LiliaPrimaryFixed.copy(alpha = 0.45f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recuperar Senha",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = LiliaPrimary,
                                    fontSize = 22.sp
                                )
                            )
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Fechar",
                                    tint = LiliaSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (!emailSent) {
                            Text(
                                text = "Informe o email cadastrado para enviarmos as instruções de redefinição segura de senha.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 20.sp,
                                    fontSize = 14.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "Email Cadastrado",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = recoveryEmail,
                                onValueChange = { recoveryEmail = it },
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = LiliaOutlineVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                placeholder = { Text("seu@email.com") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LiliaPrimary,
                                    unfocusedBorderColor = Color(0xFFF1F5F9),
                                    focusedContainerColor = LiliaSurfaceContainerLowest,
                                    unfocusedContainerColor = LiliaSurfaceContainerLowest
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { emailSent = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .shadow(8.dp, shape = PillShape, ambientColor = LiliaPrimary.copy(alpha = 0.25f)),
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LiliaPrimary,
                                    contentColor = LiliaOnPrimary
                                )
                            ) {
                                Text(
                                    text = "Enviar Link de Recuperação",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp
                                    )
                                )
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = LiliaMintLight,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = LiliaPrimary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Link Enviado!",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = LiliaPrimary
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Verifique sua caixa de entrada em $recoveryEmail para redefinir sua senha com segurança.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 18.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LiliaMintLight,
                                    contentColor = LiliaPrimary
                                )
                            ) {
                                Text(
                                    text = "Entendido, Voltar ao Login",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = LiliaPrimary
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
