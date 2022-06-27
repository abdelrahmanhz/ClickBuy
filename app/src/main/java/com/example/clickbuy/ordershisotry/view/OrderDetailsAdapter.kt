package com.example.clickbuy.ordershisotry.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clickbuy.R
import com.example.clickbuy.models.BagItem
import com.example.clickbuy.models.NoteAttribute
import com.example.clickbuy.models.Order
import com.example.clickbuy.util.calculatePrice
import java.text.DecimalFormat


class OrderDetailsAdapter(val context: Context) :
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    private lateinit var lineItemList: List<BagItem>
    private lateinit var itemImageList: List<NoteAttribute>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDetailsAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.custom_row_order_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderDetailsAdapter.ViewHolder, position: Int) {
        holder.orderDetailsTitle.text = lineItemList[position].title
        holder.orderDetailsPrice.text = calculatePrice(lineItemList[position].price)
        holder.orderDetailsItemQuantity.text =
            DecimalFormat.getInstance().format(lineItemList[position].quantity)
        val imageComping = itemImageList[position].value
        Glide.with(holder.itemView.context).load(imageComping).into(holder.orderDetailsImage)
    }

    override fun getItemCount(): Int {
        return lineItemList.size
    }

    fun setListOfOrdersDetails(lineItemList: List<BagItem>, itemImageList: List<NoteAttribute>) {
        this.lineItemList = lineItemList
        this.itemImageList = itemImageList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderDetailsTitle: TextView
        var orderDetailsImage: ImageView
        var orderDetailsPrice: TextView
        var orderDetailsItemQuantity: TextView

        init {
            orderDetailsTitle = itemView.findViewById(R.id.titleItemTextViewOrderDetails)
            orderDetailsImage = itemView.findViewById(R.id.orderDetailsImageView)
            orderDetailsPrice = itemView.findViewById(R.id.priceItemTextViewOrderDetails)
            orderDetailsItemQuantity = itemView.findViewById(R.id.quantityItemTextViewOrderDetails)
        }
    }
}

interface OrderDetailsInterface {
    fun showOrderDetails(lineItemList: List<BagItem>?, itemImageList: List<NoteAttribute>?)
    fun showOrderDetails(order: Order)
}