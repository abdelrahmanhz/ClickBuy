package com.example.clickbuy.favourites.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.favourites.view.FavouritesFragmentInterface
import com.example.clickbuy.models.DraftOrder
import com.example.clickbuy.models.Favorite
import com.example.clickbuy.util.Extensions.load

class FavouritesAdapter(
    private var favourites: ArrayList<DraftOrder>,
    private var view: FavouritesFragmentInterface
): RecyclerView.Adapter<FavouritesAdapter.FavouriteViewHolder>() {
    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favouriteImage: ImageView = itemView.findViewById(R.id.fav_item_img)
        val favouriteTitle: TextView = itemView.findViewById(R.id.fav_item_title)
        val favouritesPrice: TextView = itemView.findViewById(R.id.fav_item_price)
        val favouriteDeleteImage : ImageView = itemView.findViewById(R.id.fav_item_delete_image)
        val favouriteCardView: CardView = itemView.findViewById(R.id.fav_card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favourite_item, parent, false))
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        favourites[position].note_attributes?.first()?.value?.let { holder.favouriteImage.load(it) }
        favourites[position].line_items?.first()?.title.let { holder.favouriteTitle.text = it}
        favourites[position].line_items?.first()?.price.let { holder.favouritesPrice.text = it}
        holder.favouriteDeleteImage.setOnClickListener {
            view.deleteFavouriteItem(favourites[position], position)
        }

        holder.favouriteCardView.setOnClickListener {
            favourites[position].line_items?.get(0)?.product_id?.let { id ->
                view.showFavouriteItemDetails(id)
            }
        }
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    fun setFavourites(favourites: ArrayList<DraftOrder>){
        this.favourites.clear()
        this.favourites = favourites
        notifyDataSetChanged()
    }
}