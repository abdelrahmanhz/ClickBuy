package com.example.clickbuy.category.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import com.example.clickbuy.R
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.category.CategoryAdapter
import com.example.clickbuy.category.viewmodel.CategoryViewModel
import com.example.clickbuy.category.viewmodel.CategoryViewModelFactory
import com.example.clickbuy.home.view.HomeFragment
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import android.graphics.drawable.BitmapDrawable

import android.view.WindowManager

import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clickbuy.category.BrandsFilterAdapter
import com.example.clickbuy.category.SubCategoriesFromFilterInterface
import com.example.clickbuy.category.SubCateogriesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clickbuy.models.Product

private const val TAG = "CategoryFragment"

class CategoryFragment : Fragment(), SubCategoriesFromFilterInterface {
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

    //private var default_id = ID_WOMEN
    private var defaultId = ""

    private lateinit var tabLayout: TabLayout
    private lateinit var subCategoryData: ArrayList<Product>
    private val categories = mutableListOf("All", "Woman", "Men", "Kids")
    private var productType: String = ""
    private var vendor: String = ""
    // var comingProductType: String = ""

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

        myToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, HomeFragment())
                .commit()
        }

        viewModel.subCategory.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "categoryProducts: $it")
                categoryAdapter.setListOfCategory(it.products)
                subCategoryData = it.products as ArrayList<Product>
            }
        }

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.view.setBackgroundColor(Color.BLACK)
                when (tab.position) {
                    0 -> {
                        Log.i(TAG, "onTabSelected: women")
                        defaultId = ""
                        //   getAllProducts()
                    }
                    1 -> {
                        Log.i(TAG, "onTabSelected: women")
                        defaultId = ID_WOMEN
                        //  getAllProducts()
                    }
                    2 -> {
                        defaultId = ID_MEN
                        // getAllProducts()
                    }
                    3 -> {
                        defaultId = ID_KIDS
                        // getAllProducts()
                    }
                }
                getAllProducts()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.view.setBackgroundColor(Color.WHITE)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        myToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search_menu -> {
                    Log.i(TAG, "onOptionsItemSelected: search")
                }
                R.id.favorite_menu -> {
                }
                R.id.filter_menubar -> {
                    val dialog = BottomSheetDialog(requireContext())
                    val viewLay = layoutInflater.inflate(R.layout.filter_popup, null)
//                    viewLay.setBackgroundResource(R.drawable.background_filter)
                    val recycler: RecyclerView =
                        viewLay.findViewById(R.id.subCategoryFilterRecyclerViewPopUp)
                    val btnDone: Button = viewLay.findViewById(R.id.doneButton)
                    val priceSeeker: SeekBar = viewLay.findViewById(R.id.priceSlider)
                    val layoutManager = LinearLayoutManager(CategoryFragment().context)
                    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    subcategoryAdapter = SubCateogriesAdapter(requireContext(), this)
                    recycler.layoutManager = GridLayoutManager(requireContext(), 2)
                    recycler.adapter = subcategoryAdapter
                    viewModel.getAllCategoryProducts(defaultId)
                    viewModel.category.observe(viewLifecycleOwner, {
                        subcategoryAdapter.setListOfBrands(it)
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
                            var priceFiltered: ArrayList<Product> = ArrayList()
                            for (i in 0 until subCategoryData.size) {
                                if (subCategoryData[i].variants?.get(0)!!.price.toDouble() <= seek.progress)
                                    priceFiltered.add(subCategoryData[i])
                                categoryAdapter.setListOfCategory(priceFiltered)
                            }
                            Toast.makeText(requireContext(), "Progress is: " + seek.progress, Toast.LENGTH_SHORT).show()
                        }
                    })
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
        myToolbar.inflateMenu(R.menu.appbar)

        categoryAdapter = CategoryAdapter(requireContext())
        categoryRecyclerView.adapter = categoryAdapter
    }

    private fun initTabLayout() {
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

    private fun displayPopupWindow(anchorView: View) {
        val popup = PopupWindow(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.filter_popup, null)
        filterBrandsRecyclerView = anchorView.findViewById(R.id.brandsFilterRecyclerView)

        popup.contentView = layout
        popup.height = WindowManager.LayoutParams.MATCH_PARENT
        popup.width = WindowManager.LayoutParams.MATCH_PARENT
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        popup.setBackgroundDrawable(BitmapDrawable())
        popup.showAsDropDown(anchorView)
        viewModel.brand.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "categoryProducts: $it")
                brandFilterAdapter.setListOfBrands(it.smart_collections)
            }
        }
    }

    private fun setUpBrandFilterRecyclerView() {
        val layoutManager = LinearLayoutManager(CategoryFragment().context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        brandFilterAdapter = BrandsFilterAdapter(requireContext())
        //brandsDetailsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        filterBrandsRecyclerView.adapter = brandFilterAdapter
    }
}