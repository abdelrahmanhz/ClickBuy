package com.example.clickbuy.util

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
