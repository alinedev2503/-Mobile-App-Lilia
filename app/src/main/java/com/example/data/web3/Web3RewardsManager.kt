package com.example.data.web3

import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Modelo de dados para transações de recompensas Web3 / Eat-to-Earn.
 */
data class Web3RewardTransaction(
    val id: String,
    val actionTitle: String,
    val category: String, // "Refeição Saudável", "Hidratação", "Health Connect Passos", "Streak Ofensiva", "Resgate"
    val tokenAmount: Double,
    val tokenSymbol: String = "LILIA",
    val txHash: String,
    val blockNumber: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Confirmed (Polygon PoS)",
    val isEarning: Boolean = true
)

/**
 * Modelo para itens do catálogo de resgate / Marketplace Eat-to-Earn.
 */
data class Web3PerkItem(
    val id: String,
    val title: String,
    val description: String,
    val tokenCost: Double,
    val category: String,
    val partnerBrand: String,
    val voucherCode: String,
    val iconName: String
)

/**
 * Estado da carteira Web3 do usuário.
 */
data class Web3WalletState(
    val walletAddress: String = "0x71C...B489",
    val fullWalletAddress: String = "0x71C83956F145D3B892eF24F70b86a877e698B489",
    val networkName: String = "Polygon Network (PoS)",
    val chainId: Int = 137,
    val tokenSymbol: String = "LILIA",
    val tokenBalance: Double = 340.0,
    val totalEarned: Double = 520.0,
    val totalClaimed: Double = 180.0,
    val isConnected: Boolean = true,
    val transactions: List<Web3RewardTransaction> = emptyList()
)

/**
 * Gerenciador Web3 e simulador de oráculo criptográfico descentralizado para o Hackathon.
 */
object Web3RewardsManager {

    private val secureRandom = SecureRandom()

    fun generateTxHash(payload: String, nonce: Long = secureRandom.nextLong()): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest((payload + nonce).toByteArray())
        return "0x" + hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun getAvailablePerks(): List<Web3PerkItem> = listOf(
        Web3PerkItem(
            id = "perk_1",
            title = "1 Mês Lília Pro / Premium",
            description = "Acesso irrestrito a fotos ilimitadas e anamnese avançada.",
            tokenCost = 150.0,
            category = "Assinatura",
            partnerBrand = "Lília AI",
            voucherCode = "LILIA-PRO-WEB3-FREE",
            iconName = "diamond"
        ),
        Web3PerkItem(
            id = "perk_2",
            title = "Cupom R$ 50 Suplementos & Whey",
            description = "Desconto exclusivo em lojas parceiras de nutrição esportiva.",
            tokenCost = 200.0,
            category = "Nutrição",
            partnerBrand = "Growth & Max Nutri",
            voucherCode = "HEALTH2EARN50",
            iconName = "fitness"
        ),
        Web3PerkItem(
            id = "perk_3",
            title = "Voucher Hortifruti Orgânico",
            description = "R$ 35 de desconto em feiras e delivery de orgânicos.",
            tokenCost = 120.0,
            category = "Alimentação",
            partnerBrand = "Feira Direta & Cesta Verde",
            voucherCode = "ORGANICO-EAT-35",
            iconName = "eco"
        ),
        Web3PerkItem(
            id = "perk_4",
            title = "Teleconsulta com Nutricionista",
            description = "Sessão online de 45 minutos com profissional credenciado.",
            tokenCost = 450.0,
            category = "Saúde",
            partnerBrand = "Rede NutriConecta",
            voucherCode = "NUTRI-CONSULT-LILIA",
            iconName = "medical"
        )
    )

    fun getInitialTransactions(): List<Web3RewardTransaction> = listOf(
        Web3RewardTransaction(
            id = "tx_01",
            actionTitle = "Refeição Almoço Funcional Registrada",
            category = "Refeição Saudável",
            tokenAmount = 15.0,
            txHash = "0x8f2d9c1048b234ea76a89c2049d56711928374a2b1093847291a02938472bb19",
            blockNumber = 59842104L,
            timestamp = System.currentTimeMillis() - 3600000L * 4,
            isEarning = true
        ),
        Web3RewardTransaction(
            id = "tx_02",
            actionTitle = "Meta de 8.000 Passos (Health Connect)",
            category = "Health Connect Passos",
            tokenAmount = 25.0,
            txHash = "0x3e1a90bc472891fa3049281a029384617a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d",
            blockNumber = 59841890L,
            timestamp = System.currentTimeMillis() - 3600000L * 8,
            isEarning = true
        ),
        Web3RewardTransaction(
            id = "tx_03",
            actionTitle = "Meta de Hidratação 3.000ml Batida",
            category = "Hidratação",
            tokenAmount = 10.0,
            txHash = "0xaa42918bc2049d816a72b903847291a029384617a2b3c4d5e6f7a8b9c0d1e2f3",
            blockNumber = 59840212L,
            timestamp = System.currentTimeMillis() - 3600000L * 24,
            isEarning = true
        ),
        Web3RewardTransaction(
            id = "tx_04",
            actionTitle = "Ofensiva de 3 Dias Consecutivos",
            category = "Streak Ofensiva",
            tokenAmount = 50.0,
            txHash = "0x7bc192039485761a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a",
            blockNumber = 59838900L,
            timestamp = System.currentTimeMillis() - 3600000L * 48,
            isEarning = true
        )
    )
}
