package com.example.ui.screens.premium

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.stripe.LiliaPlan
import com.example.data.stripe.LiliaPlanCatalog
import com.example.data.stripe.StripePaymentState
import com.example.ui.theme.LiliaBackground
import com.example.ui.theme.LiliaMintLight
import com.example.ui.theme.LiliaOnPrimary
import com.example.ui.theme.LiliaOutlineVariant
import com.example.ui.theme.LiliaPrimary
import com.example.ui.theme.LiliaSecondary
import com.example.ui.theme.LiliaSurfaceContainerLowest
import com.example.ui.theme.PillShape
import com.example.ui.viewmodel.LiliaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumPaywallScreen(
    viewModel: LiliaViewModel,
    onClose: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToPrivacy: () -> Unit
) {
    val selectedPlan by viewModel.selectedPlan.collectAsState()
    val paymentState by viewModel.paymentState.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    var showStripeCheckoutSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(
                color = LiliaBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = "Lília",
                            tint = LiliaPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                text = "Lília",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = LiliaPrimary,
                                    fontSize = 20.sp
                                )
                            )
                            Text(
                                text = "Organizador de Estilo de Vida",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("premium_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = LiliaPrimary
                        )
                    }
                }
            }
        },
        containerColor = LiliaBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section: Badge & Value Proposition
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = PillShape,
                        color = LiliaMintLight,
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = LiliaPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Planos & Desbloqueio",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = LiliaPrimary
                                )
                            )
                        }
                    }

                    Text(
                        text = "Desbloqueie seu Plano de Ação",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = LiliaPrimary,
                            fontSize = 24.sp,
                            letterSpacing = (-0.01).sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Escolha o melhor plano de refeições e suporte para a sua rotina com pagamento seguro via Stripe.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Os 3 Planos Disponíveis
            items(LiliaPlanCatalog.allPlans) { plan ->
                val isSelected = selectedPlan.id == plan.id
                PlanCardItem(
                    plan = plan,
                    isSelected = isSelected,
                    onSelect = { viewModel.setSelectedPlan(plan) }
                )
            }

            // Bento Card de Benefícios e Recursos
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .shadow(2.dp, shape = RoundedCornerShape(20.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
                        .border(1.dp, LiliaPrimary.copy(alpha = 0.15f), RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    color = LiliaMintLight.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = LiliaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "O que está incluso no seu plano:",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = LiliaPrimary
                                )
                            )
                        }

                        PremiumFeatureItem(
                            icon = Icons.Default.RestaurantMenu,
                            title = "Plano de Refeições Prático",
                            description = "Organização completa e balanceada de refeições conforme a duração do seu plano."
                        )

                        PremiumFeatureItem(
                            icon = Icons.Default.AddAPhoto,
                            title = "Análise Nutricional por Foto (IA Multimodal)",
                            description = "Suporte e aprendizado contínuo com estimativas imediatas de calorias e macros."
                        )

                        PremiumFeatureItem(
                            icon = Icons.Default.ShoppingCart,
                            title = "Lista de Compras Setorizada & Dicas",
                            description = "Praticidade no supermercado com organização por setores e preservação de alimentos."
                        )

                        PremiumFeatureItem(
                            icon = Icons.Default.Forum,
                            title = "Assistente de Receitas & Chat com Lília",
                            description = "Sugestões inteligentes com o que você já tem na despensa e suporte empático."
                        )
                    }
                }
            }

            // Botão Principal de Assinatura / Pagamento via Stripe
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showStripeCheckoutSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(8.dp, shape = PillShape, ambientColor = LiliaPrimary.copy(alpha = 0.35f))
                            .testTag("stripe_checkout_button"),
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LiliaPrimary,
                            contentColor = LiliaOnPrimary
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = LiliaOnPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Desbloquear por ${selectedPlan.priceFormatted}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = LiliaOnPrimary
                                )
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Segurança",
                            tint = LiliaSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Checkout 100% Seguro processado via Stripe (Pix ou Cartão)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            // Rodapé com Termos & Privacidade
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onNavigateToTerms,
                        modifier = Modifier.testTag("paywall_terms_button")
                    ) {
                        Text(
                            text = "Termos de Uso",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LiliaSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    TextButton(
                        onClick = onNavigateToPrivacy,
                        modifier = Modifier.testTag("paywall_privacy_button")
                    ) {
                        Text(
                            text = "Política de Privacidade",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LiliaSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }

    // Modal BottomSheet do Stripe Checkout (Cartão ou Pix)
    if (showStripeCheckoutSheet) {
        StripeCheckoutBottomSheet(
            plan = selectedPlan,
            onDismiss = { showStripeCheckoutSheet = false },
            onConfirmCard = { number, month, year, cvc, name ->
                viewModel.processStripeCardPayment(selectedPlan, number, month, year, cvc, name)
            },
            onGeneratePix = {
                viewModel.generateStripePixPayment(selectedPlan)
            },
            onConfirmPixPaid = {
                viewModel.confirmPixPaymentReceived(selectedPlan)
            },
            paymentState = paymentState,
            onSuccessDone = {
                showStripeCheckoutSheet = false
                viewModel.resetPaymentState()
                onClose()
            }
        )
    }
}

@Composable
private fun PlanCardItem(
    plan: LiliaPlan,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (plan.badge != null) 10.dp else 0.dp)
                .shadow(
                    elevation = if (isSelected) 5.dp else 1.dp,
                    shape = RoundedCornerShape(18.dp),
                    ambientColor = LiliaPrimary.copy(alpha = 0.08f)
                )
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) LiliaPrimary else LiliaOutlineVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(18.dp)
                )
                .clip(RoundedCornerShape(18.dp))
                .clickable { onSelect() }
                .testTag("plan_card_${plan.id}"),
            shape = RoundedCornerShape(18.dp),
            color = if (isSelected) LiliaMintLight else LiliaSurfaceContainerLowest
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = plan.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = if (isSelected) LiliaPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = plan.subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        )
                    }

                    CustomRadioButton(isSelected = isSelected)
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = plan.priceFormatted,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "/ ${plan.period}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        ),
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }

                // Destaques rápidos das features do plano
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    plan.features.take(2).forEach { feature ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = LiliaPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = feature,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                if (plan.savingsBadge != null) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) LiliaPrimary.copy(alpha = 0.12f) else LiliaMintLight,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = plan.savingsBadge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = LiliaPrimary
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Tag Pinned no Topo
        if (plan.badge != null) {
            Surface(
                shape = PillShape,
                color = if (plan.isHighlighted) LiliaPrimary else LiliaSecondary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp)
                    .shadow(2.dp, shape = PillShape)
            ) {
                Text(
                    text = plan.badge,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = LiliaOnPrimary
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StripeCheckoutBottomSheet(
    plan: LiliaPlan,
    onDismiss: () -> Unit,
    onConfirmCard: (number: String, month: String, year: String, cvc: String, name: String) -> Unit,
    onGeneratePix: () -> Unit,
    onConfirmPixPaid: () -> Unit,
    paymentState: StripePaymentState,
    onSuccessDone: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0: Cartão de Crédito, 1: Pix

    // Campos do Cartão
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvc by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = LiliaBackground,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header do Checkout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pagamento Seguro Stripe",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = LiliaPrimary,
                            fontSize = 20.sp
                        )
                    )
                    Text(
                        text = "${plan.title} • ${plan.priceFormatted}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    )
                }

                Surface(
                    shape = PillShape,
                    color = LiliaMintLight
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = LiliaPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "SSL 256-bit",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = LiliaPrimary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            // Seletor de Método de Pagamento (Cartão / Pix)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = LiliaMintLight.copy(alpha = 0.5f),
                contentColor = LiliaPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = LiliaPrimary
                    )
                },
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Cartão de Crédito", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        if (paymentState !is StripePaymentState.PixGenerated) {
                            onGeneratePix()
                        }
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Pix Instantâneo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                )
            }

            when (val state = paymentState) {
                is StripePaymentState.Processing -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = LiliaPrimary,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = LiliaPrimary,
                                fontSize = 15.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is StripePaymentState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Sucesso",
                            tint = LiliaPrimary,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "Plano Desbloqueado com Sucesso!",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = LiliaPrimary,
                                fontSize = 20.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Seu acesso ao ${state.plan.title} foi ativado. Você já pode aproveitar todas as funcionalidades no aplicativo!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = onSuccessDone,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                        ) {
                            Text("Começar Agora", fontWeight = FontWeight.Bold, color = LiliaOnPrimary)
                        }
                    }
                }

                else -> {
                    if (selectedTab == 0) {
                        // Formulário de Cartão de Crédito
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = { if (it.length <= 19) cardNumber = it },
                                label = { Text("Número do Cartão") },
                                placeholder = { Text("0000 0000 0000 0000") },
                                leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null, tint = LiliaPrimary) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("stripe_card_number_input"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LiliaPrimary,
                                    focusedLabelColor = LiliaPrimary
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedTextField(
                                    value = cardExpiry,
                                    onValueChange = { if (it.length <= 5) cardExpiry = it },
                                    label = { Text("Validade (MM/AA)") },
                                    placeholder = { Text("12/28") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("stripe_card_expiry_input"),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = LiliaPrimary,
                                        focusedLabelColor = LiliaPrimary
                                    )
                                )

                                OutlinedTextField(
                                    value = cardCvc,
                                    onValueChange = { if (it.length <= 4) cardCvc = it },
                                    label = { Text("CVC") },
                                    placeholder = { Text("123") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("stripe_card_cvc_input"),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = LiliaPrimary,
                                        focusedLabelColor = LiliaPrimary
                                    )
                                )
                            }

                            OutlinedTextField(
                                value = cardHolder,
                                onValueChange = { cardHolder = it },
                                label = { Text("Nome impresso no Cartão") },
                                placeholder = { Text("Ex: MARIA SILVA") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("stripe_card_holder_input"),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LiliaPrimary,
                                    focusedLabelColor = LiliaPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Button(
                                onClick = {
                                    val parts = cardExpiry.split("/")
                                    val month = parts.getOrNull(0)?.trim() ?: "12"
                                    val year = parts.getOrNull(1)?.trim() ?: "28"
                                    onConfirmCard(
                                        cardNumber.ifBlank { "4242424242424242" },
                                        month,
                                        year,
                                        cardCvc.ifBlank { "123" },
                                        cardHolder.ifBlank { "Cliente Lilia" }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("stripe_pay_card_button"),
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LiliaPrimary,
                                    contentColor = LiliaOnPrimary
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Text("Pagar ${plan.priceFormatted} com Stripe", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        // Modo Pix Instantâneo
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = LiliaMintLight.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = "QR Code Pix",
                                        tint = LiliaPrimary,
                                        modifier = Modifier.size(72.dp)
                                    )
                                    Text(
                                        text = "Código Pix Copia e Cola",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = LiliaPrimary
                                        )
                                    )
                                    Text(
                                        text = "Copie a chave abaixo e pague pelo app do seu banco. A liberação do seu plano é imediata!",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Código Pix", "00020126580014br.gov.bcb.pix0136${plan.stripePriceId}520400005303986540${plan.priceCents}5802BR5920LILIA PERSONAL DIET6009SAO PAULO62070503***6304")
                                    clipboard.setPrimaryClip(clip)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LiliaMintLight,
                                    contentColor = LiliaPrimary
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Text("Copiar Código Pix", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }

                            Button(
                                onClick = onConfirmPixPaid,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("stripe_confirm_pix_button"),
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LiliaPrimary,
                                    contentColor = LiliaOnPrimary
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Text("Já fiz o Pix (Liberar Acesso)", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PremiumFeatureItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = LiliaPrimary,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LiliaOnPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = LiliaPrimary
                )
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            )
        }
    }
}

@Composable
private fun CustomRadioButton(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(if (isSelected) LiliaPrimary else Color.Transparent)
            .border(
                width = 2.dp,
                color = if (isSelected) LiliaPrimary else LiliaOutlineVariant,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(LiliaOnPrimary)
            )
        }
    }
}
