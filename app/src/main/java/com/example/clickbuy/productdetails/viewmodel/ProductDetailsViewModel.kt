package com.example.clickbuy.productdetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.Favorite
import com.example.clickbuy.models.Product
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext

const val TAG = "ProductDetailsViewModel"

class ProductDetailsViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private var _product = MutableLiveData<Product>()
    private var _isFav = MutableLiveData<Boolean>()
    var product: LiveData<Product> = _product
    var isFav: LiveData<Boolean> = _isFav

    fun getProductById(productId: String) {
        viewModelScope.launch {
            val response = repo.getProductById(productId)

            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Log.i(TAG, response.body().toString())
                    _product.postValue(response.body()?.product)
                }
            } else
                Log.i(TAG, "getProductById response failed!")
        }
    }

    fun addFavourite(favorite: Favorite) {
        viewModelScope.launch {
            repo.addFavorite(favorite)
        }
    }

    fun deleteFavourite(productId: Long) {
        viewModelScope.launch {
            repo.deleteFavorite(productId)
        }

    }

    fun isFavourite(productId: Long) {
        viewModelScope.launch {
            val response = repo.isFavorite(productId)
            withContext(Dispatchers.Main) {
                Log.i(TAG, response.toString())
                _isFav.postValue(response)
            }
        }
    }
}