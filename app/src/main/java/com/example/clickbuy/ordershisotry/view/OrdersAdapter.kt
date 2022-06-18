package com.example.clickbuy.ordershisotry.view


import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.clickbuy.R
import com.example.clickbuy.models.Order
import com.example.clickbuy.ordershisotry.OrderDetailsInterface


private const val TAG = "OrdersAdapter"

class OrdersAdapter(val context: Context , orderFragment : OrderDetailsInterface) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>()  {
    var orderDetailsInterface : OrderDetailsInterface = orderFragment

    var order: List<Order> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):OrdersAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_row_orders, parent, false)
        val holder=ViewHolder(view)

        return holder
    }
    override fun onBindViewHolder(
        holder:OrdersAdapter.ViewHolder,
        position: Int
    ){
        holder.itemView.setOnClickListener {
            orderDetailsInterface.showOrderDetails(order[position].line_items,order[position].note_attributes)
        }
              holder.orderDateTextView.text = order[position].created_at
              holder.orderPriceTextView.text = order[position].current_total_price
              holder.orderNumberTextView.text = order[position].line_items?.size.toString() +"  " +"Items"
    }
    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount: " + order.size)
        return order.size
    }
    fun setListOfBrands(order: List<Order>){
        this.order = order.toList()
        Log.i(TAG, "setListOfBrands: ")
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderDateTextView :  TextView
        var orderNumberTextView : TextView
        var orderPriceTextView : TextView
        init {
             orderDateTextView    = itemView.findViewById(R.id.dateOfOrdersTextView)
             orderNumberTextView  = itemView.findViewById(R.id.numberOfOrdersTextView)
             orderPriceTextView   = itemView.findViewById(R.id.priceOfOrderTextView)


        }
    }
}
