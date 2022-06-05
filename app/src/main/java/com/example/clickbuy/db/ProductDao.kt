package com.example.clickbuy.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.clickbuy.models.Favorite

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    fun getFavorites(): LiveData<List<Favorite>>

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE id = :productId)")
    fun isFavorite(productId: Long): Boolean

    @Query("DELETE FROM favorite WHERE id = :productId")
    fun deleteFavorite(productId: Long)
}