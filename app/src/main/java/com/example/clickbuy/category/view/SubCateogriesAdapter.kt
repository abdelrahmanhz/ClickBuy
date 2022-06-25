package com.example.clickbuy.category.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.SubCategory


class SubCateogriesAdapter(val context: Context, subCategory: SubCategoriesFromFilterInterface) :
    RecyclerView.Adapter<SubCateogriesAdapter.ViewHolder>() {
    var subCategoryBrandInterface: SubCategoriesFromFilterInterface = subCategory
    var subCategorySet: HashSet<SubCategory> = HashSet()
    private var checkedPosition = -1

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
        holder.subCategoryTitle.text = subCategorySet.elementAt(position).product_type
        holder.bind(subCategorySet.elementAt(position))
        when (subCategorySet.elementAt(position).product_type) {
            "T-SHIRTS" -> holder.subCategoryImage.setImageResource(R.drawable.ic_tshirt)
            "ACCESSORIES" -> holder.subCategoryImage.setImageResource(R.drawable.ic_caps)
            "SHOES" -> holder.subCategoryImage.setImageResource(R.drawable.ic_shoses)
        }
    }

    override fun getItemCount(): Int {
        return subCategorySet.size
    }

    fun setListOfSubCategories(subCategories: HashSet<SubCategory>) {
        this.subCategorySet = subCategories
        notifyDataSetChanged()
    }

    fun reset() {
     checkedPosition = -1
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var subCategoryTitle: TextView
        var subCategoryImage: ImageView

        init {
            subCategoryTitle = itemView.findViewById(R.id.subCategoryFilterTitle)
            subCategoryImage = itemView.findViewById(R.id.imageViewFilterSubCategory)
        }

        fun bind(elementAt: SubCategory) {
            if (checkedPosition == -1) {
                itemView.setBackgroundColor(context.resources.getColor(R.color.transparent))
            } else {
                if (checkedPosition == adapterPosition) {
                    itemView.setBackgroundColor(Color.BLACK)
                } else {
                    itemView.setBackgroundColor(context.resources.getColor(R.color.transparent))
                }
            }
            itemView.setOnClickListener {
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    itemView.setBackgroundColor(Color.BLACK)
                    checkedPosition = adapterPosition
                    subCategoryBrandInterface.setSubCategoryTitle(
                        elementAt.product_type
                    )
                }

            }
        }
    }
}