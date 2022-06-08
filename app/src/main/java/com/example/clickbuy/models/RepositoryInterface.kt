package com.example.clickbuy.models

import androidx.lifecycle.LiveData
import retrofit2.Response

interface RepositoryInterface {

    //Network
    suspend fun getAllBrands(): Response<Brands>
    suspend fun getAllProducts(
        collectionId: String,
        vendor: String,
        productType: String
    )
            : Response<Products>

    suspend fun getSubCategories(): Response<Products>
    suspend fun getProductById(productId: String): Response<ProductParent>
    suspend fun getAllProductsInCollectionByID(id: String): Response<Products>
    suspend fun getCategoryIdByTitle(categoryTitle: String): Response<CustomCollections>
    suspend fun getAllProductsInSpecificCollectionByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleComingFromHome: String
    ): Response<Products>

    //suspend fun getAllOrdersById(id: String): Response<Orders>


    // room
    suspend fun addFavorite(favorite: Favorite)
    suspend fun getFavorites(): List<Favorite>
    suspend fun deleteFavorite(productId: Long)
    suspend fun isFavorite(productId: Long): Boolean

    suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleFromFilter: String
    ): Response<Products>

    suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): Response<SubCategories>
}