package com.example.clickbuy.home.view


import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.clickbuy.R
import com.example.clickbuy.models.Brand
import com.bumptech.glide.Glide


private const val TAG = "BrandsAdapter"

class BrandsAdapter(val context: Context, homeFragment: CategoryBrandInterface) :
    RecyclerView.Adapter<BrandsAdapter.ViewHolder>() {
    private var categoryBrandInterface: CategoryBrandInterface = homeFragment
    var brand: List<Brand> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrandsAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.brands_custom_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BrandsAdapter.ViewHolder,
        position: Int
    ) {
        val imageComping = brand[position].image?.src

        Glide.with(holder.itemView.context).load(imageComping).into(holder.brandImage)
        holder.itemView.setOnClickListener {
            categoryBrandInterface.setBrandName(brand[position].title.toString())
        }
    }

    override fun getItemCount(): Int {
        return brand.size
    }

    fun setListOfBrands(brands: List<Brand>) {
        this.brand = brands.toList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var brandImage: ImageView

        init {
            brandImage = itemView.findViewById(R.id.brandImageCustomRow)
        }
    }
}
