package com.example.clickbuy.models

import android.content.Context
import android.util.Log
import com.example.clickbuy.network.RemoteSource
import com.example.clickbuy.network.RetrofitClient
import retrofit2.Response


private const val TAG = "Repository"

class Repository private constructor(
    var remoteSource: RemoteSource,
    var context: Context
) : RepositoryInterface {


    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RetrofitClient, context: Context
        ): Repository {

            return instance ?: Repository(remoteSource, context)
        }
    }

    init {
        this.remoteSource = remoteSource
    }

    override suspend fun getAllBrands(): Response<Brands> {
        Log.i(TAG, "getAllBrands: ")
        return remoteSource.getAllBrands()

    }

//    override suspend fun getAllBrandsDetais(id: String): Response<Products> {
//        Log.i(TAG, "getAllBrandsDetails: ")
//        return remoteSource.getAllProductsInCollectionByID(id)
//    }

    override suspend fun getAllProductsInCollectionByID(id: String): Response<Products> {
        Log.i(TAG, "getAllSalesById: ")
        return remoteSource.getAllProductsInCollectionByID(id)
    }

    override suspend fun getCategoryIdByTitle(categoryTitle: String): Response<CustomCollections> {
        return remoteSource.getCategoryIdByTitle(categoryTitle)
    }

    override suspend fun getAllProductsInSpecificCollectionByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleComingFromHome: String
    ): Response<Products> {
      return remoteSource.getAllProductsInSpecificCollectionByIDAndTitle(idCollectionDetails,categoryTitleComingFromHome)
    }

    override suspend fun getProductById(productId: String): Response<ProductParent> {
        Log.i(TAG, "getProductByID: ")
        var response = remoteSource.getProductByID(productId)
        Log.i(TAG, "getProductByID: $response")
        return response
    }

    override suspend fun getCustomerDetails(email: String): Response<Customers> {
        var response = remoteSource.getCustomerDetails(email)
        Log.i(TAG, "getCustomerDetails: " + response.code())
        return response
    }

    override suspend fun getCurrencies(): Response<Currencies> {
        var response = remoteSource.getCurrencies()
        Log.i(TAG, "getCurrencies: " + response.code())
        return response
    }

}