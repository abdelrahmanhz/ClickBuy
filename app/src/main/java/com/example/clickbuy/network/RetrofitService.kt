package com.example.clickbuy.network

import com.example.clickbuy.models.*
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.Response

interface RetrofitService {
    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("products.json")
    suspend fun getAllProducts(): Response<Products>

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
    @GET("custom_collections/{id}.json")
    suspend fun getCustomCollectionsByID(
        @Path("id") id: String
    ): Response<CustomCollectionElement>

    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("custom_collections.json")
    suspend fun getAllCustomCollections(): Response<CustomCollections>



    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("collections/{id}/products.json?fields=product_type")
    suspend fun getAllSubCategoriesForSpecificCategory(
        @Path("id") id: String
    ): Response<Products>


    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET()
    suspend fun getAvailableAds(): Response<Ads>


    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET()
    suspend fun getAvailableCoupons(): Response<Coupon>

    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("customers.json?")
    suspend fun getCustomerDetails(
        @Query("email") email: String
    ): Response<Customers>


    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("currencies.json")
    suspend fun getCurrencies(
    ): Response<Currencies>


}