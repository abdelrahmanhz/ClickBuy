package com.example.clickbuy.currency.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.clickbuy.R
import com.example.clickbuy.currency.viewmodel.CurrencyViewModel
import com.example.clickbuy.currency.viewmodel.CurrencyViewModelFactory
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.mainscreen.view.MainActivity
import com.example.clickbuy.util.ConnectionLiveData
import com.example.clickbuy.util.isRTL
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar


private const val TAG = "CurrencyFragment"

class CurrencyFragment : Fragment() {

    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var enableConnection: AppCompatButton

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

        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                shimmerFrameLayout.visibility = View.VISIBLE
                shimmerFrameLayout.startShimmerAnimation()
                viewModel.getCurrencies()
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
            } else {
                currencyRecyclerView.visibility = View.GONE
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmerAnimation()
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
            }
        }

        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        enableConnection.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            } else {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
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
        enableConnection = view.findViewById(R.id.enable_connection)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
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