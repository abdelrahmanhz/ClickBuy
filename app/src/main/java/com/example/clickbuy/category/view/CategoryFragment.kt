package com.example.clickbuy.category.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.clickbuy.R
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.category.CategoryAdapter
import com.example.clickbuy.category.viewmodel.CategoryViewModel
import com.example.clickbuy.category.viewmodel.CategoryViewModelFactory
import com.example.clickbuy.db.ConcreteLocalSource
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import de.hdodenhof.circleimageview.CircleImageView

private const val TAG = "CategoryFragment"

class CategoryFragment : Fragment() {
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryFactory: CategoryViewModelFactory
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var viewModel: CategoryViewModel
    private lateinit var womenImageView: CircleImageView
    private lateinit var menImageView: CircleImageView
    private lateinit var kidsImageView: CircleImageView
    private val ID_WOMEN = "273053712523"
    private val ID_MEN = "273053679755"
    private val ID_KIDZ = "273053745291"
    var categoryTitleComingFromHome: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_category, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryFactory = CategoryViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                ConcreteLocalSource(requireContext()),
                requireContext()
            )
        )
        initUI(view)
        setUpCategoryRecyclerView()
        viewModel = ViewModelProvider(this, categoryFactory).get(CategoryViewModel::class.java)
        viewModel.brand.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "categoryProducts: $it")
                categoryAdapter.setListOfCategory(it.products)
            }
        }
        viewModel.checkFromHomeOrCategory(ID_WOMEN, categoryTitleComingFromHome)
        womenImageView.borderWidth = 5

        womenImageView.setOnClickListener {
            viewModel.checkFromHomeOrCategory(ID_WOMEN, categoryTitleComingFromHome)
            menImageView.borderWidth = 0
            womenImageView.borderWidth = 5
            kidsImageView.borderWidth = 0
        }
        menImageView.setOnClickListener {
            viewModel.checkFromHomeOrCategory(ID_MEN, categoryTitleComingFromHome)
            menImageView.borderWidth = 5
            womenImageView.borderWidth = 0
            kidsImageView.borderWidth = 0
        }
        kidsImageView.setOnClickListener {
            viewModel.checkFromHomeOrCategory(ID_KIDZ, categoryTitleComingFromHome)
            menImageView.borderWidth = 0
            womenImageView.borderWidth = 0
            kidsImageView.borderWidth = 5
        }
    }

    private fun initUI(view: View) {
        categoryRecyclerView = view.findViewById(R.id.brandCategoryRecyclerView)
        womenImageView = view.findViewById(R.id.womenImage)
        menImageView = view.findViewById(R.id.menImage)
        kidsImageView = view.findViewById(R.id.kidsImage)
    }

    private fun setUpCategoryRecyclerView() {
        // val layoutManager = LinearLayoutManager(HomeFragment().context)
        //layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        categoryAdapter = CategoryAdapter(requireContext())
        //brandsDetailsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        categoryRecyclerView.adapter = categoryAdapter
    }

    fun setCategoryTitle(categoryTitleDetails: String) {
        this.categoryTitleComingFromHome = categoryTitleDetails
        Log.i(TAG, "setCategoryName:  $categoryTitleDetails")
    }

    companion object {

    }
}