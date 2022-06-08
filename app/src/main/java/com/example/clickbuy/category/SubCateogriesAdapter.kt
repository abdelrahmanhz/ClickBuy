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
import com.example.clickbuy.models.SubCategory

private const val TAG = "SubCateogriesAdapter"

class SubCateogriesAdapter(val context: Context, subCategory: SubCategoriesFromFilterInterface) :
    RecyclerView.Adapter<SubCateogriesAdapter.ViewHolder>() {
    var subCategoryBrandInterface: SubCategoriesFromFilterInterface = subCategory
    var subCategory: List<Product> = emptyList()
    var subCategorySet: HashSet<SubCategory> = HashSet()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubCateogriesAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.custom_subcategory_row, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(
        holder: SubCateogriesAdapter.ViewHolder,
        position: Int
    ) {
        //  Log.i(TAG, "brand position: " + brand[position])
        /*     var categories = HashSet<Product>()
             for (i in 0..subCategorySet.count()!! - 1) {
                 categories.add(subCategory.get(i))
             }
             subCategory = categories.toList()*/
        holder.subCategoryTitle.text = subCategorySet.elementAt(position).product_type
        Log.i(TAG, "onCreate: categories---->Adapterrrrr " + subCategorySet)
        holder.itemView.setOnClickListener {
            subCategoryBrandInterface.showSubCategory(
                subCategorySet.elementAt(position).toString(),
                subCategorySet.elementAt(position).product_type
            )
        }
    }

    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount: " + subCategorySet.size)
        return subCategorySet.size
    }

    fun setListOfBrands(brands: HashSet<SubCategory>) {
        this.subCategorySet = brands
        Log.i(TAG, "setListOfBrands: ${brands.size}")
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var subCategoryTitle: TextView

        init {
            subCategoryTitle = itemView.findViewById(R.id.subCategoryFilterTitle)
        }
    }
}