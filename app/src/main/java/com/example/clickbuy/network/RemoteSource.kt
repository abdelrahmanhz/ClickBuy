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
    // suspend fun getAllSubCategoriesForSpecificCategory(collectionID: String): Response<Products>
    //  suspend fun getCustomCollectionsByID(collectionID: String): Response<CustomCollectionElement>
    suspend fun signIn(email: String): Response<Customers>
    suspend fun registerCustomer(customer: CustomerParent): Response<CustomerParent>

    suspend fun getAllOrdersForSpecificCustomerById(id: String): Response<Orders>
    suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): Response<SubCategories>

    suspend fun getCustomerDetails(email: String): Response<Customers>
    suspend fun getCurrencies(): Response<Currencies>
    suspend fun getQualifiedValueCurrency(to: String): Response<CurrencyConverter>

    suspend fun getAvailableCoupons(): Response<Coupons>
    suspend fun validateCoupons(code: String): Response<Coupon>

    // Favourites
    suspend fun getDraftOrders(): Response<DraftOrders>
    suspend fun addFavourite(favorite: DraftOrderParent): Response<DraftOrderParent>
    suspend fun removeFavourite(id: String): Response<Any>

    suspend fun getAllItemInBag(): Response<ShoppingBag>
    suspend fun updateItemsInBag(shoppingBag: ShoppingBag): Response<ShoppingBag>
}