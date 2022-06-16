package com.example.clickbuy.models


import retrofit2.Response


interface RepositoryInterface {

    //Network
    suspend fun getAllBrands(): Response<Brands>
    suspend fun getAllProducts(
        collectionId: String,
        vendor: String,
        productType: String
    ): Response<Products>

    suspend fun getSubCategories(): Response<Products>
    suspend fun getProductById(productId: String): Response<ProductParent>
    //suspend fun getAllBrandsDetais(id : String): Response<Products>
    //suspend fun getSalesId(): Response<CustomCollections>

    //suspend fun getAllBrandsDetais(id: String): Response<Products>


    //suspend fun getAllBrandsDetais(id : String): Response<Products>
    //suspend fun getSalesId(): Response<CustomCollections>

    //suspend fun getAllBrandsDetais(id: String): Response<Products>
    suspend fun getAllProductsInCollectionByID(id: String): Response<Products>
    suspend fun getCategoryIdByTitle(categoryTitle: String): Response<CustomCollections>
    suspend fun getAllProductsInSpecificCollectionByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleComingFromHome: String
    ): Response<Products>

    suspend fun getAllOrdersForSpecificCustomerById(id: String): Response<Orders>

    suspend fun signIn(email: String, password: String): String
    suspend fun registerCustomer(customer: CustomerParent): Response<CustomerParent>
    suspend fun getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(
        idCollectionDetails: String,
        categoryTitleFromFilter: String
    ): Response<Products>

    suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): Response<SubCategories>
    suspend fun getCustomerDetails(email: String): Response<Customers>
    suspend fun getAllAddresses(): Response<Addresses>
    suspend fun getCurrencies(): Response<Currencies>
    suspend fun getQualifiedValueCurrency(to: String): Response<CurrencyConverter>
    suspend fun getAvailableCoupons(): Response<Coupons>
    suspend fun validateCoupons(code: String): Response<Coupon>

    suspend fun getAllItemsInBag(): Response<ShoppingBag>
    suspend fun updateItemsInBag(shoppingBag: ShoppingBag): Response<ShoppingBag>

    suspend fun addItemsInBag(product: Product): Response<ShoppingBag>
    suspend fun createBag(shoppingBag: ShoppingBag): Response<ShoppingBag>

    suspend fun setupConstantsValue()
    suspend fun deleteSavedSettings()

    suspend fun getAllAddresesForSpecificCustomer(id: String): Response<Addresses>
    suspend fun postOrders(order: OrderPojo): Response<OrderPojo>

    // Favourites
    suspend fun getFavourites(): Response<Favourites>
    suspend fun addFavourite(favorite: FavouriteParent): Response<FavouriteParent>
    suspend fun deleteFavourite(favId: String)

}