package com.example.clickbuy.network

import android.util.Log
import com.example.clickbuy.models.*
import retrofit2.Response


private const val TAG = "RetrofitClient"

class RetrofitClient : RemoteSource {

    private val retrofitHelper = RetrofitHelper.getClient().create(RetrofitService::class.java)

    companion object {
        private var instance: RetrofitClient? = null
        fun getInstance(): RetrofitClient {
            return instance ?: RetrofitClient()
        }
    }

    override suspend fun getAllProducts(): Response<Products> {
        var response = retrofitHelper.getAllProducts()
        Log.i(TAG, "getAllProducts code \n ${response.code()}")
        Log.i(TAG, "getAllProducts body \n ${response.body()}")
        return response
    }

    override suspend fun getAllProductsInCollectionByID(collectionID: String): Response<Products> {
        var response = retrofitHelper.getAllProductsInCollectionByID("273053679755")
        Log.i(TAG, "getAllProductsInCollectionByID code \n ${response.code()}")
        Log.i(TAG, "getAllProductsInCollectionByID body\n ${response.body()}")
        return response
    }

    override suspend fun getCustomCollectionsByID(collectionID: String): Response<CustomCollectionElement> {
        var response = retrofitHelper.getCustomCollectionsByID(collectionID)
        Log.i(TAG, "getCustomCollectionsByID: responseCode ---->\n ${response.code()}")
        Log.i(TAG, "getCustomCollectionsByID: response ---->\n ${response.body()}")
        return response
    }

    override suspend fun getAllCustomCollections(): Response<CustomCollections> {
        var response = retrofitHelper.getAllCustomCollections()
        Log.i(TAG, "getAllCustomCollections: responseCode ---->\n ${response.code()}")
        Log.i(TAG, "getAllCustomCollections: response ---->\n ${response.body()}")
        return response
    }
}