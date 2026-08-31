package com.example.ui.navigation

object NavRoutes {
    const val LOGIN = "login"
    const val TERMS = "terms"
    const val PRIVACY = "privacy"
    const val DIARY = "diary"
    const val ASSISTANT = "assistant"
    const val RECIPES = "recipes"
    const val SHOPPING_LIST = "shopping_list"
    const val INSIGHTS = "insights"
    const val PROFILE = "profile"
    const val PREMIUM = "premium"
}

enum class BottomTab(val route: String, val title: String, val icon: String) {
    DIARY(NavRoutes.DIARY, "Diary", "event_note"),
    ASSISTANT(NavRoutes.ASSISTANT, "Assistant", "bolt"),
    INSIGHTS(NavRoutes.INSIGHTS, "Insights", "analytics"),
    PROFILE(NavRoutes.PROFILE, "Profile", "person")
}
