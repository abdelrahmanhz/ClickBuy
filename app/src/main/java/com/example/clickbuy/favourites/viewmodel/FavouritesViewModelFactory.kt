package com.example.clickbuy.favourites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface

class FavouritesViewModelFactory(private val repo: RepositoryInterface): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)){
            FavouritesViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }
}