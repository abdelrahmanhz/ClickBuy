package com.example.clickbuy.category


import androidx.recyclerview.widget.RecyclerView
import java.util.*
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.clickbuy.R
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.clickbuy.models.Product


private const val TAG = "categoryAdapter"

class CategoryAdapter(val context: Context) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>()  {
     //var brandInterface : BrandDetailsInterface = homeFragment
    var category: List<Product> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):CategoryAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.category_custom_row, parent, false)
        val holder=ViewHolder(view)

        return holder
    }
    override fun onBindViewHolder(
        holder:CategoryAdapter.ViewHolder,
        position: Int

    ) {
      Log.i(TAG, "Category position: " + category[position])

       // holder.brandImage.setima = brand[position].vendor
        var imageComping = category[position].image?.src
        Glide.with(holder.itemView.getContext()).load(imageComping).into(holder.brandImage);
     //   holder.brandImage.setImageResource(R.drawable.adidas_logo)
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Recycle Click$position", Toast.LENGTH_SHORT).show()
        }


    }
    fun setListOfCategory(brands: List<Product>){
        this.category = brands.toList()
        Log.i(TAG, "setListOfCategory: ")
        notifyDataSetChanged()

    }
    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount: " + category.size)
        return category.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var brandImage: ImageView
        init {
            brandImage = itemView.findViewById(R.id.categoryImageCustomRow)
        }
    }
}
