package com.example.clickbuy.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.clickbuy.models.Favorite

class ConcreteLocalSource(private val context: Context): LocalSource {

    private var productDao: ProductDao

    init {
        val db: AppDatabase? = AppDatabase.getInstance(context.applicationContext)
        productDao = db!!.ProductDao()
    }

    override fun insertFavorite(favorite: Favorite) {
        productDao.insertFavorite(favorite)
    }

    override fun getFavorites(): LiveData<List<Favorite>> {
        return productDao.getFavorites()
    }

    override fun deleteFavorite(productId: Long) {
        productDao.deleteFavorite(productId)
    }

    override fun isFavorite(productId: Long): Boolean {
        return productDao.isFavorite(productId)
    }
}