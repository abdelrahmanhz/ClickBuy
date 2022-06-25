package com.example.clickbuy.models

import java.io.Serializable

class ShoppingBag(
    val draft_order: DraftOrder
) : Serializable

/*data class DraftOrder(
    val admin_graphql_api_id: String,
    val applied_discount: Any,
    val billing_address: Any,
    val completed_at: Any,
    val created_at: String,
    val currency: String,
    val customer: Customer,
    val email: String,
    val id: Long,
    val invoice_sent_at: Any,
    val invoice_url: String,
    val line_items: List<BagItem>,
    val name: String,
    val note: String,
    val note_attributes: List<NoteAttribute>,
    val order_id: Any,
    val payment_terms: Any,
    val shipping_address: Any,
    val shipping_line: Any,
    val status: String,
    val subtotal_price: String,
    val tags: String,
    val tax_exempt: Boolean,
    val tax_lines: List<TaxLineX>,
    val taxes_included: Boolean,
    val total_price: String,
    val total_tax: String,
    val updated_at: String
)*/
data class DraftOrder(
    val email: String? = null,
    val id: Long? = null,
    val line_items: List<BagItem>,
    val note_attributes: List<NoteAttribute>,
    val total_price: String = "",
    val total_tax: String = "",
    val subtotal_price: String = "",
) : Serializable

data class TaxLineX(
    val price: String,
    val rate: Double,
    val title: String
) : Serializable

data class NoteAttribute(
    val name: String,
    val value: String
) : Serializable

data class BagItem(
    val admin_graphql_api_id: String = "",
    val applied_discount: Any? = null,
    val custom: Boolean = false,
    val fulfillment_service: String = "",
    val gift_card: Boolean = false,
    val grams: Int = 0,
    val id: Long = 0,
    val name: String = "",
    val price: String = "",
    val product_id: Long = 0,
    val properties: List<Any>? = null,
    var quantity: Int,
    val requires_shipping: Boolean = false,
    val sku: String = "",
    val tax_lines: List<TaxLineX>? = null,
    val taxable: Boolean = false,
    val title: String = "",
    val variant_id: Long,
    val variant_title: String = "",
    val vendor: String = ""
) : Serializable

