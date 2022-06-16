package com.example.clickbuy.network

import com.example.clickbuy.models.*
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Query

interface RetrofitService {

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("products.json")
    suspend fun getAllProducts(

        @Query("collection_id") id: String,
        @Query("vendor") vendor: String,
        @Query("product_type") title: String
    ): Response<Products>

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("collections/{id}/products.json")
    suspend fun getAllProductsInCollectionByID(@Path("id") id: String): Response<Products>

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("custom_collections.json?title={categoryTitle}")
    suspend fun getCategoryIdByTitle(
        @Path("categoryTitle") categoryTitle: String
    ): Response<CustomCollections>


    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("smart_collections.json")
    suspend fun getAllBrands(): Response<Brands>


    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("products/{id}.json")
    suspend fun getProductById(@Path("id") id: String): Response<ProductParent>

    //Get all Coupons
    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("price_rules/1089622311051/discount_codes.json")
    suspend fun getAvailableCoupons(): Response<Coupons>

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("discount_codes/lookup.json?")
    suspend fun validateCoupons(
        @Query("code") code: String
    ): Response<Coupon>


    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("products.json?")
    suspend fun getAllSubCategoriesForSpecificCategory(
        @Query("fields") product_type: String,
        @Query("collection_id") id: String
    ): Response<SubCategories>

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("products.json?")
    suspend fun getAllSubCategoriesForSpecificCategoryByIDAndTitle(
        @Query("collection_id") id: String,
        @Query("vendor") title: String
    ): Response<Products>

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("products.json?")
    suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        @Query("collection_id") id: String,
        @Query("product_type") title: String
    ): Response<Products>


    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("products.json?")
    suspend fun getSubCategories(): Response<Products>

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("customers/{id}/orders.json")
    suspend fun getAllOrdersForSpecificCustomerById(
        @Path("id") id: String
    ): Response<Orders>

    //Customer Details
    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("customers.json?")
    suspend fun getCustomerDetails(
        @Query("email") email: String
    ): Response<Customers>

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @PUT("customers/{id}.json")
    suspend fun updateCustomerDetails(
        @Path("id") id: String, @Body customer: CustomerParent
    ): Response<CustomerParent>

    //Address
    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("customers.json?")
    suspend fun getAllAddresses(
        @Query("email") email: String
    ): Response<Addresses>

    //Currency
    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("currencies.json")
    suspend fun getCurrencies(
    ): Response<Currencies>

    @GET("convert?apikey=UzDffIvEj5rwG6iHMLxXMS5Cz4jsyYBK&amount=1&from=EGP")
    suspend fun getQualifiedValueCurrency(
        @Query("to") to: String
    ): Response<CurrencyConverter>


    //Bag
    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("draft_orders/{id}.json")
    suspend fun getAllItemsInBag(@Path("id") id: String): Response<ShoppingBag>

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @PUT("draft_orders/{id}.json")
    suspend fun updateItemsInBag(
        @Path("id") id: String,
        @Body shoppingBag: ShoppingBag
    ): Response<ShoppingBag>

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @POST("draft_orders.json")
    suspend fun createBag(
        @Body shoppingBag: ShoppingBag
    ): Response<ShoppingBag>

    //Login & SignUp
    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @GET("customers.json?")
    suspend fun signIn(
        @Query("email") email: String
    ): Response<Customers>

    @Headers(RetrofitHelper.HEADERS_ACCESS_TOKEN, RetrofitHelper.HEADERS_CONTENT_TYPE)
    @POST("customers.json")
    suspend fun registerCustomer(@Body customerParent: CustomerParent): Response<CustomerParent>

}