package com.example.clickbuy.models

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.example.clickbuy.network.RemoteSource
import com.example.clickbuy.network.RetrofitClient
import retrofit2.Response


private const val TAG = "Repository"

class Repository private constructor(
    var remoteSource: RemoteSource,
    var context: Context
) : RepositoryInterface {

    private var sharedPrefs: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

            companion object {
        var instance: Repository? = null

        fun getInstance(
            remoteSource: RetrofitClient, context: Context
        ): Repository {

            return instance ?: Repository(remoteSource, context)
        }
    }

    init {
        this.remoteSource = remoteSource
        this.sharedPrefs = context.getSharedPreferences("DeviceToken", MODE_PRIVATE)
        this.editor = sharedPrefs!!.edit()
    }

    override suspend fun getAllBrands(): Response<Brands> {
        Log.i(TAG, "getAllBrands: ")
        return remoteSource.getAllBrands()

    }

    override suspend fun getAllProducts(idCollectionDetails : String , categoryTitleComing : String , subCategory : String): Response<Products> {
        return remoteSource.getAllProducts(idCollectionDetails ,categoryTitleComing , subCategory)
    }

    override suspend fun getSubCategories(): Response<Products> {
        return  remoteSource.getSubCategories()
    }


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
        return remoteSource.getAllProductsInSpecificCollectionByIDAndTitle(
            idCollectionDetails,
            categoryTitleComingFromHome
        )
    }

    override suspend fun signIn(email: String, password: String): String {
        var responseMessage: String = ""
        val response =  remoteSource.signIn(email)
        if (response.code() == 200){
            if (response.body()?.customers.isNullOrEmpty()){
                responseMessage = "No such user"
            }
            else {
                // shared pref
                if (response.body()?.customers!![0].tags == password){
                    responseMessage = "Logged in successfully"
                    editor?.putBoolean("IS_LOGGING", true)
                    editor?.putLong("USER_ID", response.body()!!.customers[0].id!!)
                    editor?.apply()
                }
                else
                    responseMessage = "Entered a wrong password"
            }
        }
        else{
            responseMessage = "Something went wrong"
        }
        return responseMessage
    }

    override suspend fun registerCustomer(customer: CustomerParent): Response<CustomerParent> {
        return remoteSource.registerCustomer(customer)
    }

    override suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleFromFilter: String
    ): Response<Products> {
        return remoteSource.getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(idCollectionDetails,categoryTitleFromFilter)
    }

    override suspend fun getProductById(productId: String): Response<ProductParent> {
        Log.i(TAG, "getProductByID: ")
        var response = remoteSource.getProductByID(productId)
        Log.i(TAG, "getProductByID: $response")
        return response
    }


    // local (room)
    override suspend fun addFavorite(favorite: Favorite) {
        //localSource.insertFavorite(favorite)
    }

    override suspend fun getFavorites(): List<Favorite> {
       //return localSource.getFavorites()
        return emptyList()
    }

    override suspend fun deleteFavorite(productId: Long) {
        //localSource.deleteFavorite(productId)
    }

    override suspend fun isFavorite(productId: Long): Boolean {
        //return localSource.isFavorite(productId)
        return false
    }

}