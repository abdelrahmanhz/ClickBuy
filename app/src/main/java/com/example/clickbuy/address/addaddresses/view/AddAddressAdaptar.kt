package com.example.clickbuy.address.addaddresses.view

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.clickbuy.R
import com.example.clickbuy.models.Result


class AddAddressAdapter(var addAddressFragment: AddAddressFragment) :
    RecyclerView.Adapter<AddAddressAdapter.ViewHolder>() {
    var addresses: List<Result> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddAddressAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_row_add_address, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddAddressAdapter.ViewHolder, position: Int) {
        val address = addresses[position]
        holder.addressName.text = address.formatted
        holder.itemView.setOnClickListener {
            addAddressFragment.onAddressSelected(address)
        }
    }

    override fun getItemCount(): Int {
        return addresses.size
    }

    fun setListOfAddresses(addresses: List<Result>) {
        this.addresses = addresses.toList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var addressName: TextView

        init {
            addressName = itemView.findViewById(R.id.address_textView)
        }
    }
}
