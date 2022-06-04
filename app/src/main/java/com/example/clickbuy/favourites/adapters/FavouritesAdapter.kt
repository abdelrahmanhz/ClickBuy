package com.example.clickbuy.favourites.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.Favorite
import com.example.clickbuy.util.Extensions.load

class FavouritesAdapter(
    private var favourites: List<Favorite>,
): RecyclerView.Adapter<FavouritesAdapter.FavouriteViewHolder>() {
    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favouriteImage: ImageView = itemView.findViewById(R.id.fav_item_img)
        val favouriteTitle: TextView = itemView.findViewById(R.id.fav_item_title)
        val favouritesPrice: TextView = itemView.findViewById(R.id.fav_item_price)
        val favouriteDeleteImage : ImageView = itemView.findViewById(R.id.fav_item_delete_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favourite_item, parent, false))
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.favouriteImage.load(favourites[position].image)
        holder.favouriteTitle.text = favourites[position].price
        holder.favouriteDeleteImage.setOnClickListener {  }
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    fun setFavourites(favourites: List<Favorite>){
        this.favourites = favourites
    }
}