package com.example.clickbuy.models

data class Favourites (
    var draft_orders: List<Favourite>? = null
)

data class FavouriteNoteAttribute (
    val name: String? = null,
    val value: String? = null
)

data class FavouriteParent (
    val draft_order: Favourite? = null
)

data class Favourite (
    val id: Long? = null,
    val note: String? = null,
    var email: String? = null,
    val taxes_included: Boolean? = null,
    val currency: String? = null,
    val invoice_sent_at: Any? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val tax_exempt: Boolean? = null,
    val completed_at: Any? = null,
    val name: String? = null,
    val status: String? = null,
    val line_items: List<FavouriteLineItem>,
    val shipping_address: Any? = null,
    val billing_address: Any? = null,
    val invoice_url: String? = null,
    val applied_discount: Any? = null,
    val order_id: Any? = null,
    val shipping_line: Any? = null,
    val tax_lines: List<TaxLine>? = null,
    val tags: String? = null,
    val note_attributes: List<FavouriteNoteAttribute>? = null,
    val total_price: String? = null,
    val subtotal_price: String? = null,
    val total_tax: String? = null,
    val payment_terms: Any? = null,
    val admin_graphql_api_id: String? = null,
    val customer: Customer? = null,
    val presentment_currency: Currency? = null,
    val total_line_items_price_set: FavouriteSet? = null,
    val total_price_set: FavouriteSet? = null,
    val subtotal_price_set: FavouriteSet? = null,
    val total_tax_set: FavouriteSet? = null,
    val total_discounts_set: FavouriteSet? = null,
    val total_shipping_price_set: FavouriteSet? = null,
)

data class FavouriteLineItem (
    val id: Long? = null,
    val variant_id: Long? = null,
    val product_id: Long? = null,
    val title: String? = null,
    val variant_title: Any? = null,
    val sku: Any? = null,
    val vendor: Any? = null,
    val quantity: Long? = null,
    val requires_shipping: Boolean? = null,
    val taxable: Boolean? = null,
    val gift_card: Boolean? = null,
    val fulfillment_service: String? = null,
    val grams: Long? = null,
    val tax_lines: List<TaxLine>? = null,
    val applied_discount: AppliedDiscount? = null,
    val name: String? = null,
    val properties: List<Any?>? = null,
    val custom: Boolean? = null,
    val price: String? = null,
    val admin_graphql_api_id: String? = null
)

data class AppliedDiscount (
    val description: String? = null,
    val value: String? = null,
    val title: String? = null,
    val amount: String? = null,
    val value_type: String? = null
)

data class TaxLine (
    val rate: Double? = null,
    val title: String? = null,
    val price: String? = null
)

data class FavouriteSet (
    val shop_money: FavouriteMoney? = null,
    val presentment_money: FavouriteMoney? = null
)

data class FavouriteMoney (
    val amount: String? = null,
    val currency_code: Currency? = null
)





