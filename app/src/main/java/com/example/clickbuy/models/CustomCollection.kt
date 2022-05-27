package com.example.clickbuy.models

data class CustomCollections(
    val custom_collections: List<CustomCollection>
)

data class CustomCollectionElement(
    val custom_collection: CustomCollection
)

data class CustomCollection(
    val id: Long,
    val handle: String,
    val title: String,
    val updated_at: String,
    val body_html: String? = null,
    val published_at: String,
    val sort_order: String,
    val template_suffix: Any? = null,
    val products_count: Long,
    val published_scope: String,
    val admin_graphql_api_id: String,
    val image: CustomCollectionImage? = null
)

data class CustomCollectionImage(
    val created_at: String,
    val alt: Any? = null,
    val width: Long,
    val height: Long,
    val src: String
)

