package com.example.clickbuy.search.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clickbuy.R
import com.example.clickbuy.category.view.CategoryAdapter
import com.example.clickbuy.category.viewmodel.ProductDetailsIDShow
import com.example.clickbuy.databinding.FragmentSearchBinding
import com.example.clickbuy.models.Product
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.productdetails.view.ProductDetailsFragment
import com.example.clickbuy.search.viewModel.SearchViewModel
import com.example.clickbuy.search.viewModel.SearchViewModelFactory
import com.example.clickbuy.util.ConnectionLiveData
import com.example.clickbuy.util.isRTL

private const val TAG = "SearchFragment"

class SearchFragment : Fragment(), ProductDetailsIDShow {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchResultAdapter: CategoryAdapter
    private lateinit var searchFactory: SearchViewModelFactory
    private lateinit var viewModel: SearchViewModel
    var products = ArrayList<Product>()
    var tempProducts = ArrayList<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        checkInternetConnection()
        initUI()
        getAllProducts()
    }

    private fun checkInternetConnection() {
        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                binding.searchNoInternetAnimation.visibility = View.GONE
                binding.searchEnableConnection.visibility = View.GONE
                if(tempProducts.isEmpty()) binding.searchEmptyAnimation.visibility = View.VISIBLE
                binding.searchSV.visibility = View.VISIBLE
                binding.searchResultRV.visibility = View.VISIBLE
                viewModel.getAllProducts()
            } else {
                binding.searchNoInternetAnimation.visibility = View.VISIBLE
                binding.searchEnableConnection.visibility = View.VISIBLE
                binding.searchEmptyAnimation.visibility = View.GONE
                binding.searchSV.visibility = View.GONE
                binding.searchResultRV.visibility = View.GONE
            }
        }

        binding.searchEnableConnection.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            } else {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
        }
    }

    private fun getAllProducts() {
        viewModel.products.observe(requireActivity()){
            products = it.products as ArrayList<Product>
        }
    }

    private fun initViewModel() {
        searchFactory = SearchViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        viewModel = ViewModelProvider(this, searchFactory).get(SearchViewModel::class.java)
    }

    private fun initUI() {

        binding.searchResultRV.visibility = View.GONE
        binding.searchEmptyAnimation.visibility = View.VISIBLE
        setupToolBar()
        setupSearchView()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.searchResultRV.layoutManager = GridLayoutManager(context, 2)
        searchResultAdapter = CategoryAdapter(requireContext(), this)
        binding.searchResultRV.adapter = searchResultAdapter
    }

    private fun setupSearchView() {
        binding.searchSV.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.searchResultRV.visibility = View.VISIBLE
                if(!p0.isNullOrEmpty()){
                    tempProducts.clear()
                    products.forEach {
                        if (it.title?.contains(p0, true)!! || it.vendor?.contains(p0, true)!! )
                            tempProducts.add(it)
                    }
                    Log.i(TAG, "tempProducts count ${tempProducts.count()}")
                    if (tempProducts.isEmpty()){
                        binding.searchResultRV.visibility = View.GONE
                        binding.searchEmptyAnimation.visibility = View.VISIBLE
                    }
                    else{
                        binding.searchResultRV.visibility = View.VISIBLE
                        binding.searchEmptyAnimation.visibility = View.GONE
                        searchResultAdapter.setListOfCategory(tempProducts as List<Product>)
                        binding.searchResultRV.adapter?.notifyDataSetChanged()
                    }
                }
                else{
                    tempProducts.clear()
                    searchResultAdapter.setListOfCategory(tempProducts)
                    binding.searchResultRV.visibility = View.GONE
                    binding.searchEmptyAnimation.visibility = View.VISIBLE
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                binding.searchResultRV.visibility = View.VISIBLE
                if(!p0.isNullOrEmpty()){
                    tempProducts.clear()
                    products.forEach {
                        if (it.title?.contains(p0, true)!! || it.vendor?.contains(p0, true)!! )
                            tempProducts.add(it)
                    }
                    Log.i(TAG, "tempProducts count ${tempProducts.count()}")
                    if (tempProducts.isEmpty()){
                        binding.searchResultRV.visibility = View.GONE
                        binding.searchEmptyAnimation.visibility = View.VISIBLE
                    }
                    else{
                        binding.searchResultRV.visibility = View.VISIBLE
                        binding.searchEmptyAnimation.visibility = View.GONE
                        searchResultAdapter.setListOfCategory(tempProducts as List<Product>)
                        binding.searchResultRV.adapter?.notifyDataSetChanged()
                    }
                }
                else{
                    tempProducts.clear()
                    searchResultAdapter.setListOfCategory(tempProducts)
                    binding.searchResultRV.visibility = View.GONE
                    binding.searchEmptyAnimation.visibility = View.VISIBLE
                }
                return false
            }
        })
    }
    private fun setupToolBar() {
        binding.searchTB.setNavigationIcon(if (isRTL()) R.drawable.ic_back_icon_rtl else R.drawable.ic_back_icon)
        binding.searchTB.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }
    override fun SetProductDetailsID(id: String) {
        val productDetails = ProductDetailsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, productDetails)
            .addToBackStack(null).commit()
        productDetails.setProductId(id)
    }
}