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

    fun addFavourite(favorite: Favorite){
        viewModelScope.launch {
            repo.addFavorite(favorite)
        }
    }

    fun deleteFavourite(productId: Long){
        repo.deleteFavorite(productId)
    }

    fun isFavourite(productId: Long): Boolean{
        var successReturn = false
        CoroutineScope(coroutineContext).async(Dispatchers.IO) {
            val success = async {repo.isFavorite(productId)}
            successReturn = success.await()
        }
        return successReturn
    }
}