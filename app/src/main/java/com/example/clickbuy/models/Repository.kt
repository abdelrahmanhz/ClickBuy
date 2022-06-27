package com.example.clickbuy.models

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.example.clickbuy.network.RemoteSource
import com.example.clickbuy.util.ConstantsValue
import retrofit2.Response

private const val TAG = "Repository"

class Repository private constructor(
    var remoteSource: RemoteSource,
    var context: Context
) : RepositoryInterface {

    private var lineItems: MutableList<BagItem> = mutableListOf()
    private var noteAttributes: MutableList<NoteAttribute> = mutableListOf()


    private var sharedPrefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RemoteSource, context: Context
        ): Repository {
            if (instance == null) {
                instance = Repository(remoteSource, context)
            }
            return instance!!
        }
    }

    init {
        this.sharedPrefs = context.getSharedPreferences("DeviceToken", MODE_PRIVATE)
        this.editor = sharedPrefs!!.edit()
    }

    override suspend fun setupConstantsValue() {

        ConstantsValue.isLogged = sharedPrefs?.getBoolean("IS_LOGGING", false)!!
        ConstantsValue.userID = sharedPrefs?.getString("USER_ID", "")!!
        ConstantsValue.email = sharedPrefs?.getString("USER_EMAIL", "")!!
        ConstantsValue.draftOrderID = sharedPrefs?.getString("CART_ID", "empty")!!
        ConstantsValue.to = sharedPrefs?.getString("CURRENCY", "EGP")!!


        Log.i(TAG, "setupConstantsValue: to-------------------> " + ConstantsValue.to)

        if (ConstantsValue.isLogged && ConstantsValue.draftOrderID != "null") {
            getAllItemsInBag()
        }
    }

    override suspend fun deleteSavedSettings() {
        ConstantsValue.isLogged = false
        ConstantsValue.userID = ""
        ConstantsValue.email = ""
        ConstantsValue.draftOrderID = ""
        editor?.remove("IS_LOGGING")
        editor?.remove("USER_ID")
        editor?.remove("USER_EMAIL")
        editor?.remove("CART_ID")
        editor?.remove("CURRENCY")
        editor?.apply()

        lineItems = mutableListOf()
        noteAttributes = mutableListOf()
    }

    override suspend fun getAllBrands(): Response<Brands> {
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

    /* override suspend fun getAllProductsInCollectionByID(id: String): Response<Products> {
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
     }*/


    override suspend fun getAllOrdersForSpecificCustomerById(id: String): Response<Orders> {
        return remoteSource.getAllOrdersForSpecificCustomerById(id)

    }

    override suspend fun signIn(email: String, password: String): String {
        val responseMessage: String
        val response = remoteSource.signIn(email)
        if (response.code() == 200) {
            if (response.body()?.customers.isNullOrEmpty()) {
                responseMessage = "No such user"
            } else {
                // shared pref
                if (response.body()?.customers!![0].tags == password) {
                    responseMessage = "Logged in successfully"
                    editor?.putBoolean("IS_LOGGING", true)
                    editor?.putString("USER_ID", response.body()!!.customers[0].id!!.toString())
                    editor?.putString("USER_EMAIL", response.body()!!.customers[0].email)
                    editor?.putString("CART_ID", response.body()!!.customers[0].note.toString())
                    editor?.apply()
                    setupConstantsValue()
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


    override suspend fun getProductById(productId: String): Response<ProductParent> {
        val response = remoteSource.getProductByID(productId)
        return response
    }


    override suspend fun getAllSubCategoriesForSpecificCategory(idCollectionDetails: String): Response<SubCategories> {
        val response = remoteSource.getAllSubCategoriesForSpecificCategory(idCollectionDetails)
        return response
    }

    override suspend fun getCustomerDetails(email: String): Response<Customers> {
        val response = remoteSource.getCustomerDetails(email)
        return response
    }

    override suspend fun updateCustomerDetailsTest(customer: CustomersTest): Response<CustomersTest> {
        val response = remoteSource.updateCustomerDetailsTest(customer)
        return response
    }

    override suspend fun getAllAddresses(): Response<CustomerAddresses> {
        val response = remoteSource.getAllAddresses()
        return response
    }

    override suspend fun addAddress(address: CustomerAddressUpdate): Response<CustomerAddressResponse> {
        Log.i("AddressViewModel", "start addAddress: repository")
        val response = remoteSource.addAddress(address)
        Log.i("AddressViewModel", "get addAddress: repository")
        return response
    }

    override suspend fun getAddressFromApi(placeName: String): Response<AddressResponseAPI> {
        val response = remoteSource.getAddressFromApi(placeName)
        return response
    }

    override suspend fun getCurrencies(): Response<Currencies> {
        val response = remoteSource.getCurrencies()
        return response
    }

    override suspend fun getQualifiedValueCurrency(
        to: String
    ): Response<CurrencyConverter> {
        editor?.putString("CURRENCY", ConstantsValue.to)
        editor?.apply()
        val response = remoteSource.getQualifiedValueCurrency(to)
        return response
    }

    override suspend fun getAvailableCoupons(): Response<Coupons> {
        val response = remoteSource.getAvailableCoupons()
        return response
    }

    override suspend fun validateCoupons(code: String): Response<Coupon> {
        val response = remoteSource.validateCoupons(code)
        return response
    }

    override suspend fun getAllItemsInBag(): Response<ShoppingBag> {
        val response = remoteSource.getAllItemsInBag()
        if (!response.body()?.draft_order?.line_items.isNullOrEmpty()) {
            lineItems = response.body()?.draft_order?.line_items?.toMutableList()!!
            noteAttributes = response.body()?.draft_order?.note_attributes?.toMutableList()!!
        }
        return response
    }

    override suspend fun updateItemsInBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {
        val response = remoteSource.updateItemsInBag(shoppingBag)
        if (!response.body()?.draft_order?.note_attributes.isNullOrEmpty()) {
            lineItems = response.body()?.draft_order?.line_items!!.toMutableList()
            noteAttributes = response.body()?.draft_order?.note_attributes!!.toMutableList()
        } else {
            lineItems = mutableListOf()
            noteAttributes = mutableListOf()
        }
        return response
    }

    override suspend fun addItemsInBag(
        product: Product,
        variantPosition: Int
    ): Response<ShoppingBag> {
        Log.i(TAG, "addItemsInBag: draftOrderID--------> " + ConstantsValue.draftOrderID)

        Log.i(TAG, "addItemsInBag before add: lineItems-----------> " + lineItems.size)
        Log.i(TAG, "addItemsInBag before add: noteAttributes------> " + noteAttributes.size)


        if (ConstantsValue.draftOrderID.trim() == "null" ||
            ConstantsValue.draftOrderID.trim().isEmpty()
        ) {

            Log.i(TAG, "addItemsInBag: create")
            lineItems.add(BagItem(quantity = 1, variant_id = product.variants?.get(0)!!.id))
            noteAttributes.add(
                NoteAttribute(
                    name = product.variants[variantPosition].id.toString(),
                    value = product.images?.get(variantPosition)!!.src
                )
            )
            val shoppingBag = ShoppingBag(
                DraftOrder(
                    email = ConstantsValue.email,
                    line_items = lineItems,
                    note_attributes = noteAttributes,
                )
            )


            val response = createBag(shoppingBag)
            return response
        } else {
            var isExist = false

            if (lineItems.size == 1 && noteAttributes.size == 0) {
                lineItems.removeAt(0)
            } else {
                for (i in lineItems) {
                    if (i.variant_id == product.variants?.get(variantPosition)!!.id) {
                        i.quantity++
                        isExist = true
                        break
                    }
                }
            }

            if (!isExist) {
                lineItems.add(
                    BagItem(
                        quantity = 1,
                        variant_id = product.variants?.get(variantPosition)!!.id
                    )
                )
                noteAttributes.add(
                    NoteAttribute(
                        name = product.variants[variantPosition].id.toString(),
                        value = product.images?.get(variantPosition)!!.src
                    )
                )
            }
            val shoppingBag = ShoppingBag(
                DraftOrder(
                    ConstantsValue.email,
                    ConstantsValue.draftOrderID.toLong(),
                    lineItems,
                    noteAttributes
                )
            )
            val response = updateItemsInBag(shoppingBag)
            return response
        }
    }

    override suspend fun createBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {
        val response = remoteSource.createBag(shoppingBag)
        if (response.isSuccessful) {
            val responseUpdate = remoteSource.updateCustomerDetails(
                CustomerParent(
                    Customer(
                        id = ConstantsValue.userID.toLong(),
                        email = ConstantsValue.email,
                        note = response.body()?.draft_order?.id
                    )
                )
            )
            editor?.putString("CART_ID", responseUpdate.body()?.customer?.note?.toString())
            editor?.apply()
            ConstantsValue.draftOrderID = responseUpdate.body()?.customer?.note.toString()
        }
        return response
    }

    /*  override suspend fun getAllAddresesForSpecificCustomer(id: String): Response<Addresses> {
          val response = remoteSource.getAllAddresesForSpecificCustomer(id)
          return response
      }*/


    override suspend fun postOrders(order: OrderPojo): Response<OrderPojo> {
        return remoteSource.postOrders(order)
    }


    override suspend fun getFavourites(): Response<Favourites> {
        val response = remoteSource.getDraftOrders()
        if (response.code() == 200 && !response.body()?.draft_orders.isNullOrEmpty()) {
            val email = sharedPrefs?.getString("USER_EMAIL", "")
            if (!email.isNullOrEmpty())
                response.body()?.draft_orders =
                    response.body()?.draft_orders?.filter { it.note == "fav" && it.email == email }
        }
        return response
    }

    override suspend fun addFavourite(favorite: FavouriteParent): Response<FavouriteParent> {
        val email = sharedPrefs?.getString("USER_EMAIL", "")
        favorite.draft_order?.email = email
        return remoteSource.addFavourite(favorite)
    }

    override suspend fun deleteFavourite(favId: String) {
        remoteSource.removeFavourite(favId)
    }

}