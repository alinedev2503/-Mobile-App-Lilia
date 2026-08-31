package com.example.ui.screens.shopping

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShoppingItemEntity
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
fun ShoppingListScreen(
    viewModel: LiliaViewModel,
    onBackClick: () -> Unit
) {
    val items by viewModel.shoppingItems.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val totalItems = items.size
    val completedItems = items.count { it.isChecked }

    Scaffold(
        topBar = {
            LiliaTopAppBar(
                title = "Lília",
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .padding(end = 4.dp, bottom = 8.dp)
                    .height(48.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = PillShape,
                        ambientColor = LiliaPrimary.copy(alpha = 0.2f),
                        spotColor = LiliaPrimary.copy(alpha = 0.3f)
                    )
                    .testTag("shopping_add_fab"),
                shape = PillShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LiliaPrimary,
                    contentColor = LiliaOnPrimary
                ),
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar",
                        tint = LiliaOnPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Adicionar Item",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = LiliaOnPrimary
                        )
                    )
                }
            }
        },
        containerColor = LiliaSurfaceContainerLowest
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Section
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Lista da Semana",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 28.sp,
                                letterSpacing = (-0.01).sp
                            )
                        )

                        if (completedItems > 0) {
                            TextButton(
                                onClick = { viewModel.clearCheckedShoppingItems() },
                                modifier = Modifier.testTag("shopping_clear_checked")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = null,
                                    tint = LiliaPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Limpar",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = LiliaPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gerada com base no seu cardápio de 7 dias.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 14.sp
                        )
                    )
                }
            }

            // Categories list: Hortifruti, Proteínas, Grãos, Outros
            val defaultCategories = listOf("Hortifruti", "Proteínas", "Grãos")
            val extraCategories = items.map { it.category }.distinct().filterNot { it in defaultCategories }
            val allActiveCategories = defaultCategories + extraCategories

            allActiveCategories.forEach { category ->
                val categoryItems = items.filter { it.category == category }
                if (categoryItems.isNotEmpty()) {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = LiliaPrimary
                                ),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )

                            // List of items under category
                            categoryItems.forEachIndexed { index, item ->
                                ShoppingItemCleanRow(
                                    item = item,
                                    onToggle = { viewModel.toggleShoppingItem(item) }
                                )
                                if (index < categoryItems.size - 1) {
                                    HorizontalDivider(
                                        color = LiliaOutlineVariant.copy(alpha = 0.25f),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (items.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = LiliaBackground
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = LiliaSecondary.copy(alpha = 0.5f),
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Sua lista de compras está vazia.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showAddDialog = true },
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                            ) {
                                Text("Adicionar Alimento", color = LiliaOnPrimary)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var quantity by remember { mutableStateOf("1 unid.") }
        var category by remember { mutableStateOf("Hortifruti") }

        val categoryOptions = listOf("Hortifruti", "Proteínas", "Grãos", "Outros")

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = LiliaSurfaceContainerLowest,
            title = {
                Text(
                    text = "Adicionar à Lista",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome do alimento") },
                        placeholder = { Text("ex: Maçã Gala, Brócolis") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LiliaPrimary,
                            unfocusedBorderColor = LiliaOutlineVariant.copy(alpha = 0.4f)
                        )
                    )
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantidade") },
                        placeholder = { Text("ex: 6 unid., 800g, 1 maço") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LiliaPrimary,
                            unfocusedBorderColor = LiliaOutlineVariant.copy(alpha = 0.4f)
                        )
                    )
                    Text(
                        text = "Categoria:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categoryOptions.forEach { cat ->
                            Surface(
                                shape = PillShape,
                                color = if (category == cat) LiliaPrimary else LiliaMintLight,
                                modifier = Modifier
                                    .clickable { category = cat }
                                    .weight(1f)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 7.dp)
                                ) {
                                    Text(
                                        text = cat,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (category == cat) LiliaOnPrimary else LiliaPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            viewModel.addShoppingItem(name, quantity, category)
                            showAddDialog = false
                        }
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = LiliaPrimary)
                ) {
                    Text("Adicionar", color = LiliaOnPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
private fun ShoppingItemCleanRow(
    item: ShoppingItemEntity,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 14.dp)
            .testTag("shopping_item_${item.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Circular Minimalist Checkbox
            if (item.isChecked) {
                Surface(
                    shape = CircleShape,
                    color = LiliaPrimary,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Comprado",
                            tint = LiliaOnPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(
                            width = 1.5.dp,
                            color = LiliaOutlineVariant,
                            shape = CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Food Name
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = if (item.isChecked) FontWeight.Normal else FontWeight.Normal,
                    textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (item.isChecked) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                )
            )
        }

        // Quantity info on right
        Text(
            text = item.quantity,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                color = if (item.isChecked) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.tertiary
            )
        )
    }
}
