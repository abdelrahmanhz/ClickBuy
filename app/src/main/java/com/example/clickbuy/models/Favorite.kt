package com.example.clickbuy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite (
    @PrimaryKey
    val id: Long,
    val title: String,
    val price: String,
    val image: String
)