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

class ProductReviewsAdapter () : RecyclerView.Adapter<ProductReviewsAdapter.ProductReviewViewHolder>(){

    private var reviews = listOf(
        Review("Ahmed S.", 3.7f, "Oversize.", 1),
        Review("Mohammed K.", 4.5f, "it’s a really nice t-shirts and i will definitely buy it again", 1),
        Review("Hanna A.", 5f, "Nice and loose.", 1),
        Review("Aya H.", 5f, "love this too so much! quality is great.", 1),
        Review("Asmaa H.", 2.8f, "nice material", 1),
        Review("Hager S.", 3.2f, "beautiful colors, looks just like the pictures.", 1),
        Review("Hala N.", 4.8f, "Very nice. Thanks for it!!", 1),
        Review("Naira R.", 4.8f, "Really good quality!! I absolutely love this!", 1),
    )
    private val count = (1..reviews.size).shuffled().take(1)[0]

    inner class ProductReviewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        val reviewerImage: ImageView = itemView.findViewById(R.id.review_item_image)
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
//        holder.reviewerImage.setImageResource(R.drawable.default_image)
        holder.review.text = reviews[(reviews.indices).shuffled().take(1)[0]].review
        holder.reviewer.text = reviews[(reviews.indices).shuffled().take(1)[0]].reviewer
        holder.rate.rating = reviews[(reviews.indices).shuffled().take(1)[0]].rating
    }

    override fun getItemCount(): Int {
        Log.i("TAG", "getItemCount:  $count")
        return count
    }
}



