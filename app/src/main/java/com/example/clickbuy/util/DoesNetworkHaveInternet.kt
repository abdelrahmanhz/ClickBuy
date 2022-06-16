package com.example.clickbuy.util

import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory


private const val TAG = "ConnectionLiveData"

object DoesNetworkHaveInternet {

    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            Log.i(TAG, "PINGING google.")
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            Log.i(TAG, "PING success.")
            true
        } catch (exception: IOException) {
            Log.i(TAG, "No internet connection. $exception")
            false
        }
    }
}