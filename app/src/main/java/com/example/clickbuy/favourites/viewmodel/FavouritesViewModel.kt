package com.example.clickbuy.favourites.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.Favorite
import com.example.clickbuy.models.Product
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "FavouritesViewModel"
class FavouritesViewModel(private val repo: RepositoryInterface): ViewModel() {

    private var _favourites = MutableLiveData<List<Favorite>>()
    var favourites: LiveData<List<Favorite>> = _favourites

    fun getFavourites(){
        viewModelScope.launch {
            val response = repo.getFavorites()
            withContext(Dispatchers.Main) {
                Log.i(com.example.clickbuy.productdetails.viewmodel.TAG, response.toString())
                _favourites.postValue(response)
            }
        }
        viewModelScope.launch {
             repo.getFavorites()
        }
    }

    fun addFavourite(favorite: Favorite){
        viewModelScope.launch {
            repo.addFavorite(favorite)
        }
    }

    fun deleteFavourite(productId: Long){
        viewModelScope.launch {
            repo.deleteFavorite(productId)
        }
    }
}