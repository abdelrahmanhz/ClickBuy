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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clickbuy.models.Product
import com.example.clickbuy.models.Products
private const val TAG = "CategoryFragment"
class CategoryFragment : Fragment(), SubCategoriesFromFilterInterface {
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var subcategoryAdapter: SubCateogriesAdapter
    private lateinit var brandFilterAdapter: BrandsFilterAdapter
    private lateinit var categoryFactory: CategoryViewModelFactory
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var filterBrandsRecyclerView: RecyclerView
    private lateinit var subCategoryRecyclerView: RecyclerView
    private lateinit var myToolbar: MaterialToolbar
    private lateinit var viewModel: CategoryViewModel
    private val ID_WOMEN = "273053712523"
    private val ID_MEN = "273053679755"
    private val ID_KIDZ = "273053745291"
    //private var default_id = ID_WOMEN
    private var default_id = ""

    private lateinit var tabLayout: TabLayout
    private lateinit var subCategoryData: ArrayList<Product>
    val catergories = mutableListOf("All","Woman", "Men", "Kidz")
    var productType: String = ""
    var vendor: String = ""
    var comingProductType: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_category, container, false)
        Log.i(TAG, "onCreateView: ")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "onViewCreated: ")
        categoryFactory = CategoryViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        initUI(view)
        myToolbar.inflateMenu(R.menu.appbar)
        setUpCategoryRecyclerView()
        myToolbar.setNavigationOnClickListener {
            var home = HomeFragment()
            requireActivity()?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame, home)
                .commit()
        }
        viewModel = ViewModelProvider(this, categoryFactory).get(CategoryViewModel::class.java)
        viewModel.subCategory.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "categoryProducts: $it")
                categoryAdapter.setListOfCategory(it.products)
                subCategoryData = it.products as ArrayList<Product>
            }
        }
        initTabLayout()
        setAllCategory()
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.view.setBackgroundColor(Color.BLACK)
                when (tab.position) {
                    0-> {
                        Log.i(TAG, "onTabSelected: women")
                        default_id = ""
                        setAllCategory()
                    }
                    1 -> {
                        Log.i(TAG, "onTabSelected: women")
                        default_id = ID_WOMEN
                        setWomenCategory()
                    }
                    2 -> {
                        default_id = ID_MEN
                        setMenCategory()
                    }
                    3 -> {
                        default_id = ID_KIDZ
                        setKidsCategory()
                    }
                }
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
                    Log.i(TAG, "onOptionsItemSelected: searchhhhhhhhhhhh")
                    true
                }
                R.id.favorite_menu -> {
                    true
                }
                R.id.filter_menubar -> {
                    val dialog = BottomSheetDialog(requireContext())
                    val viewLay = layoutInflater.inflate(R.layout.filter_popup, null)
                    var recycler: RecyclerView =
                        viewLay.findViewById(R.id.subCategoryFilterRecyclerViewPopUp)
                    var btnDone: Button = viewLay.findViewById(R.id.doneButton)
                    var priceSeeker: SeekBar = viewLay.findViewById(R.id.priceSlider)
                    val layoutManager = LinearLayoutManager(CategoryFragment().context)
                    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    subcategoryAdapter = SubCateogriesAdapter(requireContext(), this)
                    recycler.layoutManager = GridLayoutManager(requireContext(), 2)
                    recycler.adapter = subcategoryAdapter
                    var subCategory: HashSet<String> = HashSet()
                    viewModel.getAllCategoryProducts(default_id)
                    viewModel.category.observe(viewLifecycleOwner, Observer {
                        subcategoryAdapter.setListOfBrands(it)
                    })
                    priceSeeker?.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seek: SeekBar,
                            progress: Int, fromUser: Boolean
                        ) {
                        }

                        override fun onStartTrackingTouch(seek: SeekBar) {
                        }

                        override fun onStopTrackingTouch(seek: SeekBar) {
                            var arr: ArrayList<Product> = ArrayList()
                            for (i in 0..subCategoryData.size - 1) {
                                if (subCategoryData[i].variants?.get(0)!!.price.toDouble() <= seek.progress)
                                    arr.add(subCategoryData[i])
                                categoryAdapter.setListOfCategory(arr)
                            }
                            Log.i(TAG, "onStopTrackingTouch: arrrrr" + arr)
                            Toast.makeText(
                                requireContext(),
                                "Progress is: " + seek.progress,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                    btnDone.setOnClickListener {
                        viewModel.getAllProducts(ID_WOMEN, comingProductType, vendor)
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
    private fun setAllCategory() {
        Log.i(TAG, "setAllCategory: ")
        viewModel.getAllProducts("", productType, vendor)
    }
    private fun setWomenCategory() {
        Log.i(TAG, "setWomenCategory: ")
        viewModel.getAllProducts(ID_WOMEN, productType, vendor)
    }
    private fun setMenCategory() {
        viewModel.getAllProducts(ID_MEN, productType, vendor)
    }

    private fun setKidsCategory() {
        viewModel.getAllProducts(ID_KIDZ, productType, vendor)
    }
    private fun initUI(view: View) {
        categoryRecyclerView = view.findViewById(R.id.brandCategoryRecyclerView)
        tabLayout = view.findViewById(R.id.tabLayout)
        myToolbar = view.findViewById(R.id.toolBar)
    }

    private fun setUpCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter(requireContext())
        categoryRecyclerView.adapter = categoryAdapter
    }

    private fun setUpBrandFilterRecyclerView() {
        val layoutManager = LinearLayoutManager(CategoryFragment().context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        brandFilterAdapter = BrandsFilterAdapter(requireContext())
        //brandsDetailsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        filterBrandsRecyclerView.adapter = brandFilterAdapter
    }
    fun setCategoryTitle(categoryTitleDetails: String) {
        this.productType = categoryTitleDetails
        Log.i(TAG, "setCategoryName:  $categoryTitleDetails")
    }
    fun initTabLayout() {
        for (category in catergories) {
            tabLayout.addTab(tabLayout.newTab().setText(category))
        }
    }

    override fun showSubCategory(id: String, productTypeComing: String) {
        comingProductType = productType
        Log.i(TAG, "showSubCategory: comingggggggggggggggggg" + productTypeComing)
        viewModel.getAllProducts(default_id, vendor, productTypeComing)
    }
}