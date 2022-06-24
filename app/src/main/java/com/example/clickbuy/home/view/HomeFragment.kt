package com.example.clickbuy.home.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ScrollView
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.clickbuy.R
import com.example.clickbuy.bag.view.BagFragment
import com.example.clickbuy.category.view.CategoryFragment
import com.example.clickbuy.favourites.view.FavouritesFragment
import com.example.clickbuy.home.viewmodel.HomeViewModel
import com.example.clickbuy.home.viewmodel.HomeViewModelFactory
import com.example.clickbuy.models.PriceRule
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.productdetails.view.ProductDetailsFragment
import com.example.clickbuy.util.ConnectionLiveData
import com.google.android.material.appbar.MaterialToolbar
import com.example.clickbuy.search.view.SearchFragment
import com.example.clickbuy.util.ConstantsValue
import com.example.clickbuy.util.connectInternet
import com.smarteist.autoimageslider.SliderView

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), CategoryBrandInterface, ProductDetailsInterface,
    CouponsDetailsInterface {

    private lateinit var myToolbar: MaterialToolbar

    private lateinit var enableConnection: AppCompatButton
    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var scrollView: ScrollView
    private lateinit var brandsRecyclerView: RecyclerView
    private lateinit var salesRecyclerView: RecyclerView
    private lateinit var brandAdapter: BrandsAdapter
    private lateinit var saleAdapter: SalesAdapter
    private lateinit var adsSlider: SliderView
    private lateinit var couponsSlider: SliderView
    private lateinit var homeFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel
    private lateinit var couponsAdapter: CouponsSliderAdapter
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var brandProgressBar: ProgressBar

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

        initUI(view)
        setUpBrandRecyclerView()
        setUpSaleRecyclerView()
        initViewModel()
        observeViewModel()

        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            Log.i(TAG, "onViewCreated: isInternetAvailable--------------> $it")
            if (it) {
                Log.i(TAG, "onViewCreated: in if")
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                myToolbar.visibility = View.VISIBLE
                viewModel.getAllBrands()
                viewModel.getAllSalesById()
                viewModel.getAllPriceRules()
                //     viewModel.getAvailableCoupons()
            } else {
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
                scrollView.visibility = View.GONE
                myToolbar.visibility = View.GONE
            }
        }

        enableConnection.setOnClickListener {
            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                 startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
             } else {
                 startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
             }*/
            connectInternet(requireContext())
        }


        myToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite_menubar_home -> {
                    if (ConstantsValue.isLogged) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.frame, FavouritesFragment())
                            .addToBackStack(null).commit()
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.unauthorized_wishlist),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                R.id.cart_menubar_home -> {
                    if (ConstantsValue.isLogged) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.frame, BagFragment())
                            .addToBackStack(null).commit()
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.unauthorized_shopping_cart),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                R.id.search_menubar_home -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, SearchFragment())
                        .addToBackStack(null).commit()
                }
            }
            true

        }
    }

    private fun initUI(view: View) {

        scrollView = view.findViewById(R.id.scroll_view)
        myToolbar = view.findViewById(R.id.toolBarHome)

        val resId: Int = R.anim.lat
        val animation: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, resId)

        brandProgressBar = view.findViewById(R.id.progress_bar_brand)
        brandsRecyclerView = view.findViewById(R.id.brandsRecyclerView)
        brandsRecyclerView.layoutAnimation = animation
        salesRecyclerView = view.findViewById(R.id.salesRecyclerView)

        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
        enableConnection = view.findViewById(R.id.enable_connection)


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
        couponsSlider.scrollTimeInSec = 5
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

    private fun initViewModel() {
        homeFactory = HomeViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.brand.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "brand: $it")
                brandAdapter.setListOfBrands(it.smart_collections)
            }
            brandProgressBar.visibility = View.GONE

        }

        viewModel.saleId.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "sale: $it")
                saleAdapter.setListOfSales(it.products)
            }
        }

        /*  viewModel.coupons.observe(viewLifecycleOwner) {

              if (!it.isNullOrEmpty()) {
                  couponsAdapter.setList(it)
              }
          }*/

        viewModel.priceRules.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                couponsAdapter.setList(it)
            }
        }
    }

    override fun setBrandName(nameOfBrand: String) {

        val categoryDetails = CategoryFragment()
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.frame, categoryDetails).commit()
        categoryDetails.setVendorName(nameOfBrand)

    }

    override fun productDetailsShow(id: String) {
        Log.i(TAG, "productDetailsShow: $id")
        val salesDetails = ProductDetailsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, salesDetails)
            .addToBackStack(null).commit()
        salesDetails.setProductId(id)

    }

/*    override fun copyCouponsDetails(couponCode: String) {
        val data = ClipData.newPlainText("coupon", couponCode)
        clipboardManager.setPrimaryClip(data)
    }*/

    override fun copyCouponsDetails(priceRule: PriceRule) {
        val data = ClipData.newPlainText("coupon", priceRule.title)
        clipboardManager.setPrimaryClip(data)
        ConstantsValue.discountAmount = priceRule.value
        Toast.makeText(requireContext(), getString(R.string.copy_success), Toast.LENGTH_SHORT)
            .show()
    }
}