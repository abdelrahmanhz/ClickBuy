package com.example.clickbuy.home.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import com.example.clickbuy.payment.view.PaymentFragment
import com.example.clickbuy.productdetails.view.ProductDetailsFragment
import com.smarteist.autoimageslider.SliderView

private const val TAG = "HomeView"

class HomeFragment : Fragment(), CategoryBrandInterface, ProductDetailsInterface,
    BrandDetailsInterface {
    private lateinit var brandAdapter: BrandsAdapter
    private lateinit var saleAdapter: SalesAdapter
    private lateinit var homeFactory: HomeViewModelFactory
    private lateinit var brandsRecyclerView: RecyclerView
    private lateinit var salesRecyclerView: RecyclerView
    private lateinit var viewModel: HomeViewModel
    private lateinit var adsSlider: SliderView
    private lateinit var couponsSlider: SliderView
    private lateinit var couponsAdapter: CouponsSliderAdapter
    private lateinit var clipboardManager: ClipboardManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

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

        viewModel.getAvailableCoupons()
        viewModel.coupons.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                couponsAdapter.setList(it)
            }
        })
    }

    private fun initUI(view: View) {
        brandsRecyclerView = view.findViewById(R.id.brandsRecyclerView)

        val resId: Int = R.anim.lat
        val animation: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, resId)
        brandsRecyclerView.layoutAnimation = animation
        salesRecyclerView = view.findViewById(R.id.salesRecyclerView)

        adsSlider = view.findViewById(R.id.ads_sliderView)
        adsSlider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        adsSlider.setSliderAdapter(AdsSliderAdapter())
        adsSlider.scrollTimeInSec = 3
        adsSlider.isAutoCycle = true
        adsSlider.startAutoCycle()

        couponsSlider = view.findViewById(R.id.coupons_sliderView)
        couponsSlider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        couponsAdapter = CouponsSliderAdapter(requireContext(), this)
        couponsSlider.setSliderAdapter(couponsAdapter)
        couponsSlider.scrollTimeInSec = 3
        couponsSlider.isAutoCycle = true
        couponsSlider.startAutoCycle()
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

    override fun setBrandName(nameOfBrand: String) {
        Log.i(TAG, "setBrandName: ---------> $nameOfBrand")
        val categoryDetails = CategoryFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, categoryDetails).commit()
        //convert focus to category tab on bottom navigation
//        fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frame, brandDetails)?.commit()
        categoryDetails.setVendorName(nameOfBrand)

    }
    override fun productDetailsShow(id: String) {
        Log.i(TAG, "productDetailsShow: " + id)
        val salesDetails = ProductDetailsFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, salesDetails )
            .addToBackStack(null).commit()
        salesDetails.setProductId(id)

    }

    override fun brandDetailsShow(nameOfBrand: String) {
        val data = ClipData.newPlainText("text", nameOfBrand)
        clipboardManager.setPrimaryClip(data)
        Log.i(TAG, "brandDetailsShow: ----------> " + clipboardManager.primaryClip?.getItemAt(0)?.text.toString())
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, PaymentFragment())
            .addToBackStack(null).commit()
    }

}