package com.example.clickbuy.favourites.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "FavouritesViewModel"
class FavouritesViewModel(private val repo: RepositoryInterface): ViewModel() {

    private var _favourites = MutableLiveData<DraftOrders>()
    var favourites: LiveData<DraftOrders> = _favourites
    private var _isAdded = MutableLiveData<Boolean>()
    var isAdded: LiveData<Boolean> = _isAdded

    fun getFavourites(){
        viewModelScope.launch {

            val response = repo.getFavourites()
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Log.i(
                        TAG,
                        response.body()?.toString()!!
                    )
                    _favourites.postValue(response.body())
                }
            }
        }
    }

    fun addFavourite(favorite: DraftOrderParent){
//        viewModelScope.launch {
//            val response = repo.addFavourite(favorite)
//                withContext(Dispatchers.Main) {
//                    Log.i(TAG, response.body()?.toString()!!)
//                    _isAdded.postValue(response.code() == 200)
//            }
//        }
    }

    fun deleteFavourite(productId: Long){
        viewModelScope.launch {
            //repo.deleteFavorite(productId)
        }
    }
}