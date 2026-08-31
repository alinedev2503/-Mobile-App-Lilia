package com.example.ui.screens.recipes

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.RecipeItem
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
import com.example.ui.viewmodel.LiliaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    viewModel: LiliaViewModel,
    onBackClick: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedDietFilter.collectAsState()
    val recipes by viewModel.filteredRecipes.collectAsState()
    val selectedRecipe by viewModel.selectedRecipe.collectAsState()

    val categories = listOf("All Recipes", "Vegan", "Gluten-Free", "Low Carb", "High Protein")

    Scaffold(
        topBar = {
            LiliaTopAppBar(
                title = "Lília",
                onBackClick = onBackClick,
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
            contentPadding = PaddingValues(top = 10.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Header Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Personalized Recipes",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 26.sp,
                            letterSpacing = (-0.01).sp
                        )
                    )
                    Text(
                        text = "Curated meals based on your dietary preferences.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 15.sp
                        )
                    )
                }
            }

            // 2. Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("recipes_search_input"),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = LiliaSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Search recipes...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = LiliaSecondary.copy(alpha = 0.8f),
                                fontSize = 15.sp
                            )
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LiliaPrimary,
                        unfocusedBorderColor = LiliaOutlineVariant.copy(alpha = 0.4f),
                        focusedContainerColor = LiliaSurfaceContainerLowest,
                        unfocusedContainerColor = LiliaSurfaceContainerLowest
                    ),
                    singleLine = true
                )
            }

            // 3. Dietary Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedFilter
                        Surface(
                            shape = PillShape,
                            color = if (isSelected) LiliaPrimary else LiliaSecondaryContainer.copy(alpha = 0.6f),
                            modifier = Modifier
                                .clickable { viewModel.setDietFilter(cat) }
                                .testTag("filter_chip_${cat.lowercase().replace(" ", "_")}")
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (isSelected) LiliaOnPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // 4. Recipes Grid/List
            if (recipes.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = LiliaSurfaceContainerLowest
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Nenhuma receita encontrada para os filtros atuais.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = LiliaSecondary)
                            )
                        }
                    }
                }
            } else {
                items(recipes, key = { it.id }) { recipe ->
                    ModernRecipeCard(
                        recipe = recipe,
                        onCardClick = { viewModel.selectRecipe(recipe) },
                        onFavoriteClick = { viewModel.toggleFavorite(recipe.id) }
                    )
                }
            }
        }
    }

    // Recipe Detail Modal
    selectedRecipe?.let { recipe ->
        ModalBottomSheet(
            onDismissRequest = { viewModel.selectRecipe(null) },
            containerColor = LiliaSurfaceContainerLowest,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 36.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 22.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.selectRecipe(null) }) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = LiliaSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = LiliaPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${recipe.timeMinutes} min", style = MaterialTheme.typography.labelMedium.copy(color = LiliaSecondary))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = LiliaPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${recipe.calories} kcal", style = MaterialTheme.typography.labelMedium.copy(color = LiliaSecondary))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ingredientes",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = LiliaPrimary,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                recipe.ingredients.forEach { ing ->
                    Text(
                        text = "• $ing",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 22.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Modo de Preparo",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = LiliaPrimary,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                recipe.instructions.forEachIndexed { index, step ->
                    Text(
                        text = "${index + 1}. $step",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.addRecipeToShoppingList(recipe)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(4.dp, shape = PillShape, ambientColor = LiliaPrimary.copy(alpha = 0.25f))
                        .testTag("recipe_add_to_shopping_button"),
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LiliaPrimary,
                        contentColor = LiliaOnPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = LiliaOnPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Adicionar Ingredientes à Lista da Semana",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = LiliaOnPrimary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernRecipeCard(
    recipe: RecipeItem,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(16.dp), ambientColor = LiliaPrimary.copy(alpha = 0.04f))
            .border(1.dp, Color(0xFFEFF4FF), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onCardClick() }
            .testTag("recipe_card_${recipe.id}"),
        shape = RoundedCornerShape(16.dp),
        color = LiliaSurfaceContainerLowest
    ) {
        Column {
            // Recipe Image Container with Floating Favorite Icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            ) {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Favorite Heart Button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .shadow(4.dp, shape = CircleShape, ambientColor = Color.Black.copy(alpha = 0.1f))
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { onFavoriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (recipe.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favoritar",
                        tint = if (recipe.isFavorite) LiliaPrimary else LiliaSecondary.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Recipe Info Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Category / Diet Tags
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    recipe.tags.forEach { tag ->
                        Surface(
                            shape = PillShape,
                            color = LiliaMintLight
                        ) {
                            Text(
                                text = tag.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.6.sp,
                                    color = LiliaPrimary
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // Recipe Title
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Footer: Time and Calories
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Tempo",
                            tint = LiliaSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${recipe.timeMinutes} min",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LiliaSecondary,
                                fontSize = 13.sp
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Calorias",
                            tint = LiliaSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${recipe.calories} kcal",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = LiliaSecondary,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
