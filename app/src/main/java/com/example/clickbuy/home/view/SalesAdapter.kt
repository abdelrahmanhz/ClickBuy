package com.example.clickbuy.home.view

import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.clickbuy.R
import com.bumptech.glide.Glide
import com.example.clickbuy.models.Product
import com.example.clickbuy.util.calculatePrice


private const val TAG = "SalesAdapter"

class SalesAdapter(val context: Context, homeFragment: ProductDetailsInterface) :
    RecyclerView.Adapter<SalesAdapter.ViewHolder>() {
    var sale: List<Product> = emptyList()
    var productDetailsInterface: ProductDetailsInterface = homeFragment
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SalesAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.custom_row_sales, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(
        holder: SalesAdapter.ViewHolder,
        position: Int
    ) {
        Log.i(TAG, "sale position: " + sale[position])
        val imageComping = sale[position].image.src
        holder.salesTitle.text = sale[position].title
        val priceConverted = calculatePrice(sale[0].variants!![0].price)
        holder.salesPrice.text =priceConverted
        Glide.with(holder.itemView.getContext()).load(imageComping).into(holder.saleImage)
        holder.itemView.setOnClickListener {
            productDetailsInterface.productDetailsShow(sale[position].id.toString())
        }
    }
    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount: " + sale.size)
        return sale.size
    }
    fun setListOfSales(sales: List<Product>) {
        this.sale = sales.toList()
        Log.i(TAG, "setListOfSales: ")
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var saleImage: ImageView
        var salesTitle: TextView
        var salesPrice: TextView
        init {
            saleImage = itemView.findViewById(R.id.salesImageCustomRow)
            salesTitle = itemView.findViewById(R.id.saleTitleTextView)
            salesPrice = itemView.findViewById(R.id.salePrice)
            salesTitle.isSelected = true
        }
    }
}
