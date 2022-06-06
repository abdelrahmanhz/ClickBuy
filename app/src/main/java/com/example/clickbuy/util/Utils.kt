package com.example.clickbuy.util

import android.content.Context
import android.net.ConnectivityManager

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
}