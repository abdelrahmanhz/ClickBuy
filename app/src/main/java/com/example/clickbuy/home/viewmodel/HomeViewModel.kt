package com.example.clickbuy.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.clickbuy.models.Brands
import com.example.clickbuy.models.CustomCollections
import com.example.clickbuy.models.RepositoryInterface

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
    fun getAllBrands(){
        viewModelScope.launch {
            var brands: Brands? =  null
            val brandResponse = _irepo.getAllBrands()
            if (brandResponse.code() == 200) {
                brands = brandResponse.body()!!
            }

            withContext(Dispatchers.Main) {
                _brand.postValue(brands!!)
                Log.i(
                    TAG,
                    "getAllBrands View Model--------------------->: $brands")
            }
        }
    }
    fun getSalesById() {
        viewModelScope.launch {
            var brands: CustomCollections? =  null
            val brandResponse = _irepo.getSalesId()
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
}