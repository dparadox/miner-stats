package com.paradox.minerstats.ui.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.paradox.minerstats.core.extensions.toReadableValue
import com.paradox.minerstats.core.extensions.toSimpleDateFormatString
import org.web3j.utils.Convert
import org.web3j.utils.Convert.Unit.ETHER

@SuppressLint("SetTextI18n")
@BindingAdapter("android:set_amount")
fun setAmount(view: TextView, amount: String) {
    val eth = String.format("%.2f", Convert.fromWei(amount, ETHER))
    view.text = "$eth $ETHER"
}

@BindingAdapter("android:set_date_full_time_text")
fun setDateFullTime(view: TextView, time: Long) {
    view.text = time.toSimpleDateFormatString()
}

@BindingAdapter("android:set_hash_rate")
fun setHashRate(view: TextView, hashRate: Double?) {
    hashRate?.let {
        view.text = hashRate.toReadableValue()
    }
}