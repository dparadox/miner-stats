package com.paradox.minerstats.core.extensions

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

fun Date.toString(mask: String): String? {
    return try {
        val simpleDateFormat = SimpleDateFormat(mask, Locale.getDefault())
        simpleDateFormat.format(this)
    } catch (ignored: Exception) {
        null
    }
}

fun String.toDate(mask: String): Date? {
    return try {
        val simpleDateFormat = SimpleDateFormat(mask, Locale.getDefault())
        simpleDateFormat.parse(this)
    } catch (ignored: Exception) {
        null
    }
}

fun Long.toMonthDayYearString() : String {
    val date = Date(this * 1000L)
    return SimpleDateFormat("MMMM d, yyyy").format(date)
}

fun Long.toSimpleDateFormatString() : String {
    val date = Date(this * 1000L)
    return SimpleDateFormat("yyyy-MM-dd HH:mm").format(date)
}

// This function is not fully tested, found in Stackoverflow - need more research
// TODO Find optimal way to format the Hashing power
fun Double.toReadableValue(): String {
    if (this <= 0) return "0"
    val units = arrayOf("H/s", "K/hs", "MH/s", "GH/s", "TH/s")
    val digitGroups = (log10(this) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.000#").format(this / 1024.0.pow(digitGroups.toDouble()))
        .toString() + " " + units[digitGroups]
}