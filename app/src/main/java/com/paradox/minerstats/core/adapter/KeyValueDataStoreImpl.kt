package com.paradox.minerstats.core.adapter

import android.content.SharedPreferences
import javax.inject.Inject

class KeyValueDataStoreImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : KeyValueDataStore {

    override fun getString(key: String): String? = sharedPreferences.getString(key, null)

    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun remove(vararg keys: String) {
        val editor = sharedPreferences.edit()
        keys.forEach {
            editor.remove(it)
        }
        editor.apply()
    }
}