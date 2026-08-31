package com.example.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ChatMessageEntity
import com.example.ui.components.LiliaTopAppBar
import com.example.ui.theme.LiliaBackground
import com.example.ui.theme.LiliaBorderLight
import com.example.ui.theme.LiliaMintLight
import com.example.ui.theme.LiliaOnPrimary
import com.example.ui.theme.LiliaPrimary
import com.example.ui.theme.LiliaPrimaryFixed
import com.example.ui.theme.LiliaSecondary
import com.example.ui.theme.LiliaSecondaryContainer
import com.example.ui.viewmodel.LiliaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatAnamnesisScreen(
    viewModel: LiliaViewModel,
    onNavigateToSettings: () -> Unit,
    onTriggerPhotoAnalysis: () -> Unit
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isAiTyping by viewModel.isAiTyping.collectAsState()
    val isRecordingVoice by viewModel.isRecordingVoice.collectAsState()

    var inputMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val suggestionChips = listOf(
        "Tenho intolerância a lactose 🥛",
        "Comi um doce e estou culpada 🍫",
        "Sugira um lanche prático da tarde 🥑",
        "Quero atingir minha meta de água 💧",
        "Qual o melhor horário para o almoço? 🥗"
    )

    LaunchedEffect(messages.size, isAiTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            LiliaTopAppBar(
                title = "Lília",
                subtitle = "Organizador de Estilo de Vida • Online",
                avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAiH6AAW8fJhMv8CAiVvE9lmau3hbp7OKPRalmeSDt4TJUPGw5r8MnEfQ69Jbv2NgbV7Ei3HddMhbgsYUOiYTltsmt45SUAkHo2BLs1VevO0cV6pHzEW9Fc2g22euN6R-vppQsYsixrnblkSwIWALA3z9w0mEMNoOmZlqdBA4Rc0nbpO5K24IcFM5tgrvfwP7q3XmfYoL5jiuqBnGDtsbVywx8tnrglSY0OF1jxfrstHVUu2LORofg5",
                onSettingsClick = onNavigateToSettings
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .shadow(4.dp)
                    .padding(vertical = 8.dp)
            ) {
                // Suggestion chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(suggestionChips) { chipText ->
                        SuggestionChip(
                            onClick = {
                                viewModel.sendUserMessage(chipText)
                            },
                            label = {
                                Text(
                                    text = chipText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = LiliaPrimary
                                    )
                                )
                            },
                            shape = RoundedCornerShape(999.dp),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = LiliaMintLight
                            ),
                            border = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Voice recording banner overlay if recording
                AnimatedVisibility(
                    visible = isRecordingVoice,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFEBEE))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Gravando áudio para a Lília...",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Row {
                            Text(
                                text = "Cancelar",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = LiliaSecondary,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier
                                    .clickable { viewModel.cancelVoiceRecording() }
                                    .padding(horizontal = 8.dp)
                            )
                            Text(
                                text = "Enviar Áudio",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = LiliaPrimary,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier
                                    .clickable { viewModel.stopVoiceRecordingAndSend() }
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                }

                // Input bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Camera / Vision scan button
                    IconButton(
                        onClick = onTriggerPhotoAnalysis,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(LiliaMintLight)
                            .testTag("chat_camera_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Analisar Prato",
                            tint = LiliaPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Text field
                    OutlinedTextField(
                        value = inputMessage,
                        onValueChange = { inputMessage = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_message_input"),
                        placeholder = {
                            Text(
                                text = "Fale com a Lília...",
                                style = MaterialTheme.typography.bodyMedium.copy(color = LiliaSecondary)
                            )
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LiliaPrimary,
                            unfocusedBorderColor = LiliaBorderLight,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    if (inputMessage.isNotBlank()) {
                        // Send button
                        IconButton(
                            onClick = {
                                val msg = inputMessage
                                inputMessage = ""
                                viewModel.sendUserMessage(msg)
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(LiliaPrimary)
                                .testTag("chat_send_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Enviar",
                                tint = LiliaOnPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        // Mic button
                        IconButton(
                            onClick = {
                                if (isRecordingVoice) {
                                    viewModel.stopVoiceRecordingAndSend()
                                } else {
                                    viewModel.startVoiceRecording()
                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (isRecordingVoice) Color.Red else LiliaMintLight)
                                .testTag("chat_mic_button")
                        ) {
                            Icon(
                                imageVector = if (isRecordingVoice) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = "Gravar Áudio",
                                tint = if (isRecordingVoice) Color.White else LiliaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        },
        containerColor = LiliaBackground
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatMessageBubble(message = msg)
            }

            if (isAiTyping) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            modifier = Modifier.shadow(2.dp, shape = RoundedCornerShape(18.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = LiliaPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Lília está digitando com carinho...",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = LiliaSecondary,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
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
private fun ChatMessageBubble(message: ChatMessageEntity) {
    val isUser = message.sender == "user"
    val timeFormatted = remember(message.timestamp) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp))
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuB1RevcwbYpY7D9WuwtPEtlySFC8wyhc9GklKdo7C6mldGbNHppbPsAPZBrW5hvpcw5mNaVBguFvxYlNsYFfVzaaWcdV7DbJfug9RGkHpo_nwSVAElvHo8Xg0_1mJn0U9jyf2EP3v2jvdjJ8r87PpTUgBefgQFGpglmMAkOmOX5zKlaeNl16-AVQa4aB1DqIX1Rf9bPz8WQAw7Xflfso61R2zPqDrRBIiBNYQTwcfe3sXNvF8rzk1Dm",
                contentDescription = "Lília",
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(LiliaMintLight),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isUser) 18.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 18.dp
            ),
            color = if (isUser) LiliaPrimary else MaterialTheme.colorScheme.surfaceContainerLowest,
            modifier = Modifier
                .widthIn(max = 290.dp)
                .shadow(2.dp, shape = RoundedCornerShape(18.dp))
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isUser) LiliaOnPrimary else MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = timeFormatted,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = if (isUser) LiliaOnPrimary.copy(alpha = 0.7f) else LiliaSecondary
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
