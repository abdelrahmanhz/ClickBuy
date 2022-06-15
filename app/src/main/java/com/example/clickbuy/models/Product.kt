package com.example.clickbuy.models

data class Products (
    val products: List<Product>
)

data class ProductParent (
    val product : Product
)
data class Product (
    val id: Long? = null,
    val title: String? = null,
    val body_html: String? = null,
    val vendor: String? = null,
    val product_type:  String?,
    val created_at: String? = null,
    val handle: String? = null,
    val updated_at: String? = null,
    val published_at: String? = null,
    val template_suffix: Any? = null,
    val status: String? = null,
    val published_scope: String? = null,
    val tags: String? = null,
    val admin_graphql_api_id: String? = null,
    val variants: List<Variant>? = null,
    val options: List<Option>? = null,
    val images: List<ProductImage>? = null,
    val image: ProductImage? = null
)
data class ProductImage (
    val id: Long,
    val product_id: Long,
    val position: Long,
    val created_at: String,
    val updated_at: String,
    val alt: Any? = null,
    val width: Long,
    val height: Long,
    val src: String,
    val variant_ids: List<Any?>,
    val admin_graphql_api_id: String
)
data class Option (
    val id: Long,
    val product_id: Long,
    val name: String,
    val position: Long,
    val values: List<String>
)
data class Variant (
    val id: Long,
    val product_id: Long,
    val title: String,
    val price: String,
    val sku: String,
    val position: Long,
    val inventory_policy: String,
    val compare_at_price: String? = null,
    val fulfillment_service: String,
    val inventory_management: String,
    val option1: String,
    val option2: String,
    val option3: Any? = null,
    val created_at: String,
    val updated_at: String,
    val taxable: Boolean,
    val barcode: Any? = null,
    val grams: Long,
    val image_id: Any? = null,
    val weight: Long,
    val weight_unit: String,
    val inventory_item_id: Long,
    val inventory_quantity: Long,
    val old_inventory_quantity: Long,
    val requires_shipping: Boolean,
    val admin_graphql_api_id: String
)