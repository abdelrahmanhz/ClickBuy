package com.example.clickbuy.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import java.text.DecimalFormat
import java.util.*

private const val TAG = "UTILS"

fun isRTL(): Boolean {
    val language = Locale.getDefault()
    val directionality = Character.getDirectionality(language.displayName[0]).toInt()
    return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT.toInt() || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC.toInt()

}


fun calculatePrice(amount: String): String {
    val finalValue = getEquivalentCurrencyValue(amount).plus(" " + ConstantsValue.to)
    Log.i(TAG, "calculatePrice: finalValue----------> $finalValue")
    return finalValue
}

fun getEquivalentCurrencyValue(amount: String): String {
    val newValue = amount.toDouble() * ConstantsValue.currencyValue
    return DecimalFormat("#.00")
        .format(newValue)
}

fun connectInternet(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
    } else {
        context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }
}
