package com.example.clickbuy.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.clickbuy.models.Favorite

class ConcreteLocalSource(private val context: Context) : LocalSource {

    private var productDao: ProductDao

    init {
        val db: AppDatabase? = AppDatabase.getInstance(context.applicationContext)
        productDao = db!!.ProductDao()
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        productDao.insertFavorite(favorite)
    }

    override suspend fun getFavorites(): List<Favorite> {
        return productDao.getFavorites()
    }

    override suspend fun deleteFavorite(productId: Long) {
        productDao.deleteFavorite(productId)
    }

    override suspend fun isFavorite(productId: Long): Boolean {
        return productDao.isFavorite(productId)
    }
}