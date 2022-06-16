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

    private var lineItems: MutableList<BagItem> = mutableListOf()
    private var noteAttributes: MutableList<NoteAttribute> = mutableListOf()


    private var sharedPrefs: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RetrofitClient, context: Context
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
        ConstantsValue.draftOrderID = sharedPrefs?.getString("CART_ID", "")!!

        Log.i(TAG, "setupConstantsValue:isLogged------------> ${ConstantsValue.isLogged}")
        Log.i(TAG, "setupConstantsValue:userID--------------> ${ConstantsValue.userID}")
        Log.i(TAG, "setupConstantsValue:email---------------> ${ConstantsValue.email}")
        Log.i(TAG, "setupConstantsValue:draftOrderID--------> ${ConstantsValue.draftOrderID}")
        if (ConstantsValue.isLogged && !ConstantsValue.draftOrderID.isNullOrEmpty()) {
            Log.i(TAG, "setupConstantsValue: in if to getALLITEMS From splash")
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
        editor?.apply()

        Log.i(TAG, "deleteSavedSettings:isLogged------------> ${ConstantsValue.isLogged}")
        Log.i(TAG, "deleteSavedSettings:userID--------------> ${ConstantsValue.userID}")
        Log.i(TAG, "deleteSavedSettings:email---------------> ${ConstantsValue.email}")
        Log.i(TAG, "deleteSavedSettings:draftOrderID--------> ${ConstantsValue.draftOrderID}")

        val x = sharedPrefs?.getBoolean("IS_LOGGING", false)!!
        val y = sharedPrefs?.getString("USER_ID", "")!!
        val z = sharedPrefs?.getString("USER_EMAIL", "")!!
        val t = sharedPrefs?.getString("CART_ID", "")!!

        Log.i(TAG, "deleteSavedSettings: x---------------> $x")
        Log.i(TAG, "deleteSavedSettings: y---------------> $y")
        Log.i(TAG, "deleteSavedSettings: z---------------> $z")
        Log.i(TAG, "deleteSavedSettings: t---------------> $t")
        lineItems = mutableListOf()
        noteAttributes = mutableListOf()

        Log.i(TAG, "deleteSavedSettings: lineItems.size()-------------->  " + lineItems.size)
        Log.i(TAG, "deleteSavedSettings: noteAttributes.size()--------->  " + noteAttributes.size)
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
        var responseMessage = ""
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


    // local (room)
    override suspend fun addFavorite(favorite: Favorite) {
        //localSource.insertFavorite(favorite)
    }

    override suspend fun getFavorites(): List<Favorite> {
        //return localSource.getFavorites()
        return emptyList()
    }

    override suspend fun deleteFavorite(productId: Long) {
        //localSource.deleteFavorite(productId)
    }

    override suspend fun isFavorite(productId: Long): Boolean {
        //return localSource.isFavorite(productId)
        return false
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

    override suspend fun getAllAddresses(): Response<Addresses>{
        val response = remoteSource.getAllAddresses()
        Log.i(TAG, "getAllAddresses: " + response.code())
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


    override suspend fun getAllItemsInBag(): Response<ShoppingBag> {
        val response = remoteSource.getAllItemsInBag()

        Log.i(TAG, "getAllItemInBag before add: lineItems-----------> " + lineItems.size)
        Log.i(TAG, "getAllItemInBag before add: noteAttributes------> " + noteAttributes.size)

        if (!response.body()?.draft_order?.line_items.isNullOrEmpty()) {
            lineItems = response.body()?.draft_order?.line_items?.toMutableList()!!
            noteAttributes = response.body()?.draft_order?.note_attributes?.toMutableList()!!
        }


        Log.i(TAG, "getAllItemInBag after add: lineItems-----------> " + lineItems.size)
        Log.i(TAG, "getAllItemInBag after add: noteAttributes------> " + noteAttributes.size)

        Log.i(TAG, "getAllItemInBag: $response")
        return response
    }

    override suspend fun updateItemsInBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {
        Log.i(TAG, "updateItemsInBag: draftOrderID--------> " + ConstantsValue.draftOrderID)
        val response = remoteSource.updateItemsInBag(shoppingBag)
        Log.i(TAG, "updateItemsInBag: $response")
        if (!response.body()?.draft_order?.note_attributes.isNullOrEmpty()) {
            Log.i(TAG, "updateItemsInBag after update: noteAttributes----> " + response.body()?.draft_order?.line_items?.size)
            Log.i(TAG, "updateItemsInBag after update: lineItems---------> " + response.body()?.draft_order?.note_attributes?.size)
            lineItems = response.body()?.draft_order?.line_items!!.toMutableList()
            noteAttributes = response.body()?.draft_order?.note_attributes!!.toMutableList()
            Log.i(TAG, "updateItemsInBag after update: noteAttributes----> " + noteAttributes.size)
            Log.i(TAG, "updateItemsInBag after update: lineItems---------> " + lineItems.size)
        }else{
            lineItems = mutableListOf()
            noteAttributes =  mutableListOf()
            Log.i(TAG, "updateItemsInBag clear: noteAttributes----> " + noteAttributes.size)
            Log.i(TAG, "updateItemsInBag clear: lineItems---------> " + lineItems.size)
        }
        return response
    }

    override suspend fun addItemsInBag(product: Product): Response<ShoppingBag> {
        Log.i(TAG, "addItemsInBag: draftOrderID--------> " + ConstantsValue.draftOrderID)

        Log.i(TAG, "addItemsInBag before add: lineItems-----------> " + lineItems.size)
        Log.i(TAG, "addItemsInBag before add: noteAttributes------> " + noteAttributes.size)


        if (ConstantsValue.draftOrderID.trim().isNullOrEmpty()) {

            Log.i(TAG, "addItemsInBag: create")
            lineItems.add(BagItem(quantity = 1, variant_id = product.variants?.get(0)!!.id))
            noteAttributes.add(
                NoteAttribute(
                    name = product.variants[0].id.toString(),
                    value = product.images?.get(0)!!.src
                )
            )
            val shoppingBag = ShoppingBag(
                DraftOrder(
                    email = ConstantsValue.email,
                    line_items = lineItems,
                    note_attributes = noteAttributes,
                )
            )

            Log.i(TAG, "addItemsInBag: shoppingBag----------> $shoppingBag")

            val response = createBag(shoppingBag)
            Log.i(TAG, "addItemsInBag: $response")
            return response
        } else {
            Log.i(TAG, "addItemsInBag: exist")
            var isExist = false

            if (lineItems.size == 1 && noteAttributes.size == 0) {
                lineItems.removeAt(0)
                Log.i(TAG, "addItemsInBag: in if")
            } else {
                for (i in lineItems) {
                    if (i.variant_id == product.variants?.get(0)!!.id) {
                        i.quantity++
                        isExist = true
                        break
                    }
                }
            }

            if (!isExist) {
                lineItems.add(BagItem(quantity = 1, variant_id = product.variants?.get(0)!!.id))
                noteAttributes.add(
                    NoteAttribute(
                        name = product.variants[0].id.toString(),
                        value = product.images?.get(0)!!.src
                    )
                )
            }

            Log.i(TAG, "addItemsInBag after add: lineItems-----------> " + lineItems.size)
            Log.i(TAG, "addItemsInBag after add: noteAttributes------> " + noteAttributes.size)

            val shoppingBag = ShoppingBag(
                DraftOrder(
                    ConstantsValue.email,
                    ConstantsValue.draftOrderID.toLong(),
                    lineItems,
                    noteAttributes
                )
            )

            Log.i(TAG, "addItemsInBag: shoppingBag----------> $shoppingBag")

            val response = updateItemsInBag(shoppingBag)
            Log.i(TAG, "addItemsInBag: $response")
            return response
        }
    }

    override suspend fun createBag(shoppingBag: ShoppingBag): Response<ShoppingBag> {
        val response = remoteSource.createBag(shoppingBag)
        Log.i(TAG, "createBag: -----------------> ${response.body()}")
        if (response.isSuccessful) {
            Log.i(TAG, "createBag: isSuccessful ---- ID -----> " + response.body()?.draft_order?.id)

            val responseUpdate = remoteSource.updateCustomerDetails(
                CustomerParent(
                    Customer(
                        id = ConstantsValue.userID.toLong(),
                        email = ConstantsValue.email,
                        note = response.body()?.draft_order?.id
                    )
                )
            )

            Log.i(TAG, "createBag: responseUpdate--------> $responseUpdate")
            editor?.putString("CART_ID", responseUpdate.body()?.customer?.note?.toString())
            editor?.apply()
            ConstantsValue.draftOrderID = responseUpdate.body()?.customer?.note.toString()
        }
        return response
    }
}



