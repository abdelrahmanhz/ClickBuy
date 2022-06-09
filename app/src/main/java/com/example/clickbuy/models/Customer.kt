package com.example.clickbuy.models


data class CustomersList (
    val customers: List<Customer>
)

data class CustomerParent (
    val customer: Customer
)

data class Customer (
    val first_name: String,
    val last_name: String,
    val email: String,
    val tags: String,
    val phone: String,
    val id: Long? = null,
    val verified_email: Boolean = true,
    val accepts_marketing: Boolean? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val orders_count: Long? = null,
    val state: String? = null,
    val total_spent: String? = null,
    val last_order_id: Long? = null,
    val note: String? = null,
    val multipass_identifier: Any? = null,
    val tax_exempt: Boolean? = null,
    val last_order_name: String? = null,
    val currency: String? = null,
    val addresses: List<Address>? = null,
    val accepts_marketing_updated_at: String? = null,
    val default_address: Address? = null
)

data class Address (
    val address1: String,
    val city: String,
    val province: String,
    val phone: String,
    val zip: String,
    val country: String,
    val id: Long? = null,
    val customerID: Long? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val address2: String? = null,
    val company: String? = null,
    val name: String? = null,
    val province_code: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val default: Boolean? = null
    )





