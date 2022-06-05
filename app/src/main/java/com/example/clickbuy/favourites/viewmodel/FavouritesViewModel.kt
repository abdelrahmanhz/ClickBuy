package com.example.clickbuy.favourites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.Favorite
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.launch

const val TAG = "FavouritesViewModel"
class FavouritesViewModel(private val repo: RepositoryInterface): ViewModel() {

    lateinit var favourites : LiveData<List<Favorite>>

    fun getFavourites(){
        favourites = repo.getFavorites()
    }

    fun addFavourite(favorite: Favorite){
        viewModelScope.launch {
            repo.addFavorite(favorite)
        }
    }

    fun deleteFavourite(productId: Long){
        repo.deleteFavorite(productId)
    }
}