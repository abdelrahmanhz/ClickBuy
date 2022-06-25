package com.example.clickbuy.search.view

import android.os.Bundle
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

private const val TAG = "SearchFragment"

class SearchFragment : Fragment(), ProductDetailsIDShow {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchResultAdapter: CategoryAdapter
    private lateinit var searchFactory: SearchViewModelFactory
    private lateinit var viewModel: SearchViewModel
    var products = ArrayList<Product>()

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
        initUI()
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModel.getAllProducts()
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
        binding.searchEmptyImageView.visibility = View.VISIBLE
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
            var tempProducts = ArrayList<Product>()
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
                        binding.searchEmptyImageView.visibility = View.VISIBLE
                    }
                    else{
                        binding.searchResultRV.visibility = View.VISIBLE
                        binding.searchEmptyImageView.visibility = View.GONE
                        searchResultAdapter.setListOfCategory(tempProducts as List<Product>)
                        binding.searchResultRV.adapter?.notifyDataSetChanged()
                    }
                }
                else{
                    tempProducts.clear()
                    searchResultAdapter.setListOfCategory(tempProducts)
                    binding.searchResultRV.visibility = View.GONE
                    binding.searchEmptyImageView.visibility = View.VISIBLE
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
                        binding.searchEmptyImageView.visibility = View.VISIBLE
                    }
                    else{
                        binding.searchResultRV.visibility = View.VISIBLE
                        binding.searchEmptyImageView.visibility = View.GONE
                        searchResultAdapter.setListOfCategory(tempProducts as List<Product>)
                        binding.searchResultRV.adapter?.notifyDataSetChanged()
                    }
                }
                else{
                    tempProducts.clear()
                    searchResultAdapter.setListOfCategory(tempProducts)
                    binding.searchResultRV.visibility = View.GONE
                    binding.searchEmptyImageView.visibility = View.VISIBLE
                }
                return false
            }
        })
    }
    private fun setupToolBar() {
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