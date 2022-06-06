package com.example.clickbuy.category.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.category.BrandsFilterAdapter
import com.example.clickbuy.category.SubCategoriesFromFilterInterface
import com.example.clickbuy.category.SubCateogriesAdapter
import com.example.clickbuy.category.viewmodel.CategoryViewModel
import com.example.clickbuy.category.viewmodel.CategoryViewModelFactory
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView

private const val TAG = "FilterFragment"

class FilterFragment : Fragment() ,SubCategoriesFromFilterInterface{
    private lateinit var brandFilterAdapter : BrandsFilterAdapter
    private lateinit var subCategoryFilterAdapter : SubCateogriesAdapter

    private lateinit var categoryFactory: CategoryViewModelFactory
    private lateinit var filterBrandsRecyclerView: RecyclerView
    private lateinit var subCategoryFilterRecyclerView: RecyclerView
    private lateinit var viewModel: CategoryViewModel
    private lateinit var womenImageView: CircleImageView
    private lateinit var menImageView: CircleImageView
    private lateinit var kidsImageView: CircleImageView
    var categoryTitleComingFromFilter: String = ""
    private val ID_WOMEN = "273053712523"
    private val ID_MEN = "273053679755"
    private val ID_KIDZ = "273053745291"
    private lateinit var tabLayout : TabLayout
//    var categoryTitleComingFromHome: String = ""
    val catergories = mutableListOf("Woman","Men","Kidz")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_filter, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryFactory = CategoryViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )

        initUI(view)

        setUpCategoryRecyclerView()
        setUpSubCategoryRecyclerView()
        viewModel = ViewModelProvider(this, categoryFactory).get(CategoryViewModel::class.java)
       // viewModel.getAllBrandsInFilter()
        viewModel.getSubCategories()
        //viewModel.getAllSubCategoriesFromFilter("273053679755","SHOES")
        viewModel.brand.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "categoryProducts: $it")
                brandFilterAdapter.setListOfBrands(it.smart_collections)
            }
        }
        viewModel.subCategory.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, " categoryProducts: $it")
                var categories = HashSet<String>()
                for (i in 0..it.products?.count()!! - 1) {
                    categories.add(it.products?.get(i)?.product_type.toString())
                }
                Log.i(TAG, "onCreate:categories---->" + categories)
                subCategoryFilterAdapter.setListOfBrands(it.products)
            }
        }




    }

//
//
//    private fun setMenCategory(){
//        viewModel.checkFromHomeOrCategory(ID_MEN, categoryTitleComingFromHome)
//    }
//    private fun setKidsCategory(){
//        viewModel.checkFromHomeOrCategory(ID_KIDZ, categoryTitleComingFromHome)
//    }
    private fun initUI(view: View) {
       // categoryRecyclerView = view.findViewById(R.id.brandCategoryRecyclerView)
           filterBrandsRecyclerView = view.findViewById(R.id.brandsFilterRecyclerView1)
    subCategoryFilterRecyclerView = view.findViewById(R.id.subCategoryFilterRecyclerView)
    }
    private fun setUpCategoryRecyclerView() {
         val layoutManager = LinearLayoutManager(FilterFragment().context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        brandFilterAdapter = BrandsFilterAdapter(requireContext())
        filterBrandsRecyclerView.layoutManager = layoutManager
        filterBrandsRecyclerView.adapter = brandFilterAdapter
    }
    private fun setUpSubCategoryRecyclerView() {
        val layoutManager = LinearLayoutManager(FilterFragment().context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        subCategoryFilterAdapter = SubCateogriesAdapter(requireContext(),this)
        subCategoryFilterRecyclerView.layoutManager = layoutManager
        subCategoryFilterRecyclerView.adapter = subCategoryFilterAdapter
    }

    private fun setUpBrandFilterRecyclerView() {
        val layoutManager = LinearLayoutManager(CategoryFragment().context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        brandFilterAdapter = BrandsFilterAdapter(requireContext())
        //brandsDetailsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        filterBrandsRecyclerView.adapter = brandFilterAdapter
    }

    fun setCategoryTitleFromFilter(categoryTitleDetails: String) {
        this.categoryTitleComingFromFilter = categoryTitleDetails
        Log.i(TAG, "setCategoryName:  $categoryTitleDetails")
    }
    companion object {

    }

    override fun showSubCategory(id: String, title: String) {
        Log.i(TAG, "brandDetailsShow: $title")
        var categoryDetails = CategoryFragment()
        requireActivity()?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame, categoryDetails).commit()
//        fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frame, brandDetails)?.commit()
        categoryDetails.setSubCategoryTitleAndId(id,title)
    }
}