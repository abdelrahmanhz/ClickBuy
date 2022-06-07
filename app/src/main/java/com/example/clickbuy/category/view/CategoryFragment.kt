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
import de.hdodenhof.circleimageview.CircleImageView
import android.graphics.drawable.BitmapDrawable

import android.view.WindowManager

import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clickbuy.category.BrandsFilterAdapter
import com.example.clickbuy.category.SubCateogriesAdapter
import com.google.android.material.snackbar.Snackbar


private const val TAG = "CategoryFragment"

class CategoryFragment : Fragment(), FilterInterface {

    private var subCategoryTitleDetails: String = ""
    private var subCategoryId: String = ""
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var subcategoryAdapter: SubCateogriesAdapter
    private lateinit var currentView: View
    private lateinit var brandFilterAdapter: BrandsFilterAdapter
    private lateinit var categoryFactory: CategoryViewModelFactory
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var filterBrandsRecyclerView: RecyclerView
    private lateinit var brands: RecyclerView
    private lateinit var myToolbar: MaterialToolbar
    private lateinit var viewModel: CategoryViewModel
    private lateinit var womenImageView: CircleImageView
    private lateinit var menImageView: CircleImageView
    private lateinit var kidsImageView: CircleImageView
    private val ID_WOMEN = "273053712523"
    private val ID_MEN = "273053679755"
    private val ID_KIDZ = "273053745291"
    private lateinit var tabLayout: TabLayout

    // var categoryTitleComingFromHome: String = ""
    val catergories = mutableListOf("Woman", "Men", "Kidz")
    var productType: String = ""
    var vendor: String = ""
    var flag: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);

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
        //  setUpBrandFilterRecyclerView()
//        initMediator()

        myToolbar.setNavigationOnClickListener {
            var home = HomeFragment()
            requireActivity()?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame, home)
                .commit()
        }

        viewModel = ViewModelProvider(this, categoryFactory).get(CategoryViewModel::class.java)
        viewModel.subCategory.observe(requireActivity()) {
            if (!it?.products.isNullOrEmpty()) {
                Log.i(TAG, "categoryProducts: $it")
                categoryAdapter.setListOfCategory(it.products)
            } else {
                Log.i(TAG, "onViewCreated: no data ")
                showSnackBar()
                categoryAdapter.setListOfCategory(emptyList())
            }
        }

//        viewModel.subCategory.observe(requireActivity()) {
//            if (it != null) {
//                Log.i(TAG, "categoryProducts: $it")
//                subcategoryAdapter.setListOfBrands(it.products)
//            }
//        }


        initTabLayout()
        setWomenCategory()
        //  setSubWomenCategory()

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.view.setBackgroundColor(Color.GREEN)
                when (tab.position) {
                    0 -> {
                        Log.i(TAG, "onTabSelected: women")
                        setWomenCategory()
                        //    setSubWomenCategory()
                    }
                    1 -> {
                        setMenCategory()
                        //  setSubMenCategory()
                    }
                    2 -> {
                        setKidsCategory()
                        //setSubKidsCategory()
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
                    //showPopup(myToolbar)
                    // displayPopupWindow(view)
                    showFilterFragment()
                    Log.i(TAG, "onOptionsItemSelected: filterrrr")
                    true
                }
                else -> false
            }
        }

