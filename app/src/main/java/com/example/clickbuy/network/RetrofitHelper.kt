package com.example.clickbuy.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {


    private const val API_URL = "https://madalex20220.myshopify.com/admin/api/2022-01/"
    private const val CURRENCY_API = "https://api.apilayer.com/currency_data/"
    const val HEADERS_CONTENT_TYPE = "Content-Type: application/json"
    const val HEADERS_ACCESS_TOKEN =
        "X-Shopify-Access-Token: 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"

    fun getClientShopify(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getClientCurrency(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(CURRENCY_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}