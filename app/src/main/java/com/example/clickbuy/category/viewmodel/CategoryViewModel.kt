package com.example.clickbuy.category.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.Brands
import com.example.clickbuy.models.CustomCollections
import com.example.clickbuy.models.Products
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

private const val TAG = "CategoryViewModel"

class CategoryViewModel(irepo: RepositoryInterface) : ViewModel() {
    private val _irepo: RepositoryInterface = irepo

    private var _category = MutableLiveData<Products>()
    var category: LiveData<Products> = _category
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
    fun getAllCategoryProducts( idCollectionDetails : String) {
        viewModelScope.launch {
            var categories: Products? =  null
            val brandResponse = _irepo.getAllProductsInCollectionByID(idCollectionDetails)

            if (brandResponse.code() == 200) {
                categories = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _category.postValue(categories!!)
                Log.i(
                    TAG,
                    "getAllCategory View Model--------------------->: $categories")
            }
        }
    }
    fun getAllCategoryProductsBrandFromHome(  idCollectionDetails : String , categoryTitleComingFromHome : String) {
        viewModelScope.launch {
            var categories: Products? =  null
            val brandResponse = _irepo.getAllProductsInSpecificCollectionByIDAndTitle(idCollectionDetails,categoryTitleComingFromHome)

            if (brandResponse.code() == 200) {
                categories = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _category.postValue(categories!!)
                Log.i(
                    TAG,
                    "getAllCategory View Model--------------------->: $categories")
            }
        }
    }

    fun checkFromHomeOrCategory( idCollectionDetails : String , categoryTitleComingFromHome : String){
        if (categoryTitleComingFromHome.isNullOrEmpty()){
            getAllCategoryProducts(idCollectionDetails)
        }
        else if (categoryTitleComingFromHome.isNullOrEmpty() && idCollectionDetails.isNullOrEmpty()){
            getAllSubCategoriesFromFilter(idCollectionDetails, categoryTitleComingFromHome)
        }
        else {
            getAllCategoryProductsBrandFromHome( idCollectionDetails , categoryTitleComingFromHome)
        }
    }

    fun getAllSubCategoriesFromFilter(idCollectionDetails : String , categoryTitleComingFilter : String) {
            viewModelScope.launch {
                var categories: Products? =  null
                val brandResponse = _irepo.getAllSubCategoriesFilterForSpecificCategoryByIDAndTitle(idCollectionDetails,categoryTitleComingFilter)

                if (brandResponse.code() == 200) {
                    categories = brandResponse.body()!!
                }
                withContext(Dispatchers.Main) {
                    _subCategory.postValue(categories!!)
                    Log.i(
                        TAG,
                        "getAllCategory View Model--------------------->: $categories")
                }
            }
        }
    fun getAllProducts(idCollectionDetails : String , categoryTitleComing : String , subCategory : String){
        viewModelScope.launch {
            var categories: Products? =  null
            val brandResponse = _irepo.getAllProducts(idCollectionDetails , categoryTitleComing , subCategory )
            if (brandResponse.code() == 200) {
                categories = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _subCategory.postValue(categories!!)
                Log.i(
                    TAG,
                    "getAllCategory View Model--------------------->: $categories")
            }
        }
    }
    fun getSubCategories(){
        viewModelScope.launch {
            var categories: Products? =  null
            val brandResponse = _irepo.getSubCategories()
            if (brandResponse.code() == 200) {
                categories = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _subCategory.postValue(categories!!)
                Log.i(TAG, "getAllCategory View Model--------------------->: $categories")
            }
        }
    }

    }

