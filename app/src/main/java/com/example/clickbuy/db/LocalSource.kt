package com.example.clickbuy.db

import androidx.lifecycle.LiveData
import com.example.clickbuy.models.Favorite

interface LocalSource {
    suspend fun insertFavorite(favorite: Favorite)
    suspend fun getFavorites(): List<Favorite>
    suspend fun deleteFavorite(productId: Long)
    suspend fun isFavorite(productId: Long): Boolean
}