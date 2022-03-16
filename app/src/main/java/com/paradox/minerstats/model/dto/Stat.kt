package com.paradox.minerstats.model.dto

data class Stat(
    val time: Long = 0,
    val lastSeen: Long = 0,
    val reportedHashrate: Double? = 0.0,
    val currentHashrate: Double? = 0.0,
    val averageHashrate: Double? = 0.0,
    val validShares: Int = 0,
    val invalidShares: Int = 0,
    val staleShares: Int = 0,
    val activeWorkers: Int = 0,
    val unpaid: String? = null,
    val unconfirmed: Int = 0
)