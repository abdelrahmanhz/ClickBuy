package com.example.clickbuy.models

data class CustomerParent (
    val customer: Customer
)

data class Customer (
    val first_name: String,
    val last_name: String,
    val email: String,
    val tags: String,
    val phone: String,
    val verified_email: Boolean = true,
    //val addresses: List<Address>
)

data class Address (
    val address1: String,
    val city: String,
    val province: String,
    val phone: String,
    val zip: String,
    val last_name: String,
    val first_name: String,
    val country: String
)