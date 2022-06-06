package com.example.clickbuy.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.clickbuy.R

object Extensions {
    fun ImageView.load(url: String){
        Glide.with(context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
            ).into(this)
    }
}