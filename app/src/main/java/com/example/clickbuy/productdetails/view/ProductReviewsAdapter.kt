package com.example.clickbuy.productdetails.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.Review

class ProductReviewsAdapter (var reviews: List<Review>) : RecyclerView.Adapter<ProductReviewsAdapter.ProductReviewViewHolder>(){

    private val count = (1..reviews.size).shuffled().take(1)[0]

    inner class ProductReviewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val reviewerImage: ImageView = itemView.findViewById(R.id.review_item_image)
        val reviewer: TextView = itemView.findViewById(R.id.review_item_reviewer)
        val review: TextView = itemView.findViewById(R.id.review_item_desc)
        val rate : RatingBar = itemView.findViewById(R.id.review_item_rate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductReviewViewHolder {
        return ProductReviewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProductReviewViewHolder, position: Int) {
        holder.reviewerImage.setImageResource(R.drawable.avatar)
        holder.review.text = reviews[(0..reviews.size-1).shuffled().take(1)[0]].review
        holder.reviewer.text = reviews[(0..reviews.size-1).shuffled().take(1)[0]].reviewer
        holder.rate.rating = reviews[(0..reviews.size-1).shuffled().take(1)[0]].rating
    }

    override fun getItemCount(): Int {
        Log.i("TAG", "getItemCount:  $count")
        return count
    }
}



