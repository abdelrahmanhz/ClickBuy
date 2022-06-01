package com.example.clickbuy.models

import retrofit2.Response

interface RepositoryInterface {

    //Network
    suspend fun getAllBrands(): Response<Brands>

    //suspend fun getAllBrandsDetais(id : String): Response<Products>
    //suspend fun getSalesId(): Response<CustomCollections>
    suspend fun getProductById(productId: String): Response<ProductParent>

    //suspend fun getAllBrandsDetais(id: String): Response<Products>
    suspend fun getAllProductsInCollectionByID(id: String): Response<Products>
    suspend fun getCategoryIdByTitle(categoryTitle: String): Response<CustomCollections>
    suspend fun getAllProductsInSpecificCollectionByIDAndTitle(idCollectionDetails : String,
                             categoryTitleComingFromHome : String): Response<Products>

}