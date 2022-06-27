package com.example.clickbuy.category.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CategoryViewModel(irepo: RepositoryInterface) : ViewModel() {
    private val _irepo: RepositoryInterface = irepo

    private var _category = MutableLiveData<HashSet<SubCategory>>()
    var category: LiveData<HashSet<SubCategory>> = _category

    private var _subCategory = MutableLiveData<Products>()
    var subCategory: LiveData<Products> = _subCategory


    fun getAllCategoryProducts(idCollectionDetails: String) {
        viewModelScope.launch {
            var categories: HashSet<SubCategory>? = null
            val brandResponse = _irepo.getAllSubCategoriesForSpecificCategory(idCollectionDetails)

            if (brandResponse.code() == 200) {
                categories = brandResponse.body()?.products
                withContext(Dispatchers.Main) {
                    _category.postValue(categories!!)
                }
            }
        }
    }

    fun getAllProducts(collectionId: String, vendor: String, productType: String) {
        viewModelScope.launch {
            val brandResponse =
                _irepo.getAllProducts(collectionId, vendor, productType)
            withContext(Dispatchers.Main) {
                if (brandResponse.code() == 200) {
                    _subCategory.postValue(brandResponse.body())
                    Log.i("TAG", "getAllProducts: body-------------" + brandResponse.body())
                    Log.i("TAG", "getAllProducts: size-------------" + brandResponse.body()?.products?.size)
                    Log.i("TAG", "getAllProducts: products---------" + brandResponse.body()?.products)
                } else {
                    Log.i("TAG", "getAllProducts: null---------" + brandResponse.body())
                    _subCategory.postValue(brandResponse.body())
                }
            }
        }
    }
}