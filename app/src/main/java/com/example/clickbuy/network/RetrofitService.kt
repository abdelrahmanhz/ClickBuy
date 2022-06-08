package com.example.clickbuy.network

import com.example.clickbuy.models.*
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Query

interface RetrofitService {

    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("products.json")
    suspend fun getAllProducts(
        @Query("collection_id") id : String,
        @Query("vendor") vendor : String,
        @Query("product_type") title: String) : Response<Products>
    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("collections/{id}/products.json")
    suspend fun getAllProductsInCollectionByID(@Path("id") id: String): Response<Products>

    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("custom_collections.json?title={categoryTitle}")
    suspend fun getCategoryIdByTitle(
        @Path("categoryTitle") categoryTitle: String
    ): Response<CustomCollections>


    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("smart_collections.json")
    suspend fun getAllBrands(): Response<Brands>


    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("products/{id}.json")
    suspend fun getProductById(@Path("id") id: String): Response<ProductParent>

    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("products.json?")
    suspend fun getAllSubCategoriesForSpecificCategory(
        @Query("fields") product_type : String,
        @Query("collection_id") id : String
    ): Response<SubCategories>

    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("products.json?")
    suspend fun getAllSubCategoriesForSpecificCategoryByIDAndTitle(
        @Query("collection_id") id : String,
        @Query("vendor") title: String

    ): Response<Products>

    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("products.json?")
    suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        @Query("collection_id") id : String,
        @Query("product_type") title: String

    ): Response<Products>



    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("products.json?")
    suspend fun getSubCategories(): Response<Products>
//    @Headers(
//        "X-Shopify-Access-Token: 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",
//        "Content-Type: application/json"
//    )
//    @GET("orders/{id}.json")
//    suspend fun getAllOrdersById(
//        @Path("id") id: String
//    ): Response<Orders>

//
//    @Headers(
//        "X-Shopify-Access-Token: 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",
//        "Content-Type: application/json"
//    )
//    @GET("orders.json")
//    suspend fun getAllOrdersById(): Response<Orders>




}