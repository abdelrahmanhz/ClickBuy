package com.example.clickbuy.models

import retrofit2.Response

interface RepositoryInterface {

    suspend fun getAllBrands(): Response<Brands>
    suspend fun getAllBrandsDetais(id : String): Response<Products>
    suspend fun getSalesId(): Response<CustomCollections>
    suspend fun getProductById(productId: String): Response<ProductParent>
}