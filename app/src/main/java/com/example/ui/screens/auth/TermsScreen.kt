package com.example.ui.screens.auth

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiliaTopAppBar
import com.example.ui.theme.LiliaBackground
import com.example.ui.theme.LiliaMintLight
import com.example.ui.theme.LiliaOnPrimary
import com.example.ui.theme.LiliaOutlineVariant
import com.example.ui.theme.LiliaPrimary
import com.example.ui.theme.LiliaSecondary
import com.example.ui.theme.LiliaSurfaceContainerLowest
import com.example.ui.theme.PillShape

@Composable
fun TermsScreen(
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onNavigateToPrivacy: () -> Unit
) {
    Scaffold(
        topBar = {
            LiliaTopAppBar(
                title = "Lília Personal Diet",
                onBackClick = onDecline
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, ambientColor = LiliaPrimary.copy(alpha = 0.08f)),
                color = LiliaSurfaceContainerLowest
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = PillShape,
                        color = Color(0xFFEFF4FF),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clickable { onDecline() }
                            .testTag("terms_decline_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Recusar",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    Button(
                        onClick = onAccept,
                        modifier = Modifier
                            .weight(1.6f)
                            .height(48.dp)
                            .shadow(4.dp, shape = PillShape, ambientColor = LiliaPrimary.copy(alpha = 0.3f))
                            .testTag("terms_accept_button"),
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
                            Text(
                                text = "Aceitar e Continuar",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = LiliaOnPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = LiliaOnPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
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
            contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    Text(
                        text = "Termos de Uso & Políticas",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = LiliaPrimary,
                            fontSize = 26.sp,
                            letterSpacing = (-0.01).sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Última atualização: Fevereiro de 2025 • Lília - Organizador de Estilo de Vida",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            // Section 1: Aceitação dos Termos
            item {
                TermsModernSectionCard(
                    icon = Icons.Default.VerifiedUser,
                    title = "1. Aceitação dos Termos"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Bem-vindo ao Lília! Ao acessar e utilizar este aplicativo, você concorda em cumprir e ficar vinculado aos presentes Termos de Uso. Estes termos constituem um contrato legalmente vinculativo entre você e a plataforma Lília.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        )
                        Text(
                            text = "Caso não concorde com qualquer disposição aqui estabelecida, solicitamos que não utilize o aplicativo nem forneça seus dados para as autoavaliações de hábitos.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }
            }

            // Section 2: Uso do Serviço & Mapeamento de Hábitos
            item {
                TermsModernSectionCard(
                    icon = Icons.Default.RestaurantMenu,
                    title = "2. Uso do Serviço & Mapeamento de Hábitos"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "O aplicativo Lília concede a você uma licença pessoal, não exclusiva, intransferível e revogável para utilizar o organizador de estilo de vida, planejamento de refeições, sugestões de receitas inteligentes e estimativas nutricionais por foto para fins estritamente pessoais, informativos e não comerciais.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        )

                        Column(
                            modifier = Modifier.padding(start = 6.dp, top = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BulletItem("Você se compromete a fornecer informações verdadeiras e atualizadas para a autoavaliação comportamental e de hábitos.")
                            BulletItem("Você é responsável por manter a confidencialidade das credenciais de acesso à sua conta.")
                            BulletItem("É estritamente vedada a engenharia reversa, raspagem de dados ou reprodução comercial dos sistemas da Lília.")
                        }
                    }
                }
            }

            // Section 3: Aviso de Isenção de Responsabilidade Médica (Regulatory Disclaimer)
            item {
                TermsModernSectionCard(
                    icon = Icons.Default.Warning,
                    title = "3. Aviso de Responsabilidade & Isenção Médica"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val medicalWarningText = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                                append("Importante: Leia com atenção!\n")
                            }
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                append("O Lília é um Organizador de Estilo de Vida e bem-estar baseado em inteligência artificial, projetado exclusivamente para fornecer suporte informativo, organização de rotinas e autoconhecimento de hábitos. ")
                            }
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = LiliaPrimary)) {
                                append("Ele NÃO constitui prescrição médica, dietoterapia clínica individualizada ou diagnóstico de saúde.")
                            }
                        }

                        Text(
                            text = medicalWarningText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        )

                        Text(
                            text = "As estimativas calóricas, macronutrientes aproximados, fórmulas referenciais (como TMB/Harris-Benedict e IMC) e análises de fotos são ferramentas de suporte e aprendizado contínuo. Elas não substituem consultas, diagnósticos ou prescrições individualizadas de médicos ou nutricionistas habilitados. Consulte sempre um profissional de saúde qualificado antes de mudanças bruscas na alimentação, especialmente em casos de patologias pré-existentes, gestação, lactação ou uso de medicamentos.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }
            }

            // Section 4: Política de Assinaturas, Cancelamento & Reembolso
            item {
                TermsModernSectionCard(
                    icon = Icons.Default.CurrencyExchange,
                    title = "4. Assinaturas, Reembolso & Garantia (7 Dias)"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Queremos que você tenha a melhor experiência possível. Nossas assinaturas funcionam com transparência total processadas via Stripe:",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        )

                        Column(
                            modifier = Modifier.padding(start = 6.dp, top = 2.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BulletItem("Garantia de 7 Dias: Se você assinou qualquer plano e não estiver 100% satisfeito, poderá solicitar reembolso total no prazo de até 7 dias corridos a partir da data de contratação.")
                            BulletItem("Cancelamento Descomplicado: Você pode cancelar sua assinatura a qualquer momento. Seu acesso continuará ativo até o encerramento do ciclo mensal ou anual já quitado.")
                            BulletItem("Processamento Rápido: O estorno é realizado diretamente no mesmo meio de pagamento original utilizado (cartão de crédito ou Pix).")
                        }
                    }
                }
            }

            // Section 5: Privacidade & Proteção de Dados (LGPD)
            item {
                TermsModernSectionCard(
                    icon = Icons.Default.Lock,
                    title = "5. Privacidade e Proteção de Dados"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "A segurança dos seus dados clínicos e alimentares é inegociável. Cumprimos integralmente a Lei Geral de Proteção de Dados (LGPD - Lei nº 13.709/2018). Seus dados são criptografados e JAMAIS comercializados com terceiros.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        )

                        // Callout Box
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFEFF4FF),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                val privacyText = buildAnnotatedString {
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                                        append("Canais Oficiais: ")
                                    }
                                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                        append("Para solicitar reembolsos, esclarecer dúvidas ou exercer direitos da LGPD, fale conosco pelo chat ou no email ")
                                    }
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = LiliaPrimary)) {
                                        append("falecomnutrion@gmail.com")
                                    }
                                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                        append(". Acesse nossa ")
                                    }
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = LiliaPrimary)) {
                                        append("Política de Privacidade completa.")
                                    }
                                }

                                Text(
                                    text = privacyText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 13.sp,
                                        lineHeight = 19.sp
                                    ),
                                    modifier = Modifier.clickable { onNavigateToPrivacy() }
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
private fun TermsModernSectionCard(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
            .border(1.dp, LiliaOutlineVariant.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = LiliaSurfaceContainerLowest
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = LiliaPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            content()
        }
    }
}

@Composable
private fun BulletItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp, end = 10.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(LiliaPrimary)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        )
    }
}
