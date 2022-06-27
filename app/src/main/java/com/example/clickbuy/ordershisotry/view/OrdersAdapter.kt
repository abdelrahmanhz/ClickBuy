package com.example.clickbuy.ordershisotry.view

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.Order
import com.example.clickbuy.util.calculatePrice
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class OrdersAdapter(val context: Context, orderFragment: OrderDetailsInterface) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    private var orderDetailsInterface: OrderDetailsInterface = orderFragment
    private var order: List<Order> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.custom_row_orders, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: OrdersAdapter.ViewHolder, position: Int) {

        val date =
            order[position].created_at?.split(Regex("T"), order[position].created_at?.length!!)
        val time = date?.get(1)?.split(Regex("\\+"), date.size)

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date2 = dateFormatter.parse(date?.get(0))
        val time2 = timeFormatter.parse(time?.get(0))
        holder.orderDateTextView.text =
            dateFormatter.format(date2).plus(context.resources.getString(R.string.at))
                .plus(timeFormatter.format(time2))

        val convertedPrice = calculatePrice(order[position].current_total_price.toString())
        holder.orderPriceTextView.text = convertedPrice

        holder.orderNumberTextView.text =
            DecimalFormat.getInstance()
                .format(order[position].line_items?.size)
                .plus("  " + context.resources.getString(R.string.items))

        holder.itemView.setOnClickListener {
            orderDetailsInterface.showOrderDetails(
                /*order[position].line_items,
                order[position].note_attributes*/
                order[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return order.size
    }

    fun setListOfOrders(order: List<Order>) {
        this.order = order.toList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderDateTextView: TextView
        var orderNumberTextView: TextView
        var orderPriceTextView: TextView

        init {
            orderDateTextView = itemView.findViewById(R.id.dateOfOrdersTextView)
            orderNumberTextView = itemView.findViewById(R.id.numberOfOrdersTextView)
            orderPriceTextView = itemView.findViewById(R.id.priceOfOrderTextView)
        }
    }
}
