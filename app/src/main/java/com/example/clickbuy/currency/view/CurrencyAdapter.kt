package com.example.clickbuy.currency.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.Currency
import com.example.clickbuy.util.ConstantsValue
import java.util.*


class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private var currencyList: List<Currency> = ArrayList()
    private var checkedPosition = ConstantsValue.to

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_currency, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = currencyList[position]
        if (currency.enabled)
            holder.bind(currency)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    fun setList(currencyList: List<Currency>) {
        this.currencyList = currencyList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currencyTextView: TextView = itemView.findViewById(R.id.currency_textView)
        private var checkedImageView: ImageView = itemView.findViewById(R.id.checked_imageView)

        fun bind(currency: Currency) {
            currencyTextView.text = currency.currency
            if (checkedPosition.isEmpty()) {
                checkedImageView.visibility = View.GONE
            } else {
                if (checkedPosition == currencyTextView.text.toString()) {
                    checkedImageView.visibility = View.VISIBLE
                } else {
                    checkedImageView.visibility = View.GONE
                }
            }
            itemView.setOnClickListener {
                checkedImageView.visibility = View.VISIBLE
                if (checkedPosition != currencyTextView.text.toString()) {
                    ConstantsValue.to = currencyTextView.text.toString()
                    checkedPosition = currencyTextView.text.toString()
                    notifyItemChanged(adapterPosition)
                    notifyDataSetChanged()

                }
            }
        }
    }
}