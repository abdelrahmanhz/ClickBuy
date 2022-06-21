package com.example.clickbuy.models

data class Coupons(
    val discount_codes: List<DiscountCode>
)

data class Coupon(
    val discount_code: DiscountCode
)

data class DiscountCode(
    val code: String,
    val created_at: String,
    val id: Long,
    val price_rule_id: Long,
    val updated_at: String,
    val usage_count: Int
)