package com.example.clickbuy.favourites.view

import com.example.clickbuy.models.Favourite

interface FavouritesFragmentInterface {
    fun deleteFavouriteItem(favorite: Favourite, position: Int)
    fun showFavouriteItemDetails(id: Long)
}