package com.example.clickbuy.models

data class Currencies(
    val currencies: List<Currency>
)

data class Currency(
    val currency: String,
    val enabled: Boolean,
    val rate_updated_at: String
)
data class CurrencyConverter(
    val date: String,
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean
)
data class Info(
    val rate: Double,
    val timestamp: Int
)

data class Query(
    val amount: Int,
    val from: String,
    val to: String
)