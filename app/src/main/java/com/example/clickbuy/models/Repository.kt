package com.example.clickbuy.models

import android.content.Context
import android.util.Log
import com.example.clickbuy.network.RemoteSource
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.util.ConstantsValue
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

    override suspend fun getAllBrands(): Response<Brands> {
        Log.i(TAG, "getAllBrands: ")
        return remoteSource.getAllBrands()

    }

    override suspend fun getAllProducts(
        collectionId: String,
        vendor: String,
        productType: String
    ): Response<Products> {
        return remoteSource.getAllProducts(collectionId, vendor, productType)

    }

    override suspend fun getSubCategories(): Response<Products> {
        return remoteSource.getSubCategories()
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

//    override suspend fun getAllOrdersById(id: String): Response<Orders> {
//        return remoteSource.getAllOrdersById(id)
//
//    }

    override suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleFromFilter: String
    ): Response<Products> {
        return remoteSource.getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
            idCollectionDetails,
            categoryTitleFromFilter
        )
    }

    override suspend fun getProductById(productId: String): Response<ProductParent> {
        Log.i(TAG, "getProductByID: ")
        val response = remoteSource.getProductByID(productId)
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

    override suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): Response<SubCategories> {
        Log.i(TAG, "getAllSubCategoriesForSpecificCategory: ")
        val response = remoteSource.getAllSubCategoriesForSpecificCategory(idCollectionDetails)
        Log.i(TAG, "getAllSubCategoriesForSpecificCategory: $response")
        return response

    }

    override suspend fun getCustomerDetails(email: String): Response<Customers> {
        val response = remoteSource.getCustomerDetails(email)
        Log.i(TAG, "getCustomerDetails: " + response.code())
        return response
    }

    override suspend fun getCurrencies(): Response<Currencies> {
        val response = remoteSource.getCurrencies()
        Log.i(TAG, "getCurrencies: " + response.code())
        return response
    }

    override suspend fun getQualifiedValueCurrency(
        to: String
    ): Response<CurrencyConverter> {
        val response = remoteSource.getQualifiedValueCurrency(to)
        Log.i(TAG, "getQualifiedValueCurrency: " + response.code())
        return response
    }

    override suspend fun getAvailableCoupons(): Response<Coupons> {
        val response = remoteSource.getAvailableCoupons()
        Log.i(TAG, "getAvailableCoupons: " + response.code())
        return response
    }

    override suspend fun validateCoupons(code: String): Response<Coupon> {
        val response = remoteSource.validateCoupons(code)
        Log.i(TAG, "validateCoupons: " + response.code())
        return response
    }

    override suspend fun getAllItemInBag(): Response<ShoppingBag> {
        val response = remoteSource.getAllItemInBag()
        Log.i(TAG, "getAllItemInBag: $response")
        return response
    }

    override suspend fun updateItemsInBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {
        Log.i(TAG, "updateItemsInBag: draftOrderID--------> " + ConstantsValue.draftOrderID)
        val response = remoteSource.updateItemsInBag(shoppingBag)
        Log.i(TAG, "updateItemsInBag: $response")
        return response
    }
}
