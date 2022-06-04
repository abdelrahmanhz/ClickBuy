package com.example.clickbuy.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.clickbuy.models.Favorite

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    fun getFavorites(): LiveData<List<Favorite>>

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)
}