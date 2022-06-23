package com.example.clickbuy.address.showaddresses.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.clickbuy.R
import com.example.clickbuy.address.showaddresses.viewmodel.AddressViewModel
import com.example.clickbuy.address.showaddresses.viewmodel.AddressViewModelFactory
import com.example.clickbuy.address.addaddresses.view.AddAddressFragment
import com.example.clickbuy.models.CustomerAddress
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.util.ConnectionLiveData
import com.example.clickbuy.util.isRTL
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton


private const val TAG = "AddressFragment"

class AddressFragment : Fragment() {

    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var enableConnection: AppCompatButton

    private lateinit var arrowBackImageView: ImageView
    private lateinit var addressRecyclerView: RecyclerView
    private lateinit var addAddressButton: FloatingActionButton
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var addAddress: AppCompatButton
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var viewModelFactory: AddressViewModelFactory
    private lateinit var viewModel: AddressViewModel
    private var addressList = arrayListOf<CustomerAddress>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_address, container, false)

        initView(view)
        checkRTL()
        initViewModel()
        observeViewModel()

        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                shimmerFrameLayout.visibility = View.VISIBLE
                shimmerFrameLayout.startShimmerAnimation()
                viewModel.getAllAddresses()
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
                addAddressButton.visibility = View.VISIBLE
            } else {
                addressRecyclerView.visibility = View.GONE
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmerAnimation()
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
                addAddressButton.visibility = View.GONE
            }
        }

        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        addAddressButton.setOnClickListener {
            replaceFragment(AddAddressFragment())
        }

        addAddress.setOnClickListener {
            replaceFragment(AddAddressFragment())
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
        addressRecyclerView = view.findViewById(R.id.recyclerView_address)
        addAddressButton = view.findViewById(R.id.add_address_floatingActionButton)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_frame_layout)
        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)
        constraintLayout = view.findViewById(R.id.empty_address_constraintLayout)
        addAddress = view.findViewById(R.id.add_address_button)
        addressAdapter = AddressAdapter()
        addressRecyclerView.layoutManager = LinearLayoutManager(view.context)
        addressRecyclerView.adapter = addressAdapter
        addressAdapter.setList(addressList)

        enableConnection = view.findViewById(R.id.enable_connection)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)

    }

    private fun checkRTL() {
        if (isRTL())
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_right)
    }

    private fun initViewModel() {
        viewModelFactory = AddressViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AddressViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.addresses.observe(viewLifecycleOwner) {
            Log.i(TAG, "observeViewModel: it.size------------> " + it.size)
            if (it.isNullOrEmpty()) {
                addressRecyclerView.visibility = View.GONE
                addAddressButton.visibility = View.GONE
                addressAdapter.setList(emptyList())
                constraintLayout.visibility = View.VISIBLE
            } else {
                addressRecyclerView.visibility = View.VISIBLE
                addAddressButton.visibility = View.VISIBLE
                addressAdapter.setList(it)
                constraintLayout.visibility = View.GONE
            }

            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .addToBackStack(null).commit()
    }
}
