package com.example.clickbuy.db

import androidx.lifecycle.LiveData
import com.example.clickbuy.models.Favorite

class ConcreteLocalSource(private val productDao: ProductDao): LocalSource {
    override suspend fun insertFavorite(favorite: Favorite) {
        productDao.insertFavorite(favorite)
    }

    override fun getFavorites(): LiveData<List<Favorite>> {
        return productDao.getFavorites()
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        productDao.deleteFavorite(favorite)
    }
}