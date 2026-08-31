package com.example.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.model.PlateAnalysisUiState
import com.example.ui.components.LiliaBottomNavBar
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.PrivacyPolicyScreen
import com.example.ui.screens.auth.TermsScreen
import com.example.ui.screens.chat.ChatAnamnesisScreen
import com.example.ui.screens.dashboard.DashboardScreen
import com.example.ui.screens.insights.InsightsScreen
import com.example.ui.screens.meal.MealAnalysisBottomSheet
import com.example.ui.screens.premium.PremiumPaywallScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.recipes.RecipesScreen
import com.example.ui.screens.shopping.ShoppingListScreen
import com.example.ui.viewmodel.LiliaViewModel

@Composable
fun LiliaAppNavHost(
    viewModel: LiliaViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavRoutes.LOGIN

    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage.collectAsState()
    val analysisResult by viewModel.currentAnalysisResult.collectAsState()
    val analysisUiState by viewModel.analysisUiState.collectAsState()

    var activeMealTypeToScan by remember { mutableStateOf("Almoço") }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    val isBottomBarVisible = currentRoute in listOf(
        NavRoutes.DIARY,
        NavRoutes.ASSISTANT,
        NavRoutes.INSIGHTS,
        NavRoutes.PROFILE
    )

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                LiliaBottomNavBar(
                    currentRoute = currentRoute,
                    onTabSelected = { tab ->
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = NavRoutes.DIARY
            ) {
                // Auth Routes
                composable(NavRoutes.LOGIN) {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate(NavRoutes.DIARY) {
                                popUpTo(NavRoutes.LOGIN) { inclusive = true }
                            }
                        },
                        onNavigateToTerms = { navController.navigate(NavRoutes.TERMS) },
                        onNavigateToPrivacy = { navController.navigate(NavRoutes.PRIVACY) }
                    )
                }

                composable(NavRoutes.TERMS) {
                    TermsScreen(
                        onAccept = {
                            navController.navigate(NavRoutes.DIARY) {
                                popUpTo(NavRoutes.TERMS) { inclusive = true }
                            }
                        },
                        onDecline = { navController.popBackStack() },
                        onNavigateToPrivacy = { navController.navigate(NavRoutes.PRIVACY) }
                    )
                }

                composable(NavRoutes.PRIVACY) {
                    PrivacyPolicyScreen(
                        onBackClick = { navController.popBackStack() },
                        onManageDataClick = { navController.navigate(NavRoutes.PROFILE) }
                    )
                }

                // Main Navigation Tabs
                composable(NavRoutes.DIARY) {
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToRecipes = { navController.navigate(NavRoutes.RECIPES) },
                        onNavigateToShoppingList = { navController.navigate(NavRoutes.SHOPPING_LIST) },
                        onTriggerScanMeal = { mealType ->
                            activeMealTypeToScan = mealType
                            viewModel.triggerPlateAnalysis(mealType)
                        },
                        onNavigateToSettings = { navController.navigate(NavRoutes.PROFILE) }
                    )
                }

                composable(NavRoutes.ASSISTANT) {
                    ChatAnamnesisScreen(
                        viewModel = viewModel,
                        onNavigateToSettings = { navController.navigate(NavRoutes.PROFILE) },
                        onTriggerPhotoAnalysis = {
                            activeMealTypeToScan = "Almoço"
                            viewModel.triggerPlateAnalysis("Almoço")
                        }
                    )
                }

                composable(NavRoutes.INSIGHTS) {
                    InsightsScreen(
                        viewModel = viewModel,
                        onNavigateToSettings = { navController.navigate(NavRoutes.PROFILE) }
                    )
                }

                composable(NavRoutes.PROFILE) {
                    ProfileScreen(
                        viewModel = viewModel,
                        onNavigateToTerms = { navController.navigate(NavRoutes.TERMS) },
                        onNavigateToPrivacy = { navController.navigate(NavRoutes.PRIVACY) },
                        onNavigateToPremium = { navController.navigate(NavRoutes.PREMIUM) },
                        onLogout = {
                            navController.navigate(NavRoutes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                // Sub-screens
                composable(NavRoutes.RECIPES) {
                    RecipesScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.SHOPPING_LIST) {
                    ShoppingListScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.PREMIUM) {
                    PremiumPaywallScreen(
                        viewModel = viewModel,
                        onClose = { navController.popBackStack() },
                        onNavigateToTerms = { navController.navigate(NavRoutes.TERMS) },
                        onNavigateToPrivacy = { navController.navigate(NavRoutes.PRIVACY) }
                    )
                }
            }

            // Global Analysis Bottom Sheet if active
            if (analysisUiState !is com.example.data.model.PlateAnalysisUiState.Idle) {
                MealAnalysisBottomSheet(
                    uiState = analysisUiState,
                    mealType = activeMealTypeToScan,
                    onConfirm = { updated ->
                        viewModel.confirmAndLogMeal(updated, activeMealTypeToScan)
                    },
                    onRetry = { meal ->
                        viewModel.triggerPlateAnalysis(meal)
                    },
                    onManualLog = { meal, desc, cal, prot, carb, fat ->
                        viewModel.manualLogMeal(meal, desc, cal, prot, carb, fat)
                    },
                    onNavigateToChat = {
                        viewModel.dismissAnalysisModal()
                        navController.navigate(NavRoutes.ASSISTANT)
                    },
                    onDismiss = {
                        viewModel.dismissAnalysisModal()
                    }
                )
            }
        }
    }
}
