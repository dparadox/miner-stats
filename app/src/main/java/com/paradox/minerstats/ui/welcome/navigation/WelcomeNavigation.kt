package com.paradox.minerstats.ui.welcome.navigation

sealed class WelcomeNavigation {
    object Success : WelcomeNavigation()
    data class ShowError(val message: String) : WelcomeNavigation()
}