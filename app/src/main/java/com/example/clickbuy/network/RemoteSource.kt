package com.example.clickbuy.network

import com.example.clickbuy.models.*
import retrofit2.Response

interface RemoteSource {

<<<<<<< Updated upstream
    suspend fun getAllProducts():  Response<Products>
    suspend fun getAllProductsInCollectionByID(collectionID: String):Response<Products>
    suspend fun getCustomCollectionsByID(collectionID: String): Response<CustomCollectionElement>
    suspend fun getAllCustomCollections():  Response<CustomCollections>

    suspend fun getAvailableCoupons():  Response<Coupon>
    suspend fun getAvailableAds():  Response<Ads>
    suspend fun getAllSubCategoriesForSpecificCategory(collectionID: String): Response<Products>
=======
    suspend fun getAllProducts(): Response<Products>
    suspend fun getAllProductsInCollectionByID(collectionID: String): Response<Products>
    suspend fun getProductByID(productId: String): Response<ProductParent>
    //suspend fun getCustomCollectionsByID(collectionID: String): Response<CustomCollectionElement>

    suspend fun getCategoryIdByTitle(categoryTitle: String): Response<CustomCollections>
    suspend fun getAllBrands(): Response<Brands>

    // suspend fun getAllSubCategoriesForSpecificCategory(collectionID: String): Response<Products>
    suspend fun getAllProductsInSpecificCollectionByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleComingFromHome: String
    ): Response<Products>
//  suspend fun getCustomCollectionsByID(collectionID: String): Response<CustomCollectionElement>


    suspend fun getCustomerDetails(email: String): Response<Customers>
    suspend fun getCurrencies(): Response<Currencies>
>>>>>>> Stashed changes
}