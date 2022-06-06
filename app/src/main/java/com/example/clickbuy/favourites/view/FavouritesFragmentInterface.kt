package com.example.clickbuy.favourites.view

import com.example.clickbuy.models.Favorite

interface FavouritesFragmentInterface {
    fun deleteFavouriteItem(favorite: Favorite, position: Int)
    fun showFavouriteItemDetails(id: Long)
}