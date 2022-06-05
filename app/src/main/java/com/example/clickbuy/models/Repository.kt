package com.example.clickbuy.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.clickbuy.db.LocalSource
import com.example.clickbuy.network.RemoteSource
import com.example.clickbuy.network.RetrofitClient
import retrofit2.Response


private const val TAG = "Repository"

class Repository private constructor(
    var remoteSource: RemoteSource,
    var localSource: LocalSource,
    var context: Context
) : RepositoryInterface {


    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RetrofitClient, localSource: LocalSource, context: Context
        ): Repository {

            return instance ?: Repository(remoteSource, localSource, context)
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

    // local (room)
    override fun addFavorite(favorite: Favorite) {
        localSource.insertFavorite(favorite)
    }

    override fun getFavorites(): LiveData<List<Favorite>> {
        return localSource.getFavorites()
    }

    override fun deleteFavorite(productId: Long) {
        localSource.deleteFavorite(productId)
    }

    override fun isFavorite(productId: Long): Boolean {
        return localSource.isFavorite(productId)
    }

}