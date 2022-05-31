package com.example.clickbuy.home


import androidx.recyclerview.widget.RecyclerView
import java.util.*
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.clickbuy.R
import com.example.clickbuy.models.Brand
import kotlin.collections.HashSet
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.clickbuy.home.view.BrandDetailsInterface
import com.example.clickbuy.home.view.HomeFragment


private const val TAG = "BrandsAdapter"

class BrandsAdapter(val context: Context, homeFragment: BrandDetailsInterface) :
    RecyclerView.Adapter<BrandsAdapter.ViewHolder>()  {
     var brandInterface : BrandDetailsInterface = homeFragment

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

    ) {
        Log.i(TAG, "brand position: " + brand[position].id)

       // holder.brandImage.setima = brand[position].vendor
        var imageComping = brand[position].image.src
        Glide.with(holder.itemView.getContext()).load(imageComping).into(holder.brandImage);

       //holder.brandImage.setImageResource(R.drawable.adidas_logo)
//        when(imageComping){
//            "ADIDAS" -> holder.brandImage.setImageResource(R.drawable.adidas_logo)
//            "ASICS TIGER" -> holder.brandImage.setImageResource(R.drawable.asics_tiger)
//            "CONVERSE" -> holder.brandImage.setImageResource(R.drawable.converse2)
//            "DR MARTENS" -> holder.brandImage.setImageResource(R.drawable.drmartens)
//            "FLEX FIT" -> holder.brandImage.setImageResource(R.drawable.flexfit)
//            "HERSCHEL" -> holder.brandImage.setImageResource(R.drawable.herschel)
//            "NIKE" -> holder.brandImage.setImageResource(R.drawable.nike)
//            "PALLADIUM" -> holder.brandImage.setImageResource(R.drawable.palladium)
//            "PUMA" -> holder.brandImage.setImageResource(R.drawable.puma)
//            "SUPRA" -> holder.brandImage.setImageResource(R.drawable.supra)
//            "TIMBERLAND" -> holder.brandImage.setImageResource(R.drawable.timberland)
//            "VANS" -> holder.brandImage.setImageResource(R.drawable.vans)
//            else -> holder.brandImage.setImageResource(R.drawable.default_brands)
//        }
        holder.itemView.setOnClickListener {
            brandInterface.brandDetailsShow(brand[position].id)
            Toast.makeText(context, "Recycle Click$position", Toast.LENGTH_SHORT).show()
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
