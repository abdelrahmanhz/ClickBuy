package com.example.clickbuy.productdetails.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.util.Extensions.load

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
        holder.productImage.load(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setImages(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }
}