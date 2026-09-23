package com.example.data.stripe

data class LiliaPlan(
    val id: String,
    val stripeProductId: String,
    val stripePriceId: String,
    val title: String,
    val subtitle: String,
    val priceFormatted: String,
    val priceCents: Long,
    val period: String,
    val badge: String? = null,
    val isHighlighted: Boolean = false,
    val durationDays: Int,
    val photoAnalysisLimit: Int,
    val features: List<String>,
    val savingsBadge: String? = null
)

object LiliaPlanCatalog {
    val PLAN_START = LiliaPlan(
        id = "plan_start_7d",
        stripeProductId = "prod_lilia_start_7d",
        stripePriceId = "price_1UFgw2E3bAgRQ7BQV9hDRrMK",
        title = "Plano Start (7 Dias)",
        subtitle = "Experimentação rápida & foco",
        priceFormatted = "R$ 19,90",
        priceCents = 1990,
        period = "pagamento único • 7 dias",
        badge = "Entrada",
        isHighlighted = false,
        durationDays = 7,
        photoAnalysisLimit = 10,
        features = listOf(
            "Plano de ação prático de 7 dias de refeições",
            "Até 10 análises nutricionais por foto de pratos",
            "Lista de compras básica da semana setorizada",
            "Chat com a Lília para dúvidas pontuais"
        ),
        savingsBadge = null
    )

    val PLAN_RESET = LiliaPlan(
        id = "plan_reset_15d",
        stripeProductId = "prod_lilia_reset_15d",
        stripePriceId = "price_1UFgw2E3bAgRQ7BQ4bzTlJUF",
        title = "Desafio Reset (15 Dias)",
        subtitle = "Equilíbrio & criação de hábitos",
        priceFormatted = "R$ 29,90",
        priceCents = 2990,
        period = "pagamento único • 15 dias",
        badge = "Mais Equilibrado",
        isHighlighted = false,
        durationDays = 15,
        photoAnalysisLimit = 30,
        features = listOf(
            "Plano de ação prático de 15 dias em 2 fases",
            "Até 30 análises nutricionais por foto com alertas",
            "Assistente de receitas inteligentes com o que tem em casa",
            "Lista de compras inteligente + dicas de conservação",
            "Chat interativo contínuo com a Lília"
        ),
        savingsBadge = "Economize 25%"
    )

    val PLAN_TRANSFORMATION = LiliaPlan(
        id = "plan_transform_30d",
        stripeProductId = "prod_lilia_transform_30d",
        stripePriceId = "price_1UFgw2E3bAgRQ7BQgWTnnX0p",
        title = "Transformação 360° (30 Dias)",
        subtitle = "O plano completo definitivo",
        priceFormatted = "R$ 39,90",
        priceCents = 3990,
        period = "30 dias de acesso completo",
        badge = "Mais Popular",
        isHighlighted = true,
        durationDays = 30,
        photoAnalysisLimit = 9999,
        features = listOf(
            "Plano de ação prático de 30 dias com metas semanais",
            "Análise nutricional por foto ILIMITADA",
            "Gerador ilimitado de receitas por IA com a despensa",
            "Lista de compras setorizada dinâmica do mês",
            "Mapeamento comportamental & Gráficos de hábitos",
            "Suporte prioritário 24/7 com a Lília"
        ),
        savingsBadge = "Melhor Custo-Benefício"
    )

    val allPlans: List<LiliaPlan> = listOf(
        PLAN_START,
        PLAN_RESET,
        PLAN_TRANSFORMATION
    )
}

sealed interface StripePaymentState {
    object Idle : StripePaymentState
    data class Processing(val message: String = "Processando pagamento seguro com a Stripe...") : StripePaymentState
    data class PixGenerated(
        val plan: LiliaPlan,
        val qrCodePayload: String,
        val copyPasteCode: String,
        val expiresMinutes: Int = 30
    ) : StripePaymentState
    data class Success(val plan: LiliaPlan, val transactionId: String) : StripePaymentState
    data class Error(val message: String) : StripePaymentState
}
