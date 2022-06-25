package com.example.clickbuy.models

data class Customers(
    val customers: List<Customer>
)

data class CustomerParent(
    val customer: Customer
)

data class CustomersTest(
    val customer: CustomerTest
)

data class Customer(
    val accepts_marketing: Boolean? = null,
    val accepts_marketing_updated_at: String? = null,
    val addresses: List<CustomerAddress>? = null,
    val admin_graphql_api_id: String? = null,
    val created_at: String? = null,
    val currency: String? = null,
    val default_address: DefaultAddress? = null,
    val email: String,
    val first_name: String? = null,
    val id: Long? = null,
    val last_name: String? = null,
    val last_order_id: Any? = null,
    val last_order_name: Any? = null,
    val marketing_opt_in_level: Any? = null,
    val multipass_identifier: Any? = null,
    val note: Any? = null,
    val orders_count: Int? = null,
    val phone: String? = null,
    val sms_marketing_consent: SmsMarketingConsent? = null,
    val state: String? = null,
    val tags: String? = null,
    val tax_exempt: Boolean? = null,
    val tax_exemptions: List<Any>? = null,
    val total_spent: String? = null,
    val updated_at: String? = null,
    val verified_email: Boolean = true,
)

data class CustomerTest(
    val email: String? = null,
    val first_name: String? = null,
    val id: Long? = null,
    val last_name: String? = null,
    val tags: String? = null,
)

data class CustomerAddresses(
    val addresses: List<CustomerAddress>
)

data class CustomerAddressUpdate(
    val address: CustomerAddress
)

data class CustomerAddressResponse(
    val customer_address: CustomerAddress
)


data class CustomerAddress(
    val address1: String?= null,
    val city: String?= null,
    val country: String?= null,
    val country_code: String?= null,
    val country_name: String?= null,
    val customer_id: Long? = null,
    val default: Boolean?= null,
    val id: Long? = null,
    val province: String?= null
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