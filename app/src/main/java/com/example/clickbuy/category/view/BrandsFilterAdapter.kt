package com.example.clickbuy.category.view


import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.clickbuy.R
import com.example.clickbuy.models.Brand

private const val TAG = "BrandsFilterAdapter"

class BrandsFilterAdapter(val context: Context) :
    RecyclerView.Adapter<BrandsFilterAdapter.ViewHolder>()  {
 //   var categoryBrandInterface : CategoryBrandInterface = homeFragment
    var brand: List<Brand> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):BrandsFilterAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_row_brands_filter, parent, false)
        val holder=ViewHolder(view)

        return holder
    }
    override fun onBindViewHolder(
        holder:BrandsFilterAdapter.ViewHolder,
        position: Int
    ){
        Log.i(TAG, "brand position: " + brand[position].id)
//        var imageComping = brand[position].image.src
//        Glide.with(holder.itemView.getContext()).load(imageComping).into(holder.brandImage);
    holder.brandTitle.text = brand[position].title

        holder.itemView.setOnClickListener {
          //  categoryBrandInterface.categoryBrandShow(brand[position].title)
        }

    }
    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount: " + brand.size)
        return brand.size
    }
    fun setListOfBrands(brands: List<Brand>){
        this.brand = brands.toList()
        Log.i(TAG, "setListOfBrands: ")
        notifyDataSetChanged()

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var brandTitle: TextView
        init {
            brandTitle = itemView.findViewById(R.id.brandFilterTitle)
        }
    }
}
