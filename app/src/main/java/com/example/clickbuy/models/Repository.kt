package com.example.clickbuy.models

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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

    private var sharedPrefs: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RetrofitClient, context: Context
        ): Repository {
            return instance ?: Repository(remoteSource, context)
        }
    }

    init {
        this.sharedPrefs = context.getSharedPreferences("DeviceToken", MODE_PRIVATE)
        this.editor = sharedPrefs!!.edit()
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

    override suspend fun getAllOrdersForSpecificCustomerById(id: String): Response<Orders> {
        return remoteSource.getAllOrdersForSpecificCustomerById(id)

    }

    override suspend fun signIn(email: String, password: String): String {
        val responseMessage: String
        val response =  remoteSource.signIn(email)
        if (response.code() == 200){
            if (response.body()?.customers.isNullOrEmpty()){
                responseMessage = "No such user"
            } else {
                // shared pref
                if (response.body()?.customers!![0].tags == password) {
                    responseMessage = "Logged in successfully"
                    editor?.putBoolean("IS_LOGGING", true)
                    editor?.putLong("USER_ID", response.body()!!.customers[0].id!!)
                    editor?.putString("USER_EMAIL", response.body()!!.customers[0].email)
                    editor?.apply()
                } else
                    responseMessage = "Entered a wrong password"
            }
        } else {
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

    override suspend fun getFavourites(): Response<DraftOrders> {
        val response = remoteSource.getDraftOrders()
        if (response.code() == 200 && !response.body()?.draft_orders.isNullOrEmpty()){
            Log.i(TAG, "getFavourites")
            // filter with user id too
            val email = sharedPrefs?.getString("USER_EMAIL", "")
            if (!email.isNullOrEmpty())
                response.body()?.draft_orders = response.body()?.draft_orders?.filter { it.note == "fav" && it.email == email}
        }
        return response
    }

    override suspend fun addFavourite(favorite: DraftOrderParent): Response<DraftOrderParent> {
        val email = sharedPrefs?.getString("USER_EMAIL", "")
        favorite.draft_order?.email = email
        return remoteSource.addFavourite(favorite)
    }

    override suspend fun deleteFavourite(favId: String) {
        remoteSource.removeFavourite(favId)
    }
}