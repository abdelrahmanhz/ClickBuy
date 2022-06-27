package com.example.clickbuy.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.clickbuy.R
import com.example.clickbuy.models.DiscountCode
import com.example.clickbuy.models.PriceRule
import com.example.clickbuy.util.calculatePrice
import com.smarteist.autoimageslider.SliderViewAdapter


private const val TAG = "CouponsSliderAdapter"

class CouponsSliderAdapter(
    var context: Context,
    var couponsDetailsInterface: CouponsDetailsInterface
) :
    SliderViewAdapter<CouponsSliderAdapter.SliderAdapterViewHolder>() {

    private var couponsList: List<DiscountCode> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterViewHolder {
        val view: View =
            LayoutInflater.from(parent!!.context)
                .inflate(R.layout.custom_row_coupons, parent, false)
        return SliderAdapterViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder?, position: Int) {
        val discountCode = couponsList[position]
        val discountAmount = calculatePrice("10")
        viewHolder?.discountValue?.text =
            context.resources.getString(R.string.off).plus(" $discountAmount")
        viewHolder?.codeTextView?.text =
            context.resources.getString(R.string.use_this_code).plus("  ${discountCode.code}")

        viewHolder?.itemView?.setOnClickListener {
            couponsDetailsInterface.copyCouponsDetails(discountCode.code)
        }
        /*    val priceRule = priceRules[position]
            //      (-priceRule.value.toDouble()) * ConstantsValue.currencyValue
            val discountAmount = calculatePrice((-priceRule.value.toDouble()).toString())
            viewHolder?.discountValue?.text =
                context.resources.getString(R.string.off).plus(" $discountAmount")
            viewHolder?.codeTextView?.text =
                context.resources.getString(R.string.use_this_code).plus(" " + priceRule.title)

            viewHolder?.itemView?.setOnClickListener {
                couponsDetailsInterface.copyCouponsDetails(priceRule)
            }*/
    }

    override fun getCount(): Int {
        return couponsList.size
    }

    fun setList(couponsList: List<DiscountCode>) {
        Log.i(TAG, "setList: ")
        this.couponsList = couponsList
        notifyDataSetChanged()
    }

    class SliderAdapterViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(
        itemView
    ) {
        var discountValue: TextView
        var codeTextView: TextView

        init {
            discountValue = itemView.findViewById(R.id.discount_value_textView)
            codeTextView = itemView.findViewById(R.id.code_textView)
        }
    }
}