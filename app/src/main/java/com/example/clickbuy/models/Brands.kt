package com.example.clickbuy.models

data class Brands (
    val smart_collections: List<Brand>
)
data class Brand (
    val id: String?=null,
    val handle: String?=null,
    val title: String?=null,
    val updated_at: String?=null,
    val body_html: String?=null,
    val published_at: String?=null,
    val sort_order: String?=null,
    val template_suffix: Any? = null,
    val disjunctive: Boolean?=null,
    val published_scope: String?=null,
    val admin_graphql_api_id: String?=null,
    val image: Image?=null
)
data class Image (
    val createdAt: String,
    val alt: Any? = null,
    val width: Long,
    val height: Long,
    val src: String
)
