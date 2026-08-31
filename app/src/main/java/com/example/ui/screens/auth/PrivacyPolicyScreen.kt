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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.LiliaSecondaryContainer
import com.example.ui.theme.LiliaSurfaceContainerLowest
import com.example.ui.theme.PillShape

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit,
    onManageDataClick: () -> Unit
) {
    Scaffold(
        topBar = {
            LiliaTopAppBar(
                title = "Lília Personal Diet",
                onBackClick = onBackClick
            )
        },
        containerColor = LiliaBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Política de Privacidade",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = LiliaPrimary,
                            fontSize = 26.sp,
                            letterSpacing = (-0.01).sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Data da última atualização: 06 de Fevereiro de 2025\nA sua privacidade é fundamental para nós. Esta política explica de forma clara e transparente como coletamos, usamos, protegemos e tratamos seus dados pessoais e de saúde de acordo com a LGPD.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Card 1: Quais Informações Coletamos?
            item {
                PrivacySectionCard(
                    icon = Icons.Default.Dataset,
                    title = "1. Informações que Coletamos"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Para oferecer planos nutricionais de alta precisão e adaptados à sua rotina, coletamos os seguintes tipos de informações:",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )
                        )

                        Column(
                            modifier = Modifier.padding(start = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PolicyBullet("Informações de Registro: Nome completo, e-mail, telefone e data de nascimento.")
                            PolicyBullet("Dados de Saúde e Anamnese: Respostas ao questionário nutricional, histórico de saúde, alergias, intolerâncias alimentares, medidas antropométricas (peso, altura, circunferências de cintura e quadril para cálculo de IMC e RCQ), rotina de sono e exercícios.")
                            PolicyBullet("Fotos e Imagens: Fotos de refeições e rótulos de alimentos enviadas voluntariamente para análise nutricional por visão computacional.")
                            PolicyBullet("Informações de Pagamento: Dados de cartão de crédito e transações são processados e tokenizados diretamente pelo Stripe em ambiente com certificação PCI-DSS nível 1. Não armazenamos seus dados bancários sensíveis.")
                            PolicyBullet("Dados de Dispositivo e Uso: Modelo do aparelho, sistema operacional e interações no aplicativo para aprimoramento de estabilidade.")
                        }
                    }
                }
            }

            // Card 2: Como Usamos Suas Informações & Inteligência Artificial
            item {
                PrivacySectionCard(
                    icon = Icons.Default.Psychology,
                    title = "2. Uso das Informações & IA Lília"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "A Lília Personal Diet utiliza modelos avançados de inteligência artificial fundamentados na literatura científica da nutrição (fórmulas de Harris-Benedict, recomendações da OMS e dietoterapia clássica) para:",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )
                        )

                        Column(
                            modifier = Modifier.padding(start = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PolicyBullet("Calcular sua Taxa Metabólica Basal (GEB) e Necessidade Energética Total (TEE).")
                            PolicyBullet("Montar planos alimentares personalizados de 30 dias com 3 variações por refeição.")
                            PolicyBullet("Sugerir receitas inteligentes aproveitando os ingredientes que você já tem em casa.")
                            PolicyBullet("Gerar sua lista de compras semanal organizada por categorias de supermercado.")
                            PolicyBullet("Oferecer suporte empático contínuo 24h sem gerar culpas ou ansiedade alimentar.")
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Visual banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(LiliaMintLight)
                        ) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuD53m1bkA0-TSwpNWbXORnyUrFvTwnThEeEtSutijY0Z8asO-3lFv800TwJdeQQIPvxKV_w01mXlIKLeF5ALY5axzGlDsVZrb02XFD9bdJiBDH283gNLWHKluOR0ld8EvZm4p4nK0rg5Opu8qk_Mi6DqkvNFDngSHtblsDsXkQgLfltnwWf1sf1i1F1j9Xf-h0Rksd0Qz6oQ3jtQv0A5wpIKQWwjE_pZ2x9u1QX1zuotuuPyNkd3a3m",
                                contentDescription = "Nutrição Inteligente e Segura",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            // Card 3: Compartilhamento e Não-Venda de Dados
            item {
                PrivacySectionCard(
                    icon = Icons.Default.VpnLock,
                    title = "3. Compartilhamento e Proteção Total"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Compromisso de Não-Venda: Seus dados pessoais e de saúde NUNCA serão vendidos, alugados ou comercializados a terceiros sob nenhuma circunstância.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = "O compartilhamento estrito ocorre apenas com provedores de infraestrutura seguros e essenciais à operação (serviços de nuvem criptografados e gateway Stripe) ou por estrita determinação judicial e cumprimento da legislação brasileira.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )
                        )
                    }
                }
            }

            // Card 4: Segurança das Informações
            item {
                PrivacySectionCard(
                    icon = Icons.Default.Security,
                    title = "4. Medidas de Segurança"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PolicyBullet("Criptografia: Criptografia AES-256 em repouso e protocolos TLS 1.3 em todas as comunicações de rede.")
                        PolicyBullet("Controles de Acesso: Acesso estritamente restrito e autenticado a sistemas que gerenciam informações de usuários.")
                        PolicyBullet("Auditorias Periódicas: Revisões constantes de código e conformidade com as diretrizes da ANPD e LGPD.")
                    }
                }
            }

            // Card 5: Proteção de Crianças e Menores de 14 Anos
            item {
                PrivacySectionCard(
                    icon = Icons.Default.ChildCare,
                    title = "5. Crianças e Adolescentes"
                ) {
                    Text(
                        text = "O Lília Personal Diet não é destinado a menores de 14 anos. Não coletamos intencionalmente dados de crianças. Caso você seja responsável legal e identifique que um menor forneceu dados sem consentimento, entre em contato imediatamente para exclusão completa dos registros.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    )
                }
            }

            // Card 6: Seus Direitos (LGPD) - Dark Green Primary Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(16.dp),
                    color = LiliaPrimary
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "Direitos",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Text(
                            text = "6. Seus Direitos como Titular (LGPD)",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp,
                                color = Color.White
                            )
                        )

                        Text(
                            text = "Você tem direito total a: Acesso aos dados, retificação de informações incompletas, anonimização, bloqueio ou eliminação definitiva dos seus dados a qualquer momento.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.92f),
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Button(
                            onClick = onManageDataClick,
                            modifier = Modifier
                                .height(46.dp)
                                .shadow(2.dp, shape = PillShape)
                                .testTag("privacy_manage_button"),
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = LiliaPrimary
                            )
                        ) {
                            Text(
                                text = "Gerenciar Meus Dados",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = LiliaPrimary
                                )
                            )
                        }
                    }
                }
            }

            // Card 7: Garantia de Reembolso & Atendimento
            item {
                PrivacySectionCard(
                    icon = Icons.Default.CurrencyExchange,
                    title = "7. Reembolso & Canal de Contato"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Garantia incondicional de 7 dias para assinaturas. Para solicitar reembolso ou exercer seus direitos de privacidade, envie uma mensagem pelo chat do app ou pelo e-mail:",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )
                        )
                        Text(
                            text = "falecomnutrion@gmail.com",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = LiliaPrimary,
                                fontSize = 15.sp
                            )
                        )
                    }
                }
            }

            // Footer Note
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(
                        color = LiliaOutlineVariant.copy(alpha = 0.3f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(bottom = 14.dp)
                    )
                    Text(
                        text = "Lília Personal Diet • Compromisso ético com sua saúde e privacidade.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = LiliaSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PrivacySectionCard(
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
                Surface(
                    shape = CircleShape,
                    color = LiliaSecondaryContainer,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = LiliaPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
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
private fun PolicyBullet(text: String) {
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
