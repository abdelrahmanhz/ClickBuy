package com.example.clickbuy.address.showaddresses.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.CustomerAddress
import java.util.*

private const val TAG = "AddressAdapter"

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    private var addressList: List<CustomerAddress> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_address, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressAdapter.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        val address = addressList[position]
        holder.typeOfAddressTextView.text = address.address1
        holder.addressTextView.text =
            address.city.plus(" , ").plus(address.province).plus(" , " + address.country)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun setList(addressList: List<CustomerAddress>) {
        this.addressList = addressList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var typeOfAddressTextView: TextView =
            itemView.findViewById(R.id.type_of_address_textView)
        var addressTextView: TextView = itemView.findViewById(R.id.address_textView)
    }

}