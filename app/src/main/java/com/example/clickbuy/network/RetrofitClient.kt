package com.example.clickbuy.network

import android.util.Log
import com.example.clickbuy.models.*
import com.example.clickbuy.util.ConstantsValue
import retrofit2.Response


class RetrofitClient : RemoteSource {

    private val retrofitHelper =
        RetrofitHelper.getClientShopify().create(RetrofitService::class.java)
    private val retrofitCurrencyHelper =
        RetrofitHelper.getClientCurrency().create(RetrofitService::class.java)
    private val retrofitAddressHelper =
        RetrofitHelper.getClientAddress().create(RetrofitService::class.java)

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
        response.body()

        return response
    }


    override suspend fun getProductByID(productId: String): Response<ProductParent> {
        val response = retrofitHelper.getProductById(productId)
        return response
    }
    override suspend fun getAllBrands(): Response<Brands> {
        val response = retrofitHelper.getAllBrands()
        return response
    }



    override suspend fun getSubCategories(): Response<Products> {
        val response = retrofitHelper.getSubCategories()
        return response
    }

    override suspend fun getCustomerDetails(email: String): Response<Customers> {
        val response = retrofitHelper.getCustomerDetails(email)
        return response
    }

    override suspend fun updateCustomerDetails(customer: CustomerParent): Response<CustomerParent> {
        val response = retrofitHelper.updateCustomerDetails(ConstantsValue.userID, customer)
        return response
    }

    override suspend fun updateCustomerDetailsTest(customer: CustomersTest): Response<CustomersTest> {
        val response = retrofitHelper.updateCustomerDetailsTest(ConstantsValue.userID, customer)
        return response
    }

    override suspend fun getAllAddresses(): Response<CustomerAddresses> {
        val response = retrofitHelper.getAllAddresses(ConstantsValue.userID)
        return response
    }

    override suspend fun addAddress(address: CustomerAddressUpdate): Response<CustomerAddressResponse> {
        Log.i("AddressViewModel", "end addAddress: repository")
        val response = retrofitHelper.addAddress(ConstantsValue.userID, address)
        Log.i("AddressViewModel", "addAddress: retrofitClient")
        return response
    }

    override suspend fun getAddressFromApi(placeName: String): Response<AddressResponseAPI> {
        val response = retrofitAddressHelper.getAddressFromApi(placeName)
        return response
    }

    override suspend fun deleteAddress(addressID: Long): Response<Any> {

        val response = retrofitHelper.deleteAddress(customerId = ConstantsValue.userID, addressId = addressID.toString())
        return response
    }

    override suspend fun getCurrencies(): Response<Currencies> {
        val response = retrofitHelper.getCurrencies()
        return response
    }

    override suspend fun getQualifiedValueCurrency(
        to: String
    ): Response<CurrencyConverter> {
        val response = retrofitCurrencyHelper.getQualifiedValueCurrency(to)
        return response
    }

    override suspend fun getAvailableCoupons(): Response<Coupons> {
        val response = retrofitHelper.getAvailableCoupons()
        return response
    }

    override suspend fun validateCoupons(code: String): Response<Coupon> {
        val response = retrofitHelper.validateCoupons(code)
        return response
    }

    override suspend fun getAllOrdersForSpecificCustomerById(id: String): Response<Orders> {
        val response = retrofitHelper.getAllOrdersForSpecificCustomerById(id)
        return response
    }


    override suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): Response<SubCategories> {
        val response = retrofitHelper.getAllSubCategoriesForSpecificCategory(
            "product_type",
            idCollectionDetails
        )
        return response
    }

    override suspend fun getAllItemsInBag(): Response<ShoppingBag> {
        val response = retrofitHelper.getAllItemsInBag(ConstantsValue.draftOrderID)
        return response
    }

    override suspend fun updateItemsInBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {
        val response = retrofitHelper.updateItemsInBag(ConstantsValue.draftOrderID, shoppingBag)
        return response
    }


    override suspend fun createBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {
        val response = retrofitHelper.createBag(shoppingBag)
        return response
    }

    override suspend fun signIn(email: String): Response<Customers> {
        return retrofitHelper.signIn(email)
    }

    override suspend fun registerCustomer(customer: CustomerParent): Response<CustomerParent> {
        return retrofitHelper.registerCustomer(customer)
    }

    override suspend fun postOrders(order: OrderPojo): Response<OrderPojo> {
        val response = retrofitHelper.postOrders(order)
        return response
    }

    override suspend fun getDraftOrders(): Response<Favourites> {
        val response = retrofitHelper.getFavourites()
        return response
    }

    override suspend fun addFavourite(favorite: FavouriteParent): Response<FavouriteParent> {
        val response = retrofitHelper.addFavourite(favorite)
        return response
    }

    override suspend fun removeFavourite(id: String): Response<Any> {
        return retrofitHelper.removeFavourite(id)
    }

}