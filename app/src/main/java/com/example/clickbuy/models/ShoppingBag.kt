package com.example.clickbuy.models

import java.io.Serializable

class ShoppingBag(
    val draft_order: DraftOrder
) : Serializable

data class DraftOrder(
    val email: String,
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

