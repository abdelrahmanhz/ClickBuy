package com.example.clickbuy.models

data class Customers(
    val customers: List<Customer>
)

data class Customer(
    val accepts_marketing: Boolean,
    val accepts_marketing_updated_at: String,
    val addresses: List<CustomerAddress>,
    val admin_graphql_api_id: String,
    val created_at: String,
    val currency: String,
    val default_address: DefaultAddress,
    val email: String,
    val first_name: String,
    val id: Long,
    val last_name: String,
    val last_order_id: Any,
    val last_order_name: Any,
    val marketing_opt_in_level: Any,
    val multipass_identifier: Any,
    val note: Any,
    val orders_count: Int,
    val phone: String,
    val sms_marketing_consent: SmsMarketingConsent,
    val state: String,
    val tags: String,
    val tax_exempt: Boolean,
    val tax_exemptions: List<Any>,
    val total_spent: String,
    val updated_at: String,
    val verified_email: Boolean
)

data class CustomerAddress(
    val address1: String,
    val address2: Any,
    val city: String,
    val company: Any,
    val country: String,
    val country_code: String,
    val country_name: String,
    val customer_id: Long,
    val default: Boolean,
    val first_name: String,
    val id: Long,
    val last_name: String,
    val name: String,
    val phone: String,
    val province: String,
    val province_code: String,
    val zip: String
)

data class DefaultAddress(
    val address1: String,
    val address2: Any,
    val city: String,
    val company: Any,
    val country: String,
    val country_code: String,
    val country_name: String,
    val customer_id: Long,
    val default: Boolean,
    val first_name: String,
    val id: Long,
    val last_name: String,
    val name: String,
    val phone: String,
    val province: String,
    val province_code: String,
    val zip: String
)


data class SmsMarketingConsent(
    val consent_collected_from: String,
    val consent_updated_at: Any,
    val opt_in_level: String,
    val state: String
)