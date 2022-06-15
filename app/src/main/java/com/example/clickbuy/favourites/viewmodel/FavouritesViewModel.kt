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

    private var _favourites = MutableLiveData<Favourites>()
    var favourites: LiveData<Favourites> = _favourites

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

    fun deleteFavourite(favId: String){
        viewModelScope.launch {
            repo.deleteFavourite(favId)
        }
    }
}