package com.example.clickbuy.bag.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.BagItem
import com.example.clickbuy.models.NoteAttribute
import com.example.clickbuy.util.ConstantsValue
import com.example.clickbuy.util.Extensions.load
import com.example.clickbuy.util.calculatePrice
import java.util.*

private const val TAG = "BagAdapter"

class BagAdapter(var updatingItemsAtBag: UpdatingItemsAtBag) :
    RecyclerView.Adapter<BagAdapter.ViewHolder>() {

    private var bagList: List<BagItem> = ArrayList()
    private var imagesList: List<NoteAttribute> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_bag, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        val product = bagList[position]

        holder.minusTextView.isEnabled = true
        holder.plusTextView.isEnabled = true

        holder.productImageView.load(imagesList[position].value)

        holder.productNameTextView.text = product.name
        holder.productPriceTextView.text = calculatePrice(product.price)
        holder.productNumberTextView.text = product.quantity.toString()

        holder.minusTextView.setOnClickListener {
            updatingItemsAtBag.onQuantityDecreased(position)
            holder.minusTextView.isEnabled = false
        }
        holder.plusTextView.setOnClickListener {
            updatingItemsAtBag.onQuantityIncreased(position)
            holder.plusTextView.isEnabled = false
        }
    }

    override fun getItemCount(): Int {
        return bagList.size
    }

    fun setList(bagList: List<BagItem>, imagesList: List<NoteAttribute>) {
        this.bagList = bagList
        this.imagesList = imagesList
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