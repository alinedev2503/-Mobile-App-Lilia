package com.example.ui.screens.web3

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.web3.Web3PerkItem
import com.example.data.web3.Web3RewardTransaction
import com.example.ui.theme.LiliaBackground
import com.example.ui.theme.LiliaMintLight
import com.example.ui.theme.LiliaOnPrimary
import com.example.ui.theme.LiliaPrimary
import com.example.ui.theme.LiliaPrimaryContainer
import com.example.ui.theme.LiliaSecondary
import com.example.ui.theme.LiliaSurfaceContainerLowest
import com.example.ui.theme.PillShape
import com.example.ui.viewmodel.LiliaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Web3RewardsScreen(
    viewModel: LiliaViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val walletState by viewModel.web3WalletState.collectAsState()
    val availablePerks = viewModel.web3Perks
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Dashboard & Wallet, 1: Marketplace Resgate, 2: Prova Criptográfica On-Chain
    var selectedPerkForClaim by remember { mutableStateOf<Web3PerkItem?>(null) }
    var claimedSuccessVoucher by remember { mutableStateOf<Pair<Web3PerkItem, String>?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(LiliaMintLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = null,
                                tint = LiliaPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Eat-to-Earn Web3",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            )
                            Text(
                                text = "Gamificação Verificável em Blockchain",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    Surface(
                        shape = PillShape,
                        color = Color(0xFF8247E5).copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF8247E5).copy(alpha = 0.3f)),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF8247E5))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Polygon PoS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF8247E5),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LiliaBackground
                )
            )
        },
        containerColor = LiliaBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = LiliaSurfaceContainerLowest,
                contentColor = LiliaPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = LiliaPrimary,
                        height = 3.dp
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Carteira & Saldo",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Resgatar Perks",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = "Oráculo On-Chain",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
            }

            when (selectedTab) {
                0 -> WalletDashboardTab(
                    walletState = walletState,
                    onCopyAddress = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Wallet Address", walletState.fullWalletAddress))
                        viewModel.showToast("Endereço da carteira copiado! 📋")
                    },
                    onClaimStreakReward = {
                        viewModel.claimDailyStreakWeb3Reward()
                    }
                )
                1 -> PerksMarketplaceTab(
                    walletState = walletState,
                    perks = availablePerks,
                    onSelectPerk = { perk ->
                        if (walletState.tokenBalance >= perk.tokenCost) {
                            selectedPerkForClaim = perk
                        } else {
                            viewModel.showToast("Saldo insuficiente de \$LILIA. Registre mais hábitos saudáveis!")
                        }
                    }
                )
                2 -> OnChainOracleTab(
                    walletState = walletState,
                    onCopyTxHash = { txHash ->
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Transaction Hash", txHash))
                        viewModel.showToast("Hash da transação copiado! 🔗")
                    }
                )
            }
        }
    }

    // Modal de confirmação de resgate
    selectedPerkForClaim?.let { perk ->
        AlertDialog(
            onDismissRequest = { selectedPerkForClaim = null },
            shape = RoundedCornerShape(24.dp),
            containerColor = LiliaSurfaceContainerLowest,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Redeem,
                        contentDescription = null,
                        tint = LiliaPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Confirmar Resgate Web3",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Você está resgatando:",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = perk.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    )
                    Text(
                        text = perk.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Custo em Tokens:", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "${perk.tokenCost.toInt()} \$LILIA",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = LiliaPrimary
                            )
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Saldo restante:", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                        Text(
                            text = "${(walletState.tokenBalance - perk.tokenCost).toInt()} \$LILIA",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                    Text(
                        text = "⚡ A transação emitirá um contrato de resgate e gerará um voucher de parceiro criptografado.",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF8247E5),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val perkToClaim = perk
                        selectedPerkForClaim = null
                        viewModel.redeemWeb3Perk(perkToClaim) { voucher ->
                            claimedSuccessVoucher = Pair(perkToClaim, voucher)
                        }
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                ) {
                    Text("Confirmar Resgate", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPerkForClaim = null }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }

    // Modal de Voucher Resgatado com Sucesso
    claimedSuccessVoucher?.let { (perk, voucher) ->
        AlertDialog(
            onDismissRequest = { claimedSuccessVoucher = null },
            shape = RoundedCornerShape(24.dp),
            containerColor = LiliaSurfaceContainerLowest,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Voucher Desbloqueado! 🎉",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Parabéns! Seus hábitos saudáveis geraram valor real.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = LiliaMintLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LiliaPrimary.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "CÓDIGO DO VOUCHER",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = LiliaPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = voucher,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    color = LiliaPrimary,
                                    letterSpacing = 2.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Válido no checkout do parceiro ${perk.partnerBrand}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Voucher", voucher))
                        viewModel.showToast("Código copiado para a área de transferência! 📋")
                        claimedSuccessVoucher = null
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copiar Código")
                }
            }
        )
    }
}

