package com.example.clickbuy.db

import androidx.lifecycle.LiveData
import com.example.clickbuy.models.Favorite

interface LocalSource {
    fun insertFavorite(favorite: Favorite)
    fun getFavorites(): LiveData<List<Favorite>>
    fun deleteFavorite(productId: Long)
    fun isFavorite(productId: Long): Boolean
}