//        viewModel.checkFromHomeOrCategory(ID_WOMEN, categoryTitleComingFromHome)
//        womenImageView.borderWidth = 5
//
//        womenImageView.setOnClickListener {
//            viewModel.checkFromHomeOrCategory(ID_WOMEN, categoryTitleComingFromHome)
//            menImageView.borderWidth = 0
//            womenImageView.borderWidth = 5
//            kidsImageView.borderWidth = 0
//        }
//        menImageView.setOnClickListener {
//            viewModel.checkFromHomeOrCategory(ID_MEN, categoryTitleComingFromHome)
//            menImageView.borderWidth = 5
//            womenImageView.borderWidth = 0
//            kidsImageView.borderWidth = 0
//        }
//        kidsImageView.setOnClickListener {
//            viewModel.checkFromHomeOrCategory(ID_KIDZ, categoryTitleComingFromHome)
//            menImageView.borderWidth = 0
//            womenImageView.borderWidth = 0
//            kidsImageView.borderWidth = 5
//        }
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
        // Show anchored to button
        popup.setBackgroundDrawable(BitmapDrawable())
        popup.showAsDropDown(anchorView)
        setUpBrandFilterRecyclerView()
        viewModel.brand.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "categoryProducts: $it")
                brandFilterAdapter.setListOfBrands(it.smart_collections)
            }
        }
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.filter_menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            Log.i(TAG, "showPopup: pop upp")
            when (item!!.itemId) {
                R.id.price_menu -> {
                    Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                }
                R.id.bags_menu -> {
                    Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                }
                R.id.accessories_menu -> {
                    Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                }
            }

            true
        })

        popup.show()
    }

    override fun showFilterFragment() {
        //Log.i(TAG, "brandDetailsShow: $categoryTitleDetails")
        var filterFragment = FilterFragment()
        requireActivity()?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.frame, FilterFragment()).commit()
//        fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frame, brandDetails)?.commit()
        //  categoryDetails.setCategoryTitle(categoryTitleDetails)
        filterFragment.setCategoryTitleFromFilter(productType)

    }

    private fun setWomenCategory() {
        Log.i(TAG, "setWomenCategory: ")
        viewModel.getAllProducts(ID_WOMEN, productType, vendor)

    }

    private fun setSubWomenCategory() {
        //subCategoryTitleDetails
        //subCategoryId
        viewModel.getAllSubCategoriesFromFilter("273053679755", subCategoryTitleDetails)
        //  viewModel.checkFromHomeOrCategory("273053679755", "SHOES")

    }

    private fun setSubMenCategory() {
        viewModel.getAllSubCategoriesFromFilter("273053679755", subCategoryTitleDetails)

        // viewModel.checkFromHomeOrCategory("273053679755", "SHOES")

    }

    private fun setSubKidsCategory() {
        viewModel.getAllSubCategoriesFromFilter("273053679755", subCategoryTitleDetails)

//        viewModel.checkFromHomeOrCategory("273053679755", "SHOES")

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
        currentView = view
        //   filterBrandsRecyclerView = view.findViewById(R.id.brandsFilterRecyclerView)

//        womenImageView = view.findViewById(R.id.womenImage)
//        menImageView = view.findViewById(R.id.menImage)
//        kidsImageView = view.findViewById(R.id.kidsImage)
    }

    private fun setUpCategoryRecyclerView() {
        // val layoutManager = LinearLayoutManager(HomeFragment().context)
        //layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        categoryAdapter = CategoryAdapter(requireContext())
        //subcategoryAdapter = SubCateogriesAdapter(requireContext())
        //brandsDetailsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
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

    //////
    fun setSubCategoryTitleAndId(subCategoryId: String, subCategoryTitleDetails: String) {
        this.subCategoryTitleDetails = subCategoryTitleDetails
        this.subCategoryId = subCategoryId
        // viewModel.getAllSubCategoriesFromFilter(subCategoryId, subCategoryTitleDetails)
        productType = subCategoryTitleDetails
        Log.i(TAG, "setSUBCategoryName:  $subCategoryTitleDetails")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
    }


    fun initTabLayout() {

        for (category in catergories) {
            tabLayout.addTab(tabLayout.newTab().setText(category))
        }
    }

    companion object {

    }

    private fun showSnackBar() {
        Log.i(TAG, "showSnackBar: ")
        var snackBar = Snackbar.make(
            currentView.findViewById(R.id.category_fragment_ConstraintLayout),
            getString(R.string.no_data_error),
            Snackbar.LENGTH_SHORT
        ).setActionTextColor(Color.WHITE)

        snackBar.view.setBackgroundColor(Color.GREEN)
        snackBar.show()
    }

//    override fun showSubCategory(id: String, title: String) {
//        Log.i(TAG, "sub title: $id")
//        var category = CategoryFragment()
//        requireActivity()?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame, category)
//            .commit()
////        fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frame, brandDetails)?.commit()
//        //categoryDetails.setCategoryTitle(categoryTitleDetails)    }
//    }
}