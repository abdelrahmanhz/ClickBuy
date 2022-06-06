package com.example.clickbuy.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.clickbuy.models.Favorite

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite) : Long

    @Query("SELECT * FROM favorite")
    suspend fun getFavorites(): List<Favorite>

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE id = :productId)")
    suspend fun isFavorite(productId: Long): Boolean

    @Query("DELETE FROM favorite WHERE id = :productId")
    suspend fun deleteFavorite(productId: Long)
}