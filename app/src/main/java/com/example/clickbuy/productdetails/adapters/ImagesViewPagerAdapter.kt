package com.example.clickbuy.productdetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clickbuy.R

class ImagesViewPagerAdapter(private var images: List<String>) : RecyclerView.Adapter<ImagesViewPagerAdapter.ImagesViewHolder>(){

    inner class ImagesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.pager_image_view)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesViewHolder {
        return ImagesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pager_item, parent, false))
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(images.get(position))
            .centerCrop()
            .placeholder(R.drawable.default_image)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setImages(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }

}