package com.example.clickbuy.productdetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.Product
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "ProductDetailsViewModel"
class ProductDetailsViewModel(repo: RepositoryInterface): ViewModel() {

    private val repo: RepositoryInterface = repo

    private var _product = MutableLiveData<Product>()
    var product : LiveData<Product> = _product

    fun getProductById(productId: String){
        viewModelScope.launch {
            val response = repo.getProductById(productId)

            if (response.isSuccessful){
                withContext(Dispatchers.Main) {
                    Log.i(TAG, response.body().toString())
                    _product.postValue(response.body()?.product)
                }
            }
            else
                Log.i(TAG, "getProductById response failed!")
        }
    }
}