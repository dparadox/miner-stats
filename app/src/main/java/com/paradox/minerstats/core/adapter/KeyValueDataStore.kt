package com.paradox.minerstats.core.adapter

interface KeyValueDataStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun remove(vararg keys: String)
}