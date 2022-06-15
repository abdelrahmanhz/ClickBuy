package com.example.clickbuy.currency.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.currency.viewmodel.CurrencyViewModel
import com.example.clickbuy.currency.viewmodel.CurrencyViewModelFactory
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.mainscreen.view.MainActivity
import com.example.clickbuy.util.isRTL
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar



private const val TAG = "CurrencyFragment"

class CurrencyFragment : Fragment() {
    private lateinit var arrowBackImageView: ImageView
    private lateinit var currencyRecyclerView: RecyclerView
    private lateinit var currencyAdapter: CurrencyAdapter
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var viewModelFactory: CurrencyViewModelFactory
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var currentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView: ")
        val view = inflater.inflate(R.layout.fragment_currency, container, false)
        currentView = view

        initView(view)
        initViewModel()
        checkRTL()
        observeViewModel()

        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


        return view
    }

    private fun initView(view: View) {
        shimmerFrameLayout = view.findViewById(R.id.shimmer_frame_layout)
        currencyRecyclerView = view.findViewById(R.id.recyclerView_currency)
        currencyAdapter = CurrencyAdapter()
        currencyRecyclerView.layoutManager = LinearLayoutManager(view.context)
        currencyRecyclerView.adapter = currencyAdapter

        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)
    }

    private fun initViewModel() {
        viewModelFactory = CurrencyViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CurrencyViewModel::class.java)

        viewModel.getCurrencies()
    }

    private fun checkRTL() {
        if (isRTL())
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_right)
    }

    private fun observeViewModel() {
        viewModel.currencies.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                showSnackBar()
            } else {
                currencyAdapter.setList(it)
                shimmerFrameLayout.stopShimmerAnimation()
                shimmerFrameLayout.visibility = View.GONE
                currencyRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmerAnimation()
    }

    private fun showSnackBar() {
        val snackBar = Snackbar.make(
            currentView.findViewById(R.id.ConstraintLayout_CurrencyFragment),
            getString(R.string.no_currency_found),
            Snackbar.LENGTH_SHORT
        ).setActionTextColor(Color.WHITE)

        snackBar.view.setBackgroundColor(Color.RED)
        snackBar.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
        (requireActivity() as MainActivity).updateCurrency()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach: ")
    }


}