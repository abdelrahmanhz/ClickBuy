package com.example.clickbuy.models

import androidx.lifecycle.LiveData
import retrofit2.Response

interface RepositoryInterface {

    //Network
    suspend fun getAllBrands(): Response<Brands>

    //suspend fun getAllBrandsDetais(id : String): Response<Products>
    //suspend fun getSalesId(): Response<CustomCollections>

    //suspend fun getAllBrandsDetais(id: String): Response<Products>


    suspend fun getAllProducts(
        idCollectionDetails: String,
        categoryTitleComing: String,
        subCategory: String
    )
            : Response<Products>

    suspend fun getSubCategories(): Response<Products>

    //suspend fun getAllBrandsDetais(id : String): Response<Products>
    //suspend fun getSalesId(): Response<CustomCollections>
    suspend fun getProductById(productId: String): Response<ProductParent>

    //suspend fun getAllBrandsDetais(id: String): Response<Products>
    suspend fun getAllProductsInCollectionByID(id: String): Response<Products>
    suspend fun getCategoryIdByTitle(categoryTitle: String): Response<CustomCollections>
    suspend fun getAllProductsInSpecificCollectionByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleComingFromHome: String
    ): Response<Products>


    // room
    suspend fun addFavorite(favorite: Favorite)
    suspend fun getFavorites(): List<Favorite>
    suspend fun deleteFavorite(productId: Long)
    suspend fun isFavorite(productId: Long): Boolean

    suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleFromFilter: String
    ): Response<Products>

    suspend fun getCustomerDetails(email: String): Response<Customers>
    suspend fun getCurrencies(): Response<Currencies>
    suspend fun getQualifiedValueCurrency(to: String): Response<CurrencyConverter>
}