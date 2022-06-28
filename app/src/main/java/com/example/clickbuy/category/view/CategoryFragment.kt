package com.example.clickbuy.category.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import com.example.clickbuy.R
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.category.viewmodel.CategoryViewModel
import com.example.clickbuy.category.viewmodel.CategoryViewModelFactory
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.clickbuy.bag.view.BagFragment
import com.example.clickbuy.category.viewmodel.ProductDetailsIDShow
import com.example.clickbuy.favourites.view.FavouritesFragment
import com.example.clickbuy.models.Product
import com.example.clickbuy.productdetails.view.ProductDetailsFragment
import com.example.clickbuy.util.ConnectionLiveData
import com.facebook.shimmer.ShimmerFrameLayout
import com.example.clickbuy.util.ConstantsValue
import com.example.clickbuy.util.calculatePrice
import com.example.clickbuy.util.getEquivalentCurrencyValue
import java.text.DecimalFormat

private const val ID_WOMEN = "274500059275"
private const val ID_MEN = "274500026507"
private const val ID_KIDS = "274500092043"

class CategoryFragment : Fragment(), SubCategoriesFromFilterInterface, ProductDetailsIDShow {

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var subcategoryAdapter: SubCategoriesAdapter
    private lateinit var categoryFactory: CategoryViewModelFactory
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var myToolbar: MaterialToolbar
    private lateinit var categorySearchView: SearchView
    private lateinit var viewModel: CategoryViewModel
    private var defaultId = ""
    private lateinit var tabLayout: TabLayout
    private lateinit var subCategoryData: ArrayList<Product>
    private var productType: String = ""
    private var vendor: String = ""
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var enableConnection: TextView
    private lateinit var noProductsImageView: LottieAnimationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                shimmerFrameLayout.visibility = View.VISIBLE
                shimmerFrameLayout.startShimmerAnimation()
                getAllProducts()
                tabLayout.visibility = View.VISIBLE
                categorySearchView.visibility = View.VISIBLE
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
                myToolbar.visibility = View.VISIBLE
            } else {
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmerAnimation()
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
                tabLayout.visibility = View.GONE
                noProductsImageView.visibility = View.GONE
                myToolbar.visibility = View.GONE
                categorySearchView.visibility = View.GONE
                categoryRecyclerView.visibility = View.GONE
            }
        }

        initViewModel()
        initUI(view)
        initTabLayout()
        observeViewModel()

        enableConnection.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            } else {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
        }

        if (vendor.isNotEmpty())
            myToolbar.setNavigationIcon(R.drawable.ic_back_icon)
        myToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }



        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        defaultId = ""
                    }
                    1 -> {
                        defaultId = ID_WOMEN
                    }
                    2 -> {
                        defaultId = ID_MEN
                    }
                    3 -> {
                        defaultId = ID_KIDS
                    }
                }
                getAllProducts()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                clearSearchViewText()
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        myToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite_menu -> {
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
                R.id.cart_menubar -> {
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
                R.id.filter_menubar -> {
                    val dialog = BottomSheetDialog(requireContext())
                    val viewLay = layoutInflater.inflate(R.layout.filter_popup, null)
                    val recycler: RecyclerView =
                        viewLay.findViewById(R.id.subCategoryFilterRecyclerViewPopUp)
                    val btnDone: Button = viewLay.findViewById(R.id.doneButton)
                    val btnReset: ImageView = viewLay.findViewById(R.id.btnResetFilter)
                    val priceSeeker: SeekBar = viewLay.findViewById(R.id.priceSlider)
                    val filteredPrice: TextView = viewLay.findViewById(R.id.filteredPrice)
                    val layoutManager = LinearLayoutManager(CategoryFragment().context)
                    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    subcategoryAdapter = SubCategoriesAdapter(requireContext(), this)
                    recycler.layoutManager = GridLayoutManager(requireContext(), 3)
                    recycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    recycler.adapter = subcategoryAdapter
                    viewModel.getAllCategoryProducts(defaultId)
                    viewModel.category.observe(viewLifecycleOwner) {
                        subcategoryAdapter.setListOfSubCategories(it)
                    }
                    priceSeeker.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seek: SeekBar,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                        }

                        override fun onStartTrackingTouch(seek: SeekBar) {
                        }

                        override fun onStopTrackingTouch(seek: SeekBar) {
                            val priceFiltered: ArrayList<Product> = ArrayList()
                            for (i in 0 until subCategoryData.size) {
                                if (subCategoryData[i].variants?.get(0)!!.price.toDouble() <= seek.progress)
                                    priceFiltered.add(subCategoryData[i])
                                categoryAdapter.setListOfCategory(priceFiltered)
                            }

                            val progressText = seek.progress * ConstantsValue.currencyValue
                            filteredPrice.text = DecimalFormat("#").format(progressText)
                        }
                    })
                    btnReset.setOnClickListener {
                        productType = ""
                        getAllProducts()
                        priceSeeker.progress = 0
                        filteredPrice.text = ""
                        subcategoryAdapter.reset()
                    }
                    btnDone.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.behavior.isFitToContents
                    dialog.setContentView(viewLay)
                    dialog.show()
                }
            }
            true
        }

        categorySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            var tempSubCategoryData = ArrayList<Product>()
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0.isNullOrEmpty()) {
                    tempSubCategoryData.clear()
                    subCategoryData.forEach {
                        if (it.title?.contains(p0, true)!!)
                            tempSubCategoryData.add(it)
                    }
                    categoryAdapter.setListOfCategory(tempSubCategoryData as List<Product>)

                    categoryRecyclerView.adapter?.notifyDataSetChanged()
                } else {
                    tempSubCategoryData.clear()
                    categoryAdapter.setListOfCategory(subCategoryData)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (!p0.isNullOrEmpty()) {
                    tempSubCategoryData.clear()
                    subCategoryData.forEach {
                        if (it.title?.contains(p0, true)!!)
                            tempSubCategoryData.add(it)
                    }
                    categoryAdapter.setListOfCategory(tempSubCategoryData as List<Product>)

                    categoryRecyclerView.adapter?.notifyDataSetChanged()
                } else {
                    tempSubCategoryData.clear()
                    categoryAdapter.setListOfCategory(subCategoryData)
                }
                return false
            }
        })
    }

    private fun observeViewModel() {
        Log.i("TAG", "observeViewModel: observe data")
        viewModel.subCategory.observe(viewLifecycleOwner) {
            if (it != null && it.products.isNotEmpty()) {
                categoryRecyclerView.visibility = View.VISIBLE
                noProductsImageView.visibility = View.GONE
                categoryAdapter.setListOfCategory(it.products)
                subCategoryData = it.products as ArrayList<Product>
                Log.i("TAG", "observeViewModel: exist data")
            } else {
                noProductsImageView.visibility = View.VISIBLE
                categoryRecyclerView.visibility = View.GONE
                categoryAdapter.setListOfCategory(emptyList())
                subCategoryData = ArrayList()
                Log.i("TAG", "observeViewModel: no data")
            }
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }
    }

    private fun clearSearchViewText() {
        if (categorySearchView.query.isNotEmpty()) {
            categorySearchView.setQuery("", false)
            categorySearchView.clearFocus()
            categorySearchView.isIconified = false
        }

    }

    private fun initViewModel() {
        categoryFactory = CategoryViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        viewModel = ViewModelProvider(this, categoryFactory).get(CategoryViewModel::class.java)
    }

    private fun initUI(view: View) {
        categoryRecyclerView = view.findViewById(R.id.brandCategoryRecyclerView)
        tabLayout = view.findViewById(R.id.tabLayout)
        categorySearchView = view.findViewById(R.id.categorySearchView)
        myToolbar = view.findViewById(R.id.toolBar)
        categoryAdapter = CategoryAdapter(requireContext(), this)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_frame_layout_category)
        enableConnection = view.findViewById(R.id.enable_connection_category)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation_category)
        noProductsImageView = view.findViewById(R.id.no_products_imageView)
        categoryRecyclerView.adapter = categoryAdapter
    }

    private fun initTabLayout() {
        val categories = mutableListOf(
            getString(R.string.all),
            getString(R.string.women),
            getString(R.string.men),
            getString(R.string.kids)
        )
        for (category in categories) {
            tabLayout.addTab(tabLayout.newTab().setText(category))
        }
    }

    private fun getAllProducts() {
        viewModel.getAllProducts(defaultId, vendor, productType)
    }


    fun setVendorName(brandName: String) {
        vendor = brandName
    }

    override fun setSubCategoryTitle(productType: String) {
        this.productType = productType
        viewModel.getAllProducts(defaultId, vendor, productType)

    }

    override fun setProductDetailsID(id: String) {
        val salesDetails = ProductDetailsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, salesDetails)
            .addToBackStack(null).commit()
        salesDetails.setProductId(id)
    }
}