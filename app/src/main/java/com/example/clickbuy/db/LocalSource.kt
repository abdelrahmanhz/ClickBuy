package com.example.clickbuy.db

import androidx.lifecycle.LiveData
import com.example.clickbuy.models.Favorite

interface LocalSource {
    suspend fun insertFavorite(favorite: Favorite)
    fun getFavorites(): LiveData<List<Favorite>>
    suspend fun deleteFavorite(favorite: Favorite)
}