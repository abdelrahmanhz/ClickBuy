package com.example.clickbuy.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.category.view.CategoryFragment
import com.example.clickbuy.home.BrandsAdapter
import com.example.clickbuy.home.SalesAdapter
import com.example.clickbuy.home.viewmodel.HomeViewModel
import com.example.clickbuy.home.viewmodel.HomeViewModelFactory
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.smarteist.autoimageslider.SliderView


private const val TAG = "HomeView"

class HomeFragment : Fragment(), CategoryBrandInterface, ProductDetailsInterface {
    private lateinit var brandAdapter: BrandsAdapter
    private lateinit var saleAdapter: SalesAdapter
    private lateinit var homeFactory: HomeViewModelFactory
    private lateinit var brandsRecyclerView: RecyclerView
    private lateinit var salesRecyclerView: RecyclerView
    private lateinit var viewModel: HomeViewModel
    private lateinit var adsSlider: SliderView
    var sliderDataArrayList: ArrayList<Int> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
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
        brandsRecyclerView = view.findViewById(R.id.brandsRecyclerView)

        val resId: Int = R.anim.lat
        val animation: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, resId)
        brandsRecyclerView.layoutAnimation = animation
        salesRecyclerView = view.findViewById(R.id.salesRecyclerView)

        adsSlider = view.findViewById(R.id.ads_sliderView)
        sliderDataArrayList.add(R.drawable.ads_logo)
        sliderDataArrayList.add(R.drawable.ads_logo_1)
        sliderDataArrayList.add(R.drawable.ads_logo_2)
        sliderDataArrayList.add(R.drawable.ads_logo_3)
        sliderDataArrayList.add(R.drawable.ads_logo_4)
        sliderDataArrayList.add(R.drawable.ads_logo_5)

        val adapter = SliderAdapter(requireContext(), sliderDataArrayList)
        adsSlider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        adsSlider.setSliderAdapter(adapter)
        adsSlider.scrollTimeInSec = 3
        adsSlider.isAutoCycle = true
        adsSlider.startAutoCycle()
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
        var categoryDetails = CategoryFragment()
        requireActivity()?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.frame, categoryDetails).commit()
////        fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frame, brandDetails)?.commit()
        categoryDetails.setCategoryTitle(categoryTitleDetails)
    }

    override fun productDetailsShow(id: String) {
        // Open Product Details
        Log.i(TAG, "productDetailsShow: " + id)
    }
}