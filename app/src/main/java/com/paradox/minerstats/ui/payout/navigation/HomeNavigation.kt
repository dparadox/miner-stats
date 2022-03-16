package com.paradox.minerstats.ui.payout.navigation

sealed class PayoutNavigation {
    data class ShowPayoutDetail(val url: String) : PayoutNavigation()
    data class ShowError(val message: String) : PayoutNavigation()
}