package com.example.clickbuy.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LayoutAnimationController
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.home.BrandsAdapter
import com.example.clickbuy.home.SalesAdapter
import com.example.clickbuy.home.viewmodel.HomeViewModel
import com.example.clickbuy.home.viewmodel.HomeViewModelFactory
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import android.view.animation.AnimationUtils
import com.example.clickbuy.R
import com.example.clickbuy.category.view.CategoryFragment
import com.example.clickbuy.db.ConcreteLocalSource


private const val TAG = "HomeView"

class HomeFragment : Fragment(), CategoryBrandInterface , ProductDetailsInterface {
    private lateinit var brandAdapter: BrandsAdapter
    private lateinit var saleAdapter: SalesAdapter
    private lateinit var homeFactory: HomeViewModelFactory
    private lateinit var brandsRecyclerView: RecyclerView
    private lateinit var salesRecyclerView: RecyclerView

    private lateinit var viewModel: HomeViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.clickbuy.R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFactory = HomeViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        initUI(view)
        setUpBrandRecyclerView()
        setUpSaleRecyclerView()
        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)
        viewModel.getAllBrands()
        viewModel.brand.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "brand: $it")
                brandAdapter.setListOfBrands(it.smart_collections)
            }
        }
        viewModel.getAllSalesById()
        viewModel.saleId.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "sale: $it")
                saleAdapter.setListOfSales(it.products)
            }
        }
    }

    private fun initUI(view: View) {
        brandsRecyclerView = view.findViewById(com.example.clickbuy.R.id.brandsRecyclerView)

        val resId: Int = com.example.clickbuy.R.anim.lat
        val animation: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, resId)
        brandsRecyclerView.setLayoutAnimation(animation)
        salesRecyclerView = view.findViewById(com.example.clickbuy.R.id.salesRecyclerView)
    }

    private fun setUpBrandRecyclerView() {
        val layoutManager = LinearLayoutManager(HomeFragment().context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        brandAdapter = BrandsAdapter(requireContext(), this)
        brandsRecyclerView.layoutManager = layoutManager
        brandsRecyclerView.adapter = brandAdapter
    }

    private fun setUpSaleRecyclerView() {
        val layoutManager = LinearLayoutManager(HomeFragment().context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        saleAdapter = SalesAdapter(requireContext(), this)
        salesRecyclerView.layoutManager = layoutManager
        salesRecyclerView.adapter = saleAdapter
    }



    override fun categoryBrandShow(categoryTitleDetails: String) {
       Log.i(TAG, "brandDetailsShow: $categoryTitleDetails")
//        var categoryDetails = CategoryFragment()
//        requireActivity()?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame, categoryDetails).commit()
////        fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frame, brandDetails)?.commit()
//       categoryDetails.setCategoryTitle(categoryTitleDetails)
    }
    override fun productDetailsShow(id: String) {
        // Open Product Details
        Log.i(TAG, "productDetailsShow: " + id)
    }
}