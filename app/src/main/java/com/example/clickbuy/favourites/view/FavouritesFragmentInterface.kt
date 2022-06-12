package com.example.clickbuy.favourites.view

import com.example.clickbuy.models.DraftOrder

interface FavouritesFragmentInterface {
    fun deleteFavouriteItem(favorite: DraftOrder, position: Int)
    fun showFavouriteItemDetails(id: Long)
}