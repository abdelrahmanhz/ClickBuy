package com.example.clickbuy.me.view

import android.content.Context
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

class AddressAdapter(context: Context) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    private val TAG = "AddressAdapter"
    private var context = context
    private var addressList: List<CustomerAddress> = ArrayList()
    private var checkedPosition = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder: ")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.custom_row_address, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressAdapter.ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        var address = addressList[position]
        holder.bind(address)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun setList(addressList: List<CustomerAddress>) {
        this.addressList = addressList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var typeOfAddressTextView: TextView = itemView.findViewById(R.id.type_of_address_textView)
        var addressTextView: TextView = itemView.findViewById(R.id.address_textView)
        var checkedImageView: ImageView = itemView.findViewById(R.id.checked_imageView)


        fun bind(address: CustomerAddress) {
            if (checkedPosition == -1) {
                checkedImageView.visibility = View.GONE
            } else {
                if (checkedPosition == adapterPosition) {
                    checkedImageView.visibility = View.VISIBLE
                } else {
                    checkedImageView.visibility = View.GONE
                }
            }
            typeOfAddressTextView.text = address.first_name.plus(address.last_name)
            addressTextView.text =
                address.address1.plus(address.address2).plus(address.city).plus(address.province)
                    .plus(address.country)
            itemView.setOnClickListener {
                checkedImageView.setVisibility(View.VISIBLE)
                if (checkedPosition !== adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
            }
        }
    }

}