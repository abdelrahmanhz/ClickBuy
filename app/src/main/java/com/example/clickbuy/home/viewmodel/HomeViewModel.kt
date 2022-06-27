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

   /* private var _priceRules = MutableLiveData<List<PriceRule>>()
    var priceRules: LiveData<List<PriceRule>> = _priceRules*/

    fun getAllBrands() {
        viewModelScope.launch {
            val brandResponse = _iRepo.getAllBrands()
            if (brandResponse.code() == 200) {
                withContext(Dispatchers.Main) {
                    _brand.postValue(brandResponse.body()!!)
                }
            } else {
                _brand.postValue(brandResponse.body()!!)
            }
        }
    }

    fun getAllSalesById() {
        viewModelScope.launch {
            val salesResponse = _iRepo.getAllProducts("274329501835", "", "")
            if (salesResponse.code() == 200) {
                withContext(Dispatchers.Main) {
                    _saleId.postValue( salesResponse.body()!!)
                }
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

}