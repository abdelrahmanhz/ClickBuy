package com.example.clickbuy.address.showaddresses.view

import android.os.Bundle
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
import com.example.clickbuy.R
import com.example.clickbuy.address.showaddresses.viewmodel.AddressViewModel
import com.example.clickbuy.address.showaddresses.viewmodel.AddressViewModelFactory
import com.example.clickbuy.address.addaddresses.view.AddAddressFragment
import com.example.clickbuy.models.CustomerAddress
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.util.isRTL
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton


private const val TAG = "AddressFragment"

class AddressFragment : Fragment() {

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

        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        addAddressButton.setOnClickListener {
            replaceFragment(AddAddressFragment())
            Toast.makeText(requireContext(), "Go to map or GPS", Toast.LENGTH_SHORT).show()
        }

        addAddress.setOnClickListener {
            replaceFragment(AddAddressFragment())
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

        viewModel.getAllAddresses()
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
