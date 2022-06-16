package com.example.clickbuy.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import java.text.DecimalFormat
import java.util.*

/*
fun isInternetAvailable(context: Context): Boolean {
    var isAvailable = false
    val manager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.isConnected || manager.getNetworkInfo(
            ConnectivityManager.TYPE_MOBILE
        )!!
            .isConnected
    ) isAvailable = true
    return isAvailable
}*/

private const val TAG = "UTILS"

fun isNetworkAvailable(context: Context): Boolean {
    var isConnected = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // For 29 api or above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            isConnected = when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }

    } else {
        // For below 29 api
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
            isConnected = true
        }
    }
    return isConnected
}

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
