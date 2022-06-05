package com.example.clickbuy.db

import androidx.room.TypeConverter
import com.example.clickbuy.models.Favorite
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun FavouriteListToJson(value: List<Favorite>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToFavouriteList(value: String) =
        Gson().fromJson(value, Array<Favorite>::class.java).toList()
}