@Composable
private fun WalletDashboardTab(
    walletState: com.example.data.web3.Web3WalletState,
    onCopyAddress: () -> Unit,
    onClaimStreakReward: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Web3 Token Balance Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, shape = RoundedCornerShape(24.dp), ambientColor = Color(0xFF8247E5).copy(alpha = 0.2f)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF1B6B50),
                                    Color(0xFF14523D),
                                    Color(0xFF3B1E78)
                                )
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Smart Wallet Health-to-Earn",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable { onCopyAddress() }
                                    ) {
                                        Text(
                                            text = walletState.walletAddress,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color.White,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copiar endereço",
                                            tint = Color.White.copy(alpha = 0.8f),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }

                            Surface(
                                shape = PillShape,
                                color = Color(0xFF10B981).copy(alpha = 0.25f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF34D399))
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Online",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFFD1FAE5),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "SALDO DISPONÍVEL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White.copy(alpha = 0.75f),
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "%.0f".format(walletState.tokenBalance),
                                style = MaterialTheme.typography.displaySmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    fontSize = 38.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "\$LILIA",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6EE7B7),
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Mini stats footer inside card
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.12f))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Total Ganho",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                                )
                                Text(
                                    text = "+${walletState.totalEarned.toInt()} \$LILIA",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF6EE7B7), fontWeight = FontWeight.Bold)
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Total Resgatado",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                                )
                                Text(
                                    text = "${walletState.totalClaimed.toInt()} \$LILIA",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFE2E8F0), fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Hackathon Banner: Health-to-Earn Mechanics
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = LiliaSurfaceContainerLowest,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF8247E5).copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF8247E5).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF8247E5),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Como funciona o Eat-to-Earn?",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "A IA da Lília e o Google Health Connect validam seus hábitos (pratos saudáveis, água, passos) e emitem tokens \$LILIA na blockchain Polygon.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }
        }

        // 3. Quick Rewards Claim Actions
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Missões e Recompensas Ativas",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )

                EarningMissionCard(
                    title = "Registrar Refeição com Visão Computacional",
                    rewardText = "+15 \$LILIA por prato saudável",
                    icon = Icons.Default.Diamond,
                    statusText = "Automático no Diário",
                    isAvailable = true,
                    iconTint = LiliaPrimary
                )

                EarningMissionCard(
                    title = "Bater Meta de 8.000 Passos no Health Connect",
                    rewardText = "+25 \$LILIA diários",
                    icon = Icons.Default.DirectionsRun,
                    statusText = "Validado via Oráculo",
                    isAvailable = true,
                    iconTint = Color(0xFF3B82F6)
                )

                EarningMissionCard(
                    title = "Meta de Hidratação (3.000ml)",
                    rewardText = "+10 \$LILIA diários",
                    icon = Icons.Default.WaterDrop,
                    statusText = "Bata a meta no app",
                    isAvailable = true,
                    iconTint = Color(0xFF06B6D4)
                )

                // Streak Button Action
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaMintLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LiliaPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Bônus de Ofensiva Diária",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "+50 \$LILIA a cada 3 dias seguidos",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                                )
                            }
                        }

                        Button(
                            onClick = onClaimStreakReward,
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("Reivindicar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PerksMarketplaceTab(
    walletState: com.example.data.web3.Web3WalletState,
    perks: List<Web3PerkItem>,
    onSelectPerk: (Web3PerkItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Marketplace de Benefícios",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    )
                    Text(
                        text = "Troque seus tokens \$LILIA por cupons e serviços",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                Surface(
                    shape = PillShape,
                    color = LiliaMintLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LiliaPrimary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "Saldo: ${walletState.tokenBalance.toInt()} \$LILIA",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = LiliaPrimary
                        )
                    )
                }
            }
        }

        items(perks) { perk ->
            val canAfford = walletState.tokenBalance >= perk.tokenCost

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LiliaSurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (canAfford) LiliaPrimary.copy(alpha = 0.3f) else Color(0xFFE2E8F0)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        when (perk.iconName) {
                                            "diamond" -> Color(0xFF8247E5).copy(alpha = 0.12f)
                                            "fitness" -> Color(0xFFEF4444).copy(alpha = 0.12f)
                                            "eco" -> Color(0xFF10B981).copy(alpha = 0.12f)
                                            else -> Color(0xFF3B82F6).copy(alpha = 0.12f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (perk.iconName) {
                                        "diamond" -> Icons.Default.Diamond
                                        "fitness" -> Icons.Default.FitnessCenter
                                        "eco" -> Icons.Default.LocalActivity
                                        else -> Icons.Default.MedicalServices
                                    },
                                    contentDescription = null,
                                    tint = when (perk.iconName) {
                                        "diamond" -> Color(0xFF8247E5)
                                        "fitness" -> Color(0xFFEF4444)
                                        "eco" -> Color(0xFF10B981)
                                        else -> Color(0xFF3B82F6)
                                    },
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = perk.title,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Parceiro: ${perk.partnerBrand}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }

                        Surface(
                            shape = PillShape,
                            color = if (canAfford) LiliaMintLight else Color(0xFFF1F5F9)
                        ) {
                            Text(
                                text = "${perk.tokenCost.toInt()} \$LILIA",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (canAfford) LiliaPrimary else Color.Gray
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = perk.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onSelectPerk(perk) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (canAfford) LiliaPrimary else Color(0xFF94A3B8)
                        ),
                        enabled = canAfford
                    ) {
                        Text(
                            text = if (canAfford) "Resgatar Voucher" else "Saldo Insuficiente",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnChainOracleTab(
    walletState: com.example.data.web3.Web3WalletState,
    onCopyTxHash: (String) -> Unit
) {
    val txs = walletState.transactions.ifEmpty { com.example.data.web3.Web3RewardsManager.getInitialTransactions() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Oracle Info Header
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF1E293B),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Lília Decentralized Oracle",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Text(
                            text = "Chain ID: ${walletState.chainId}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF94A3B8),
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Cada ação saudável validada pelo Gemini 2.5 Flash ou Google Health Connect é assinada criptograficamente via SHA-256 e transmitida à rede Polygon PoS com timestamp imutável.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }
        }

        item {
            Text(
                text = "Histórico de Transações On-Chain (Auditável)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }

        items(txs) { tx ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LiliaSurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (tx.isEarning) LiliaMintLight else Color(0xFFFEF2F2)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (tx.isEarning) Icons.AutoMirrored.Filled.TrendingUp else Icons.Default.Redeem,
                                    contentDescription = null,
                                    tint = if (tx.isEarning) LiliaPrimary else Color(0xFFEF4444),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = tx.actionTitle,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = SimpleDateFormat("dd/MM/yyyy • HH:mm", Locale.getDefault()).format(Date(tx.timestamp)),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Text(
                            text = "${if (tx.isEarning) "+" else "-"}${tx.tokenAmount.toInt()} \$LILIA",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (tx.isEarning) LiliaPrimary else Color(0xFFEF4444)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TX HASH (BLOCK #${tx.blockNumber})",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "${tx.txHash.take(14)}...${tx.txHash.takeLast(8)}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = Color(0xFF8247E5)
                                )
                            )
                        }

                        IconButton(
                            onClick = { onCopyTxHash(tx.txHash) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copiar Hash",
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EarningMissionCard(
    title: String,
    rewardText: String,
    icon: ImageVector,
    statusText: String,
    isAvailable: Boolean,
    iconTint: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LiliaSurfaceContainerLowest),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = rewardText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = LiliaPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Surface(
                shape = PillShape,
                color = Color(0xFFF1F5F9)
            ) {
                Text(
                    text = statusText,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}
