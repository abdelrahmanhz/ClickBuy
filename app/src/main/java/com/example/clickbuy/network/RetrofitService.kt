package com.example.clickbuy.network

import com.example.clickbuy.models.*
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Query

interface RetrofitService {

    // Get all products in APP
    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("products.json")
    suspend fun getAllProducts(): Response<Products>


    //all products in any category
    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("collections/{id}/products.json")
    suspend fun getAllProductsInCollectionByID(@Path("id") id: String): Response<Products>

    // get any category ID of custom collection (MAN ,WOMEN, KIDS)
    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("custom_collections.json?title={categoryTitle}")
    suspend fun getCategoryIdByTitle(
        @Path("categoryTitle") categoryTitle: String
    ): Response<CustomCollections>


    //Get all Brands
    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("smart_collections.json")
    suspend fun getAllBrands(): Response<Brands>


    // get all categories custom collection (MAN ,WOMEN, KIDS)
    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET("custom_collections.json")
    suspend fun getAllCustomCollections(): Response<CustomCollections>

    //Get all Coupons
    @Headers(
        "X-Shopify-Access-Token: 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",
        "Content-Type: application/json"
    )
    @GET()
    suspend fun getAvailableCoupons(): Response<Coupon>

    /*  @Headers(
          "X-Shopify-Access-Token: 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",
          "Content-Type: application/json"
      )
      @GET("custom_collections/{id}.json")
      suspend fun getCustomCollectionsByID(
          @Path("id") id: String
      ): Response<CustomCollectionElement>
      */

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
    @GET("products.json?")
    suspend fun getAllSubCategoriesForSpecificCategoryByIDAndTitle(
        @Query("collection_id") id : String,
        @Query("vendor") title: String

    ): Response<Products>



}