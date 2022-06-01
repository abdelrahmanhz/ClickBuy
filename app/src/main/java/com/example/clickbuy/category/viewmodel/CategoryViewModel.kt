package com.example.clickbuy.category.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    var brand: LiveData<Products> = _category

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
        else {
            getAllCategoryProductsBrandFromHome( idCollectionDetails , categoryTitleComingFromHome)
        }
    }

}