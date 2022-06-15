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

class HomeViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo

    private var _brand = MutableLiveData<Brands>()
    var brand: LiveData<Brands> = _brand

    private var _saleId = MutableLiveData<Products>()
    var saleId: LiveData<Products> = _saleId
    private var _order = MutableLiveData<Orders>()
    var order: LiveData<Orders> = _order

    private var _coupons = MutableLiveData<List<DiscountCode>>()
    var coupons: LiveData<List<DiscountCode>> = _coupons


    fun getAllBrands() {
        viewModelScope.launch {
            var brands: Brands? = null
            val brandResponse = _iRepo.getAllBrands()
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
            val response = _iRepo.getAvailableCoupons()
            withContext(Dispatchers.Main) {
                Log.i(TAG, "getAvailableCoupons: ${response.code()}")
                Log.i(TAG, "getAvailableCoupons: ${response.body()}")
                if (response.code() == 200 && !response.body()?.discount_codes.isNullOrEmpty())
                    _coupons.postValue(response.body()?.discount_codes)

            }
        }
    }



    fun getAllSalesById() {
        // getSalesId()
        viewModelScope.launch {
            var brands: Products? = null
            val brandResponse = _iRepo.getAllProductsInCollectionByID("273053778059")
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