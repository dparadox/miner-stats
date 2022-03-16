package com.paradox.minerstats.core.adapter

import android.content.Context
import javax.inject.Inject

class ResourceAdapterImpl @Inject constructor(private val context: Context) : ResourceAdapter {

    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, format: String): String = context.getString(resId, format)
}