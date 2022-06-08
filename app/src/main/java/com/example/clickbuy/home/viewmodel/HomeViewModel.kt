package com.example.clickbuy.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "HomeViewModel"

class HomeViewModel(irepo: RepositoryInterface) : ViewModel() {
    private val _irepo: RepositoryInterface = irepo

    private var _brand = MutableLiveData<Brands>()
    var brand: LiveData<Brands> = _brand
    private var _sale = MutableLiveData<CustomCollections>()
    var sale: LiveData<CustomCollections> = _sale

    private var _saleId = MutableLiveData<Products>()
    var saleId: LiveData<Products> = _saleId

    private var _order = MutableLiveData<Orders>()
    var order: LiveData<Orders> = _order
//    fun getAllOrdersById(id : String){
//        viewModelScope.launch {
//            var orders: Orders? = null
//            val brandResponse = _irepo.getAllOrdersById(id)
//            if (brandResponse.code() == 200) {
//                orders = brandResponse.body()!!
//            }
//            withContext(Dispatchers.Main) {
//                _order.postValue(orders!!)
//                Log.i(TAG, "getAllOrders View Model  all orderssss--------------------->: $orders"
//                )
//            }
//        }
//    }

    fun getAllBrands() {
        viewModelScope.launch {
            var brands: Brands? = null
            val brandResponse = _irepo.getAllBrands()
            if (brandResponse.code() == 200) {
                brands = brandResponse.body()!!
            }

            withContext(Dispatchers.Main) {
                _brand.postValue(brands!!)
                Log.i(
                    TAG,
                    "getAllBrands View Model--------------------->: $brands"
                )
            }
        }
    }

    fun getSalesId() {
        viewModelScope.launch {
            var brands: CustomCollections? = null
            val brandResponse = _irepo.getCategoryIdByTitle("SALE")
            if (brandResponse.code() == 200) {
                brands = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _sale.postValue(brands!!)
                Log.i(TAG, "getSalesById View Model--------------------->: $brands")
                brands.custom_collections[0].id

            }
        }
    }
    fun getAllSalesById() {
       // getSalesId()
        viewModelScope.launch {
            var brands: Products? = null
            val brandResponse = _irepo.getAllProductsInCollectionByID("273053778059")
            if (brandResponse.code() == 200) {
                brands = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _saleId.postValue(brands!!)
                Log.i(TAG, "getAllSalesById View Model--------------------->: $brands")
                brands.products
            }
        }
    }
}