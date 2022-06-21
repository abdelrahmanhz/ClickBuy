package com.example.clickbuy

import com.example.clickbuy.models.*
import retrofit2.Response

interface FakeRemoteSource {

        suspend fun getAllProductsInCollectionByID(collectionID: String): Products
        suspend fun getProductByID(productId: String): Response<ProductParent>
        suspend fun getAllProducts(
            collectionId: String,
            vendor: String,
            productType: String
        ): Response<Products>

        suspend fun getCategoryIdByTitle(categoryTitle: String): CustomCollections
        suspend fun getAllBrands(): Response<Brands>
        suspend fun getAllProductsInSpecificCollectionByIDAndTitle(
            idCollectionDetails: String,
            categoryTitleComingFromHome: String
        ): Response<Products>

        suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
            idCollectionDetails: String,
            categoryTitleFromFilter: String
        ): Response<Products>

        suspend fun getSubCategories(): Products

        suspend fun signIn(email: String):Customers
        suspend fun registerCustomer(customer: CustomerParent):CustomerParent

        suspend fun getAllOrdersForSpecificCustomerById(id: String): Orders
        suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): SubCategories

        suspend fun getCustomerDetails(email: String):Customers
        suspend fun updateCustomerDetails(customer: CustomerParent): CustomerParent
        suspend fun getAllAddresses(): Response<CustomerAddresses>

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

        suspend fun getAllAddresesForSpecificCustomer(id: String):Addresses
        suspend fun postOrders( order: OrderPojo): Response<OrderPojo>

    }
