package com.example.clickbuy.home.view


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.clickbuy.R
import com.example.clickbuy.models.DiscountCode
import com.smarteist.autoimageslider.SliderViewAdapter


private const val TAG = "CouponsSliderAdapter"

class CouponsSliderAdapter(var context: Context) :
    SliderViewAdapter<CouponsSliderAdapter.SliderAdapterViewHolder>() {

    private var clipboardManager: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private var couponsList: List<DiscountCode> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterViewHolder {
        val view: View =
            LayoutInflater.from(parent!!.context)
                .inflate(R.layout.custom_row_coupons, parent, false)
        return SliderAdapterViewHolder(view)

    }

    override fun onBindViewHolder(
        viewHolder: SliderAdapterViewHolder?,
        position: Int
    ) {
        val discountCode = couponsList[position]
        viewHolder?.discountValue?.text = context.resources.getString(R.string.off).plus(" 10%")
        viewHolder?.codeTextView?.text =
            context.resources.getString(R.string.discount_code).plus(discountCode.code)

        viewHolder?.itemView?.setOnClickListener {
            val data = ClipData.newPlainText("text", discountCode.code)
            clipboardManager.setPrimaryClip(data)

            Log.i(
                TAG,
                "onBindViewHolder: " + clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
            )
        }
    }

    override fun getCount(): Int {
        Log.i(TAG, "getCount: ---> " + couponsList.size)
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