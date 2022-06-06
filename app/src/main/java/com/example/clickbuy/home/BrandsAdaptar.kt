package com.example.clickbuy.home


import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.clickbuy.R
import com.example.clickbuy.models.Brand
import com.bumptech.glide.Glide
import com.example.clickbuy.home.view.CategoryBrandInterface


private const val TAG = "BrandsAdapter"

class BrandsAdapter(val context: Context, homeFragment: CategoryBrandInterface ) :
    RecyclerView.Adapter<BrandsAdapter.ViewHolder>()  {
     var categoryBrandInterface : CategoryBrandInterface = homeFragment
    var brand: List<Brand> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):BrandsAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.brands_custom_row, parent, false)
        val holder=ViewHolder(view)

        return holder
    }
    override fun onBindViewHolder(
        holder:BrandsAdapter.ViewHolder,
        position: Int

    ){
        Log.i(TAG, "brand position: " + brand[position].id)
        var imageComping = brand[position].image.src
        Glide.with(holder.itemView.getContext()).load(imageComping).into(holder.brandImage);

        holder.itemView.setOnClickListener {
            categoryBrandInterface.categoryBrandShow(brand[position].title)
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
        var brandImage: ImageView
        init {
            brandImage = itemView.findViewById(R.id.brandImageCustomRow)
        }
    }
}
