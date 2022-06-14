package com.example.clickbuy.productdetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.*
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

    fun addFavourite(favorite: DraftOrderParent){
        viewModelScope.launch {
            val response = repo.addFavourite(favorite)
            withContext(Dispatchers.Main) {
                Log.i(TAG, response.body().toString())
                _isFav.postValue(response.isSuccessful)
            }
        }
    }

    fun deleteFavourite(productId: Long) {
        viewModelScope.launch {
            //repo.deleteFavorite(productId)
        }
    }

    fun isFavourite(productId: String) {
        viewModelScope.launch {
            val response = repo.getFavourites()
            var isFavourite = false
            withContext(Dispatchers.Main) {
                Log.i(TAG, response.body()?.toString()!!)
                if (!response.body()?.draft_orders.isNullOrEmpty()) {
                    response.body()?.draft_orders?.forEach {
                        if (it.line_items[0].product_id == productId.toLong())
                            isFavourite = true
                    }
                } else isFavourite = false
                _isFav.postValue(isFavourite)
            }
        }
    }
}