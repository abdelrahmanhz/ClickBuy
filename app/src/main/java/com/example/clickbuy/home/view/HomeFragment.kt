package com.example.clickbuy.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.home.BrandsAdapter
import com.example.clickbuy.home.viewmodel.HomeViewModel
import com.example.clickbuy.home.viewmodel.HomeViewModelFactory
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient

private const val TAG = "HomeView"

class HomeFragment : Fragment(), BrandDetailsInterface {
    private lateinit var brandAdapter: BrandsAdapter
    private lateinit var homeFactory: HomeViewModelFactory
    private lateinit var brandsRecyclerView: RecyclerView
    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFactory = HomeViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        initUI(view)
        setUpBrandRecyclerView()
        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)
        viewModel.getAllBrands()
        viewModel.brand.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "brand: $it")
                brandAdapter.setListOfBrands(it.smart_collections)
            }
        }
    }

    private fun initUI(view: View) {
        brandsRecyclerView = view.findViewById(R.id.brandsRecyclerView)
    }

    private fun setUpBrandRecyclerView() {
        val layoutManager = LinearLayoutManager(HomeFragment().context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        brandAdapter = BrandsAdapter(requireContext(), this)
        brandsRecyclerView.layoutManager = layoutManager
        brandsRecyclerView.adapter = brandAdapter
    }


    override fun brandDetailsShow(nameOfBrand: String) {
//        Log.i(TAG, "brandDetailsShow: $nameOfBrand")
//        var brandDetails = BrandsDetailsFragment()
//        //   activity?.supportFragmentManager?.beginTransaction()?.addToBackStack(null)
//        //         ?.add(R.id.frame, brandDetails)?.commit()
//        fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frame, brandDetails)?.commit()
//        brandDetails.setBrandName(nameOfBrand)

    }
}