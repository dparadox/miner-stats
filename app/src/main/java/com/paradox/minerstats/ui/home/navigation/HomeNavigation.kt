package com.paradox.minerstats.ui.home.navigation

sealed class HomeNavigation {
    data class ShowError(val message: String) : HomeNavigation()
}