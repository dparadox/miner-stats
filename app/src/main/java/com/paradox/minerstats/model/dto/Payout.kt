package com.paradox.minerstats.model.dto

data class Payout(
    val start: String? = null,
    val end: String? = null,
    val amount: String? = null,
    val txHash: String? = null,
    val paidOn: Long? = null
)