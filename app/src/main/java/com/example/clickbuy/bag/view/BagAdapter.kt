package com.example.clickbuy.bag.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.BagItem
import com.example.clickbuy.models.LineItem
import com.example.clickbuy.models.ShoppingBag
import java.util.*

private const val TAG = "BagAdapter"

class BagAdapter(context: Context) :
    RecyclerView.Adapter<BagAdapter.ViewHolder>() {

    private var context = context
    private var bagList: List<BagItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_bag, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        val product = bagList[position]
        // holder.productImageView.setImageResource(product.imageView)
        holder.productNameTextView.text = product.name
        holder.productPriceTextView.text = product.price
        holder.productNumberTextView.text = product.quantity.toString()
        holder.minusTextView.setOnClickListener {
            product.quantity--
            if (product.quantity == 0) {
                Toast.makeText(context, "Product deleted", Toast.LENGTH_SHORT).show()
            } else {
                holder.productNumberTextView.text = product.quantity.toString()
            }
        }

        holder.plusTextView.setOnClickListener {
            product.quantity++
            holder.productNumberTextView.text = product.quantity.toString()
        }
    }

    override fun getItemCount(): Int {
        return bagList.size
    }

    fun setList(bagList: List<BagItem>) {
        this.bagList = bagList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productNameTextView: TextView = itemView.findViewById(R.id.product_name_textView)
        var productImageView: ImageView = itemView.findViewById(R.id.product_imageView)
        var productPriceTextView: TextView = itemView.findViewById(R.id.product_price_textView)
        var productNumberTextView: TextView = itemView.findViewById(R.id.productNumber_textView)
        var plusTextView: ImageView = itemView.findViewById(R.id.plus_imageView)
        var minusTextView: ImageView = itemView.findViewById(R.id.minus_textView)

    }

}