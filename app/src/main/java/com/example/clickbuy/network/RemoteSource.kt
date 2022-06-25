package com.example.clickbuy.network

import com.example.clickbuy.models.*
import retrofit2.Response


interface RemoteSource {
    suspend fun getAllProductsInCollectionByID(collectionID: String): Response<Products>
    suspend fun getProductByID(productId: String): Response<ProductParent>
    suspend fun getAllProducts(
        collectionId: String,
        vendor: String,
        productType: String
    ): Response<Products>

    suspend fun getCategoryIdByTitle(categoryTitle: String): Response<CustomCollections>
    suspend fun getAllBrands(): Response<Brands>
    suspend fun getAllProductsInSpecificCollectionByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleComingFromHome: String
    ): Response<Products>

    suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleFromFilter: String
    ): Response<Products>

    suspend fun getSubCategories(): Response<Products>
    suspend fun signIn(email: String): Response<Customers>
    suspend fun registerCustomer(customer: CustomerParent): Response<CustomerParent>

    suspend fun getAllOrdersForSpecificCustomerById(id: String): Response<Orders>
    suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): Response<SubCategories>

    //Customer
    suspend fun getCustomerDetails(email: String): Response<Customers>
    suspend fun updateCustomerDetails(customer: CustomerParent): Response<CustomerParent>
    suspend fun updateCustomerDetailsTest(customer: CustomersTest): Response<CustomersTest>
    suspend fun getAllAddresses(): Response<CustomerAddresses>
    suspend fun addAddress(address: CustomerAddressUpdate): Response<CustomerAddressResponse>
    suspend fun getAddressFromApi(placeName: String): Response<AddressResponseAPI>

    suspend fun getCurrencies(): Response<Currencies>
    suspend fun getQualifiedValueCurrency(to: String): Response<CurrencyConverter>

    suspend fun getAvailableCoupons(): Response<Coupons>
    suspend fun validateCoupons(code: String): Response<Coupon>

    suspend fun getAllItemsInBag(): Response<ShoppingBag>
    suspend fun updateItemsInBag(shoppingBag: ShoppingBag): Response<ShoppingBag>
    suspend fun createBag(shoppingBag: ShoppingBag): Response<ShoppingBag>
    // Favourites
    suspend fun getDraftOrders(): Response<Favourites>
    suspend fun addFavourite(favorite: FavouriteParent): Response<FavouriteParent>
    suspend fun removeFavourite(id: String): Response<Any>

    suspend fun getAllAddresesForSpecificCustomer(id: String): Response<Addresses>
    suspend fun postOrders( order: OrderPojo): Response<OrderPojo>

}