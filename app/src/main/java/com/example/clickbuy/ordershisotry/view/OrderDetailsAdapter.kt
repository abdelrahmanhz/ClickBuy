package com.example.clickbuy.ordershisotry

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clickbuy.R
import com.example.clickbuy.models.BagItem
import com.example.clickbuy.models.ItemImage
import com.example.clickbuy.models.LineItem
import com.example.clickbuy.models.NoteAttribute


private const val TAG = "OrdersDetailsAdapter"

class OrderDetailsAdapter(val context: Context , orderFragment : OrderDetailsInterface) :
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>()  {
    var orderDetailsInterface : OrderDetailsInterface = orderFragment
    lateinit var lineItemList: List<BagItem>
    lateinit var  itemImageList: List<NoteAttribute>

    //var order: List<Order> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):OrderDetailsAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_row_order_details, parent, false)
        val holder=ViewHolder(view)

        return holder
    }
    override fun onBindViewHolder(
        holder:OrderDetailsAdapter.ViewHolder,
        position: Int
    ){
        holder.orderDetailsTitle.text = lineItemList[position].title
        holder.orderDetailsPrice.text =  lineItemList[position].price
        //((lineItemList[position].fulfillable_quantity) *
        var imageComping = itemImageList[position].value
        Glide.with(holder.itemView.getContext()).load(imageComping).into(holder.orderDetailsImage);
    }
    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount: " + lineItemList.size)
        return lineItemList.size
    }
    fun setListOfOrdersDetails(lineItemList: List<BagItem>, itemImageList: List<NoteAttribute>){
        this.lineItemList = lineItemList
        this.itemImageList = itemImageList
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderDetailsTitle : TextView
        var orderDetailsImage : ImageView
        var orderDetailsPrice : TextView
        init {
            orderDetailsTitle    = itemView.findViewById(R.id.titleItemTextViewOrderDetails)
            orderDetailsImage  = itemView.findViewById(R.id.orderDetailsImageView)
            orderDetailsPrice   = itemView.findViewById(R.id.priceItemTextViewOrderDetails)
        }
    }
}

interface OrderDetailsInterface {
    fun showOrderDetails(lineItemList: List<BagItem>?, itemImageList: List<NoteAttribute>?)
}