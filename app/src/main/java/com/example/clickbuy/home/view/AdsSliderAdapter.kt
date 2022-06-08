package com.example.clickbuy.home.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.clickbuy.R
import com.smarteist.autoimageslider.SliderViewAdapter


private const val TAG = "SliderAdapter"

class AdsSliderAdapter() :
    SliderViewAdapter<AdsSliderAdapter.SliderAdapterViewHolder>() {

    private val mSliderItems: List<Int> = listOf(
        R.drawable.ads_logo,
        R.drawable.ads_logo_1,
        R.drawable.ads_logo_2,
        R.drawable.ads_logo_3,
        R.drawable.ads_logo_4,
        R.drawable.ads_logo_5
    )

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterViewHolder {
        val view: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.custom_row_slider, parent, false)
        return SliderAdapterViewHolder(view)

    }

    override fun onBindViewHolder(
        viewHolder: SliderAdapterViewHolder?,
        position: Int
    ) {
        viewHolder?.imageViewBackground?.setImageResource(mSliderItems[position])
        viewHolder?.imageViewBackground?.setOnClickListener {
            android.util.Log.i(TAG, "onBindViewHolder: ")
        }
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

    class SliderAdapterViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(
        itemView
    ) {
        var imageViewBackground: ImageView

        init {
            imageViewBackground = itemView.findViewById(R.id.ads_image);
        }
    }
}