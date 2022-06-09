package com.example.clickbuy.network

import android.util.Log
import com.example.clickbuy.models.*
import com.example.clickbuy.util.ConstantsValue
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


private const val TAG = "RetrofitClient"

class RetrofitClient : RemoteSource {

    private val retrofitHelper =
        RetrofitHelper.getClientShopify().create(RetrofitService::class.java)
    private val retrofitCurrencyHelper =
        RetrofitHelper.getClientCurrency().create(RetrofitService::class.java)

    companion object {
        private var instance: RetrofitClient? = null
        fun getInstance(): RetrofitClient {
            return instance ?: RetrofitClient()
        }
    }

    override suspend fun getAllProducts(
        collectionId: String,
        vendor: String,
        productType: String
    ): Response<Products> {
        var response =
            retrofitHelper.getAllProducts(collectionId, vendor, productType)
        Log.i(TAG, "getAllProducts code \n ${response.code()}")
        Log.i(TAG, "getAllProducts body \n ${response.body()}")
        return response
    }

    override suspend fun getAllProductsInCollectionByID(collectionID: String): Response<Products> {
        var response = retrofitHelper.getAllProductsInCollectionByID(collectionID)
        Log.i(TAG, "getAllProductsInCollectionByID code \n ${response.code()}")
        Log.i(TAG, "getAllProductsInCollectionByID body\n ${response.body()}")
        return response
    }

    override suspend fun getProductByID(productId: String): Response<ProductParent> {
        var response = retrofitHelper.getProductById(productId)
        return response
    }

    override suspend fun getCategoryIdByTitle(categoryTitle: String): Response<CustomCollections> {
        var response = retrofitHelper.getCategoryIdByTitle(categoryTitle)
        Log.i(TAG, "getCategoryIdByTitle: " + response.code())

        return response
    }


//
//        override suspend fun getAllCustomCollections(): Response<CustomCollections> {
//            var response = retrofitHelper.getAllCustomCollections()
//            Log.i(TAG, "getAllCustomCollections: responseCode ---->\n ${response.code()}")
//            Log.i(TAG, "getAllCustomCollections: response ---->\n ${response.body()}")
//            return response
//        }


//        override suspend fun getAllSubCategoriesForSpecificCategory(collectionID: String): Response<Products> {
//            var response = retrofitHelper.getAllSubCategoriesForSpecificCategory(collectionID)
//            Log.i(
//                TAG,
//                "getCustomCollectionsByID: responseCode ---->\n ${response.code()}"
//            )
//            Log.i(
//                TAG,
//                "getCustomCollectionsByID: response ---->\n ${response.body()}"
//            )
//            return response
//        }


    override suspend fun getAllBrands(): Response<Brands> {
        var response = retrofitHelper.getAllBrands()
        Log.i(TAG, "getAllBrands: ${response.body()}")
        return response
    }


    override suspend fun getAllProductsInSpecificCollectionByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleComingFromHome: String
    ): Response<Products> {
        var response = retrofitHelper.getAllSubCategoriesForSpecificCategoryByIDAndTitle(
            idCollectionDetails,
            categoryTitleComingFromHome
        )
        return response
    }

    override suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleFromFilter: String
    ): Response<Products> {
        var response = retrofitHelper.getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
            idCollectionDetails,
            categoryTitleFromFilter
        )
        return response
    }

    override suspend fun getSubCategories(): Response<Products> {
        var response = retrofitHelper.getSubCategories()
        return response

    }

    override suspend fun getCustomerDetails(email: String): Response<Customers> {
        var response = retrofitHelper.getCustomerDetails(email)
        Log.i(TAG, "getCustomerDetails: " + response.code())
        return response
    }

    override suspend fun getCurrencies(): Response<Currencies> {
        val response = retrofitHelper.getCurrencies()
        Log.i(TAG, "getCurrencies: " + response.code())
        return response
    }

    override suspend fun getQualifiedValueCurrency(
        to: String
    ): Response<CurrencyConverter> {
        val response = retrofitCurrencyHelper.getQualifiedValueCurrency(to)
        Log.i(TAG, "getQualifiedValueCurrency: " + response.code())
        return response
    }

    override suspend fun getAvailableCoupons(): Response<Coupons> {
        val response = retrofitHelper.getAvailableCoupons()
        Log.i(TAG, "getAvailableCoupons: " + response.code())
        return response
    }

    override suspend fun validateCoupons(code: String): Response<Coupon> {
        val response = retrofitHelper.validateCoupons(code)
        Log.i(TAG, "validateCoupons: " + response.code())
        return response
    }

    //    override suspend fun getAllOrdersById(id: String): Response<Orders> {
//        var response = retrofitHelper.getAllOrdersById(
//        )
//        return response
//    }
    override suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): Response<SubCategories> {
        Log.i(TAG, "getAllSubCategoriesForSpecificCategory: ")
        val response = retrofitHelper.getAllSubCategoriesForSpecificCategory(
            "product_type",
            idCollectionDetails
        )
        Log.i(TAG, "getAllSubCategoriesForSpecificCategory: $response")
        return response
    }

    override suspend fun getAllItemInBag(): Response<ShoppingBag> {
        Log.i(TAG, "getAllItemInBag: draftOrderID--------> " + ConstantsValue.draftOrderID)
        val response = retrofitHelper.getAllItemInBag(ConstantsValue.draftOrderID)
        Log.i(TAG, "getAllItemInBag: $response")
        return response
    }
}