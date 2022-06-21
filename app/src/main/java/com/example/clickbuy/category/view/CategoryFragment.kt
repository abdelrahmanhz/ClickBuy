package com.example.clickbuy.category.view

import android.os.Bundle
import android.util.Log
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
import android.graphics.drawable.BitmapDrawable

import android.view.WindowManager

import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clickbuy.category.viewmodel.ProductDetailsIDShow
import com.example.clickbuy.favourites.view.FavouritesFragment
import com.example.clickbuy.models.Product
import com.example.clickbuy.productdetails.view.ProductDetailsFragment
import com.facebook.shimmer.ShimmerFrameLayout

private const val TAG = "CategoryFragment"

class CategoryFragment : Fragment(), SubCategoriesFromFilterInterface, ProductDetailsIDShow {
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var subcategoryAdapter: SubCateogriesAdapter
    private lateinit var brandFilterAdapter: BrandsFilterAdapter
    private lateinit var categoryFactory: CategoryViewModelFactory
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var filterBrandsRecyclerView: RecyclerView
    private lateinit var myToolbar: MaterialToolbar
    private lateinit var viewModel: CategoryViewModel
    private val ID_WOMEN = "273053712523"
    private val ID_MEN = "273053679755"
    private val ID_KIDS = "273053745291"
    private var defaultId = ""
    private lateinit var tabLayout: TabLayout
    private lateinit var subCategoryData: ArrayList<Product>
    private var productType: String = ""
    private var vendor: String = ""
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout


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
        Log.i(TAG, "onViewCreated: ")

        initViewModel()
        initUI(view)
        initTabLayout()
        getAllProducts()

        if (vendor.isNotEmpty())
            myToolbar.setNavigationIcon(R.drawable.ic_back_icon)
        myToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        viewModel.subCategory.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "categoryProducts: $it")
                categoryAdapter.setListOfCategory(it.products)
                subCategoryData = it.products as ArrayList<Product>
            }
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        Log.i(TAG, "onTabSelected: women")
                        defaultId = ""
                    }
                    1 -> {
                        Log.i(TAG, "onTabSelected: women")
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
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        myToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite_menu -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouritesFragment())
                        .addToBackStack(null).commit()
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
                    subcategoryAdapter = SubCateogriesAdapter(requireContext(), this)
                    recycler.layoutManager = GridLayoutManager(requireContext(), 3)
                    recycler.adapter = subcategoryAdapter
                    viewModel.getAllCategoryProducts(defaultId)
                    viewModel.category.observe(viewLifecycleOwner, {
                        subcategoryAdapter.setListOfSubCategories(it)
                    })

                    priceSeeker.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seek: SeekBar,
                            progress: Int, fromUser: Boolean
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
                            filteredPrice.text = seek.progress.toString()
                        }
                    })
                    btnReset.setOnClickListener {
                        productType = ""
                        getAllProducts()
                        priceSeeker.progress = 0
                        filteredPrice.text = ""

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
        myToolbar = view.findViewById(R.id.toolBar)
        categoryAdapter = CategoryAdapter(requireContext(), this)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_frame_layout_category)

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
        Log.i(TAG, "getAllProducts: ")
        viewModel.getAllProducts(defaultId, vendor, productType)
    }


    fun setVendorName(brandName: String) {
        vendor = brandName
        Log.i(TAG, "setVendorName: -------> $brandName")
    }


    override fun setSubCategoryTitle(productType: String) {
        Log.i(TAG, "showSubCategory: productType $productType")
        this.productType = productType
        viewModel.getAllProducts(defaultId, vendor, productType)

    }

    override fun SetProductDetailsID(id: String) {
        Log.i(TAG, "productDetailsShow: " + id)
        val salesDetails = ProductDetailsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, salesDetails)
            .addToBackStack(null).commit()
        salesDetails.setProductId(id)

    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmerAnimation()
    }
}