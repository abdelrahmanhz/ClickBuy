package com.example.clickbuy.models

data class SubCategories(
    val products: HashSet<SubCategory>
)

data class SubCategory(
    val product_type: String
)