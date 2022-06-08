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

private const val TAG = "CategoryViewModel"

class CategoryViewModel(irepo: RepositoryInterface) : ViewModel() {
    private val _irepo: RepositoryInterface = irepo

    private var _category = MutableLiveData<HashSet<SubCategory>>()
    var category: LiveData<HashSet<SubCategory>> = _category
    private var _brand = MutableLiveData<Brands>()
    var brand: LiveData<Brands> = _brand

    private var _subCategory = MutableLiveData<Products>()
    var subCategory: LiveData<Products> = _subCategory
    fun getAllBrandsInFilter() {
        viewModelScope.launch {
            var brands: Brands? = null
            val brandResponse = _irepo.getAllBrands()
            if (brandResponse.code() == 200) {
                brands = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _brand.postValue(brands!!)
                Log.i(TAG, "getAllBrands View Model filter--------------------->: $brands")
            }
        }
    }

    fun getAllCategoryProducts(idCollectionDetails: String) {
        viewModelScope.launch {
            var categories: HashSet<SubCategory>? = null
            val brandResponse = _irepo.getAllSubCategoriesForSpecificCategory(idCollectionDetails)

            if (brandResponse.code() == 200) {
                categories = brandResponse.body()?.products
                withContext(Dispatchers.Main) {
                    _category.postValue(categories!!)
                    Log.i(
                        TAG,
                        "getAllCategory View Model--------------------->: ${categories.size}"
                    )
                }
            }
        }
    }

    fun getAllProducts(
        collectionId: String,
        vendor: String,
        productType: String
    ) {
        viewModelScope.launch {
            var categories: Products? = null
            val brandResponse =
                _irepo.getAllProducts(collectionId, vendor, productType)
            if (brandResponse.code() == 200) {
                categories = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _subCategory.postValue(categories!!)
                Log.i(
                    TAG,
                    "heya de getAllCategory View Model--------------------->: ${categories.products.size}"
                )
            }
        }
    }
}