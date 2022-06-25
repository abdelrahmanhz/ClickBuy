package com.example.clickbuy

import com.example.clickbuy.models.*
import com.example.clickbuy.network.RemoteSource
import org.checkerframework.checker.units.qual.C
import retrofit2.Response



class FakeRepository : RemoteSource {

    override suspend fun getAllProductsInCollectionByID(collectionID: String): Response<Products> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductByID(productId: String): Response<ProductParent> {
        val product = Product(id = 6870134227083, title = "ADIDAS | SUPERSTAR 80S")
        return Response.success(200, ProductParent(product))
    }

    override suspend fun getAllProducts(
        collectionId: String,
        vendor: String,
        productType: String
    ): Response<Products> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategoryIdByTitle(categoryTitle: String): Response<CustomCollections> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllBrands(): Response<Brands> {
        var brandItems: MutableList<Brand> = mutableListOf()
        brandItems.add(Brand(title = "ADIDAS"))
        return Response.success(200, Brands(brandItems))
    }

    override suspend fun getAllProductsInSpecificCollectionByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleComingFromHome: String
    ): Response<Products> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleFromFilter: String
    ): Response<Products> {
        TODO("Not yet implemented")
    }

    override suspend fun getSubCategories(): Response<Products> {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(email: String): Response<Customers> {
        val customers = Customers(listOf(Customer(email = "ahmza@gmail.com", tags = "12345678", id = 5764294901899, note = "873313206411")))
        return Response.success(200, customers)
    }

    override suspend fun registerCustomer(customer: CustomerParent): Response<CustomerParent> {
        return Response.success(200, customer)
    }

    override suspend fun getAllOrdersForSpecificCustomerById(id: String): Response<Orders> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): Response<SubCategories> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerDetails(email: String): Response<Customers> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCustomerDetails(customer: CustomerParent): Response<CustomerParent> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCustomerDetailsTest(customer: CustomersTest): Response<CustomersTest> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAddresses(): Response<CustomerAddresses> {
        TODO("Not yet implemented")
    }

    override suspend fun addAddress(address: CustomerAddressUpdate): Response<CustomerAddressResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAddressFromApi(placeName: String): Response<AddressResponseAPI> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrencies(): Response<Currencies> {
        TODO("Not yet implemented")
    }

    override suspend fun getQualifiedValueCurrency(to: String): Response<CurrencyConverter> {
        TODO("Not yet implemented")
    }

    override suspend fun getAvailableCoupons(): Response<Coupons> {
        TODO("Not yet implemented")
    }

    override suspend fun validateCoupons(code: String): Response<Coupon> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllItemsInBag(): Response<ShoppingBag> {
        TODO("Not yet implemented")
    }

    override suspend fun updateItemsInBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {
        TODO("Not yet implemented")
    }

    override suspend fun createBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {
        TODO("Not yet implemented")
    }

    override suspend fun getDraftOrders(): Response<Favourites> {
        val favourites = Favourites(listOf(Favourite(line_items = listOf(FavouriteLineItem(id = 40335555035275, product_id = 40335555035275)), note = "fav")))
        return Response.success(200, favourites)
    }

    override suspend fun addFavourite(favorite: FavouriteParent): Response<FavouriteParent> {
        return Response.success(200, favorite)
    }

    override suspend fun removeFavourite(id: String): Response<Any> {
        return Response.success(200)
    }

    override suspend fun getAllAddresesForSpecificCustomer(id: String): Response<Addresses> {
        TODO("Not yet implemented")
    }

    override suspend fun postOrders(order: OrderPojo): Response<OrderPojo> {
        TODO("Not yet implemented")
    }
}