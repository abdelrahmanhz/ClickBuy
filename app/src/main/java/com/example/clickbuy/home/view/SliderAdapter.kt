package com.example.clickbuy.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.clickbuy.R
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapter(context: Context, sliderDataArrayList: ArrayList<Int>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>() {

    private val mSliderItems: List<Int> = sliderDataArrayList

    class SliderAdapterViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(
        itemView
    ) {
        var imageViewBackground: ImageView

        init {
            imageViewBackground = itemView.findViewById(R.id.ads_image);
        }
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterViewHolder {
        val view: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.custom_row_slider, parent, false)
        return SliderAdapterViewHolder(view)

    }

    override fun onBindViewHolder(
        viewHolder: SliderAdapter.SliderAdapterViewHolder?,
        position: Int
    ) {
        viewHolder?.imageViewBackground?.setImageResource(mSliderItems[position])
    }

}