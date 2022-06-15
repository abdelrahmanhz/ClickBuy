package com.example.clickbuy.network

import android.util.Log
import com.example.clickbuy.models.*
import com.example.clickbuy.util.ConstantsValue
import retrofit2.Response


private const val TAG = "RetrofitClient"

class RetrofitClient : RemoteSource {

    private val retrofitHelper =
        RetrofitHelper.getClientShopify().create(RetrofitService::class.java)
    private val retrofitCurrencyHelper =
        RetrofitHelper.getClientCurrency().create(RetrofitService::class.java)

    companion object {
        private var instance: RetrofitClient? = null
        fun getInstance(): RetrofitClient {
            if (instance == null)
                instance = RetrofitClient()
            return instance!!
        }
    }

    override suspend fun getAllProducts(
        collectionId: String,
        vendor: String,
        productType: String
    ): Response<Products> {
        val response =
            retrofitHelper.getAllProducts(collectionId, vendor, productType)
        Log.i(TAG, "getAllProducts code \n ${response.code()}")
        Log.i(TAG, "getAllProducts body \n ${response.body()}")
        return response
    }

    override suspend fun getAllProductsInCollectionByID(collectionID: String): Response<Products> {
        val response = retrofitHelper.getAllProductsInCollectionByID(collectionID)
        Log.i(TAG, "getAllProductsInCollectionByID code \n ${response.code()}")
        Log.i(TAG, "getAllProductsInCollectionByID body\n ${response.body()}")
        return response
    }

    override suspend fun getProductByID(productId: String): Response<ProductParent> {
        val response = retrofitHelper.getProductById(productId)
        Log.i(TAG, "getProductByID: response.code()----> " + response.code())
        return response
    }

    override suspend fun getCategoryIdByTitle(categoryTitle: String): Response<CustomCollections> {
        val response = retrofitHelper.getCategoryIdByTitle(categoryTitle)
        Log.i(TAG, "getCategoryIdByTitle: " + response.code())

        return response
    }

    override suspend fun getAllBrands(): Response<Brands> {
        val response = retrofitHelper.getAllBrands()
        Log.i(TAG, "getAllBrands: ${response.body()}")
        return response
    }


    override suspend fun getAllProductsInSpecificCollectionByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleComingFromHome: String
    ): Response<Products> {
        val response = retrofitHelper.getAllSubCategoriesForSpecificCategoryByIDAndTitle(
            idCollectionDetails,
            categoryTitleComingFromHome
        )
        Log.i(
            TAG,
            "getAllProductsInSpecificCollectionByIDAndTitle: response.code()----> " + response.code()
        )
        return response
    }

    override suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleFromFilter: String
    ): Response<Products> {
        val response = retrofitHelper.getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
            idCollectionDetails,
            categoryTitleFromFilter
        )
        Log.i(
            TAG,
            "getAllProductsInSpecificCollectionByIDAndTitle: response.code()----> " + response.code()
        )
        return response
    }

    override suspend fun getSubCategories(): Response<Products> {
        val response = retrofitHelper.getSubCategories()
        Log.i(TAG, "getSubCategories: response.code()----> " + response.code())
        return response
    }

    override suspend fun getCustomerDetails(email: String): Response<Customers> {
        val response = retrofitHelper.getCustomerDetails(email)
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

    override suspend fun getAllOrdersForSpecificCustomerById(id: String): Response<Orders> {
        val response = retrofitHelper.getAllOrdersForSpecificCustomerById(id)
        Log.i(TAG, "getAllOrdersForSpecificCustomerById: $response")
        return response
    }

    override suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): Response<SubCategories> {
        Log.i(TAG, "getAllSubCategoriesForSpecificCategory: ")
        val response = retrofitHelper.getAllSubCategoriesForSpecificCategory(
            "product_type",
            idCollectionDetails
        )
        Log.i(TAG, "getAllSubCategoriesForSpecificCategory: $response")
        return response
    }

    override suspend fun getAllItemsInBag(): Response<ShoppingBag> {
        Log.i(TAG, "getAllItemInBag: draftOrderID--------> " + ConstantsValue.draftOrderID)
        val response = retrofitHelper.getAllItemsInBag(ConstantsValue.draftOrderID)
        Log.i(TAG, "getAllItemInBag: $response")
        return response
    }

    override suspend fun updateItemsInBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {
        Log.i(TAG, "updateItemsInBag: draftOrderID--------> " + ConstantsValue.draftOrderID)
        val response = retrofitHelper.updateItemsInBag(ConstantsValue.draftOrderID, shoppingBag)
        Log.i(TAG, "updateItemsInBag: $response")
        return response
    }

    /* override suspend fun addItemsInBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {

     }*/


    override suspend fun signIn(email: String): Response<Customers> {
        return retrofitHelper.signIn(email)
    }

    override suspend fun registerCustomer(customer: CustomerParent): Response<CustomerParent> {
        return retrofitHelper.registerCustomer(customer)
    }

}