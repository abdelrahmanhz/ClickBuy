package com.example.clickbuy.category

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.Product

private const val TAG = "SubCateogriesAdapter"

class SubCateogriesAdapter(val context: Context, subCategory : SubCategoriesFromFilterInterface) :
    RecyclerView.Adapter<SubCateogriesAdapter.ViewHolder>()  {
      var subCategoryBrandInterface : SubCategoriesFromFilterInterface = subCategory
    var subCategory: List<Product> = emptyList()
    var subCategorySet : List<String> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):SubCateogriesAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_subcategory_row, parent, false)
        val holder=ViewHolder(view)

        return holder
    }
    override fun onBindViewHolder(
        holder:SubCateogriesAdapter.ViewHolder,
        position: Int
    ){
      //  Log.i(TAG, "brand position: " + brand[position])
        var categories = HashSet<Product>()
        for (i in 0..subCategory.count()!! - 1) {
            categories.add(subCategory.get(i))
        }
        subCategory = categories.toList()
        holder.subCategoryTitle.text =categories.elementAt(position).product_type
        Log.i(TAG, "onCreate: categories---->Adapterrrrr " + categories)
        holder.itemView.setOnClickListener {
                 subCategoryBrandInterface.showSubCategory(subCategory[position].id.toString(),subCategory[position].product_type.toString())
        }

    }
    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount: " + subCategory.size)
        return subCategory.size
    }
    fun  setListOfBrands(brands: List<Product>){
        this.subCategory = brands.toList()
        Log.i(TAG, "setListOfBrands: ")
        notifyDataSetChanged()

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var subCategoryTitle: TextView
        init {
            subCategoryTitle = itemView.findViewById(R.id.subCategoryFilterTitle)
        }
    }
}
