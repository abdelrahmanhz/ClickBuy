package com.example.clickbuy.models

data class PriceRules(
    val price_rule: PriceRule
)

data class PriceRule(
    val admin_graphql_api_id: String,
    val allocation_limit: Any,
    val allocation_method: String,
    val created_at: String,
    val customer_selection: String,
    val ends_at: String,
    val id: Long,
    val once_per_customer: Boolean,
    val starts_at: String,
    val target_selection: String,
    val target_type: String,
    val title: String,
    val updated_at: String,
    val usage_limit: Int,
    val value: String,
    val value_type: String
)