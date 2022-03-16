package com.paradox.minerstats.core.adapter

interface ResourceAdapter {

    fun getString(resId: Int): String

    fun getString(resId: Int, format: String): String
}