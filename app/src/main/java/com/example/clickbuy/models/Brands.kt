package com.example.clickbuy.models

data class Brands (
    val smart_collections: List<Brand>
)
data class Brand (
    val id: String,
    val handle: String,
    val title: String,
    val updated_at: String,
    val body_html: String,
    val published_at: String,
    val sort_order: String,
    val template_suffix: Any? = null,
    val disjunctive: Boolean,
    val published_scope: String,
    val admin_graphql_api_id: String,
    val image: Image
)
data class Image (
    val createdAt: String,
    val alt: Any? = null,
    val width: Long,
    val height: Long,
    val src: String
)
