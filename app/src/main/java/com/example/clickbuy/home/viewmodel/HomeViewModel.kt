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

    private var _coupons = MutableLiveData<List<DiscountCode>>()
    var coupons: LiveData<List<DiscountCode>> = _coupons

    private var _coupon = MutableLiveData<Coupon>()
    var coupon: LiveData<Coupon> = _coupon

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

    fun getAvailableCoupons() {
        viewModelScope.launch {
            val response = _irepo.getAvailableCoupons()
            withContext(Dispatchers.Main) {
                Log.i(TAG, "getAvailableCoupons: ${response.code()}")
                Log.i(TAG, "getAvailableCoupons: ${response.body()}")
                if (response.code() == 200 && !response.body()?.discount_codes.isNullOrEmpty())
                    _coupons.postValue(response.body()?.discount_codes)

            }
        }
    }

    fun validateCoupons(code: String) {
        viewModelScope.launch {

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