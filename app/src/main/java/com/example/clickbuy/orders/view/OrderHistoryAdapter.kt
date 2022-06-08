package com.example.clickbuy.orders.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.OrderHistory
import java.util.*

class OrderHistoryAdapter(context: Context) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    private val TAG = "OrderHistoryAdapter"
    private var context = context
    private var orderList: List<OrderHistory> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_order_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        var order = orderList[position]
        holder.orderDateTextView.text = order.orderDate
        holder.orderNumberTextView.text = order.orderNumber
        holder.orderPriceTextView.text = order.orderPrice
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun setList(orderList: List<OrderHistory>) {
        this.orderList = orderList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderDateTextView: TextView = itemView.findViewById(R.id.order_date_textView)
        var orderPriceTextView: TextView = itemView.findViewById(R.id.order_price_textView)
        var orderNumberTextView: TextView = itemView.findViewById(R.id.order_number_textView)

    }

}