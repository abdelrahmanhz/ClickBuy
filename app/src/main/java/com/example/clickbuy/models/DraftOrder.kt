package com.example.clickbuy.models

data class DraftOrders (
    var draft_orders: List<DraftOrder>? = null
)

data class DraftOrderParent(
    val draftOrder: DraftOrder
)

data class DraftOrder (
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
    val line_items: List<LineItem>? = null,
    val shipping_address: Any? = null,
    val billing_address: Any? = null,
    val invoice_url: String? = null,
    val applied_discount: Any? = null,
    val order_id: Any? = null,
    val shipping_line: Any? = null,
    val tax_lines: List<TaxLine>? = null,
    val tags: String? = null,
    val note_attributes: List<NoteAttribute>? = null,
    val total_price: String? = null,
    val subtotal_price: String? = null,
    val total_tax: String? = null,
    val payment_terms: Any? = null,
    val admin_graphql_api_id: String? = null,
    val customer: Customer? = null
)

data class TaxLine (
    val rate: Double? = null,
    val title: String? = null,
    val price: String? = null
)


data class NoteAttribute (
    val name: String? = null,
    val value: String? = null
)




