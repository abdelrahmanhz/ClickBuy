package com.example.clickbuy.productdetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.*
import kotlinx.coroutines.*


const val TAG = "ProductDetailsViewModel"

class ProductDetailsViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private var _product = MutableLiveData<Product>()
    private var _isFavAndId = MutableLiveData<Pair<String, Boolean>>()
    var product: LiveData<Product> = _product
    var isFavAndId: LiveData<Pair<String, Boolean>> = _isFavAndId


    private var _isAddedToCart = MutableLiveData<Boolean>()
    var isAddedToCart: LiveData<Boolean> = _isAddedToCart

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


    fun addItemsInBag(product: Product) {
        viewModelScope.launch {
            val response = repo.addItemsInBag(product)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Log.i(TAG, "addItemsInBag response.code()---------> " + response.code())
                    Log.i(TAG, "addItemsInBag response.body()---------> " + response.body())
                    _isAddedToCart.postValue(true)
                }
            } else {
                Log.i(TAG, "addItemsInBag response.body()---------> " + response.body())
                Log.i(TAG, "addItemsInBag response.code()---------> " + response.code())
                _isAddedToCart.postValue(false)
            }

        }
    }

    fun addFavourite(favorite: FavouriteParent){
        viewModelScope.launch {
            val response = repo.addFavourite(favorite)
            withContext(Dispatchers.Main) {
                Log.i(TAG, response.body().toString())
                _isFavAndId.postValue(Pair(response.body()?.draft_order?.id.toString(), response.isSuccessful))
            }
        }
    }

    fun deleteFavourite(favId: String) {
        viewModelScope.launch {
            repo.deleteFavourite(favId)
        }
    }

    fun isFavourite(productId: String){
        viewModelScope.launch {
            val response = repo.getFavourites()
            var isFavAndId = Pair("", false)
            withContext(Dispatchers.Main) {
                Log.i(TAG, response.body()?.toString()!!)
                if (!response.body()?.draft_orders.isNullOrEmpty()) {
                    response.body()?.draft_orders?.forEach {
                        if (it.line_items[0].product_id == productId.toLong())
                            isFavAndId = isFavAndId.copy(it.id.toString(), true)
                    }
                } else isFavAndId = isFavAndId.copy("", false)
                _isFavAndId.postValue(isFavAndId)
            }
        }
    }
}