package com.example.clickbuy.network

import com.example.clickbuy.models.*
import retrofit2.Response

interface RemoteSource {

    suspend fun getAllProducts():  Response<Products>
    suspend fun getAllProductsInCollectionByID(collectionID: String):Response<Products>
    suspend fun getCustomCollectionsByID(collectionID: String): Response<CustomCollectionElement>
    suspend fun getAllCustomCollections():  Response<CustomCollections>
}