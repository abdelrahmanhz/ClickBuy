package com.example.clickbuy.category.view


import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.clickbuy.R
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.clickbuy.category.viewmodel.ProductDetailsIDShow
import com.example.clickbuy.models.Product
import com.example.clickbuy.util.calculatePrice

class CategoryAdapter(val context: Context , categoryFragment: ProductDetailsIDShow) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>()  {
    var category: List<Product> = emptyList()
    var productDetailsInterface: ProductDetailsIDShow = categoryFragment

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
        val imageComping = category[position].image?.src
        Glide.with(holder.itemView.getContext()).load(imageComping).into(holder.brandImage)
        holder.titleTextView.text = category[position].title
        val priceConverted = calculatePrice(category[position].variants!![0].price)
        holder.priceTextView.text = priceConverted
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Recycle Click$position", Toast.LENGTH_SHORT).show()
            productDetailsInterface.SetProductDetailsID(category[position].id.toString())
        }
    }
    fun setListOfCategory(brands: List<Product>){
        this.category = brands.toList()
        notifyDataSetChanged()

    }
    override fun getItemCount(): Int {
        return category.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var brandImage: ImageView
        var titleTextView : TextView
        var priceTextView : TextView
        init {
            brandImage = itemView.findViewById(R.id.categoryImageCustomRow)
            titleTextView = itemView.findViewById(R.id.categoryTitleTextView)
            priceTextView = itemView.findViewById(R.id.categoryPriceTextView)
        }
    }
}
