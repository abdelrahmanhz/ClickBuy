package com.example.clickbuy.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo

    private var _brand = MutableLiveData<Brands>()
    var brand: LiveData<Brands> = _brand

    private var _saleId = MutableLiveData<Products>()
    var saleId: LiveData<Products> = _saleId

    private var _coupons = MutableLiveData<List<DiscountCode>>()
    var coupons: LiveData<List<DiscountCode>> = _coupons

    private var _priceRules = MutableLiveData<List<PriceRule>>()
    var priceRules: LiveData<List<PriceRule>> = _priceRules

    fun getAllBrands() {
        viewModelScope.launch {
            var brands: Brands? = null
            val brandResponse = _iRepo.getAllBrands()
            if (brandResponse.code() == 200) {
                brands = brandResponse.body()!!
            }

            withContext(Dispatchers.Main) {
                _brand.postValue(brands!!)
            }
        }
    }

    fun getAllSalesById() {
        viewModelScope.launch {
            var brands: Products? = null
            val brandResponse = _iRepo.getAllProducts("273053778059", "", "")
            if (brandResponse.code() == 200) {
                brands = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _saleId.postValue(brands!!)
                brands.products
            }
        }
    }

    fun getAvailableCoupons() {
        viewModelScope.launch {
            val response = _iRepo.getAvailableCoupons()
            withContext(Dispatchers.Main) {
                if (response.code() == 200 && !response.body()?.discount_codes.isNullOrEmpty())
                    _coupons.postValue(response.body()?.discount_codes)

            }
        }
    }

    fun getAllPriceRules() {
        viewModelScope.launch {
            val response = _iRepo.getAllPriceRules()
            withContext(Dispatchers.Main) {
                if (response.code() == 200 && !response.body()?.price_rules.isNullOrEmpty())
                    _priceRules.postValue(response.body()?.price_rules)
            }
        }
    }
}