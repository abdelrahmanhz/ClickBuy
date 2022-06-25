package com.example.clickbuy.address.addaddresses.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.address.addaddresses.viewmodel.AddressViewModel
import com.example.clickbuy.address.addaddresses.viewmodel.AddressViewModelFactory
import com.example.clickbuy.models.CustomerAddress
import com.example.clickbuy.models.CustomerAddressUpdate
import com.example.clickbuy.models.Repository
import com.example.clickbuy.models.Result
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.util.isRTL
import com.google.android.material.textfield.TextInputEditText

private const val TAG = "AddAddressFragment"

class AddAddressFragment : Fragment(), OnAddressSelected {

    private lateinit var noPlaceFoundImageView: ImageView
    private lateinit var noPlaceFoundTextView: TextView
    private lateinit var arrowBackImageView: ImageView
    private lateinit var placeNameEditText: TextInputEditText
    private lateinit var getAddress: AppCompatButton
    private lateinit var addressesRecyclerView: RecyclerView
    private lateinit var addressAdapter: AddAddressAdapter
    private lateinit var viewModelFactory: AddressViewModelFactory
    private lateinit var viewModel: AddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_address, container, false)

        initView(view)
        initViewModel()
        observeGetAddress()
        observeAddedAddress()

        if (isRTL())
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_right)

        getAddress.setOnClickListener {
            val placeName = placeNameEditText.text.toString().trim()
            if (placeName.isNotEmpty()) {
                viewModel.getAddress(placeName)
                placeNameEditText.error = null
            } else {
                placeNameEditText.error = getString(R.string.address_error)
            }
        }

        return view
    }

    private fun initView(view: View) {
        noPlaceFoundImageView = view.findViewById(R.id.no_address_exist_imageView)
        noPlaceFoundTextView = view.findViewById(R.id.no_address_exist_textView)
        placeNameEditText = view.findViewById(R.id.place_name_editText)
        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)
        getAddress = view.findViewById(R.id.get_address_button)
        addressesRecyclerView = view.findViewById(R.id.address_recycler_view)
        addressAdapter = AddAddressAdapter(this)
        addressesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        addressesRecyclerView.adapter = addressAdapter
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

    private fun observeGetAddress() {
        viewModel.addresses.observe(viewLifecycleOwner) {
            if (it.status.code == 200) {
                Log.i(TAG, "addresses.observe: ---------> success")
                addressesRecyclerView.visibility = View.VISIBLE
                noPlaceFoundTextView.visibility = View.GONE
                noPlaceFoundImageView.visibility = View.GONE
                addressAdapter.setListOfAddresses(it.results)
            }else {
                Log.i(TAG, "addresses.observe:-----------> filed")
                addressesRecyclerView.visibility = View.GONE
                noPlaceFoundTextView.visibility = View.VISIBLE
                noPlaceFoundImageView.visibility = View.VISIBLE
                addressAdapter.setListOfAddresses(emptyList())
            }
        }
    }

    private fun observeAddedAddress() {
        viewModel.isAdded.observe(viewLifecycleOwner) {
            if (it)
                requireActivity().supportFragmentManager.popBackStack()
            else
                Toast.makeText(
                    requireContext(),
                    getString(R.string.add_address_error),
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    override fun onAddressSelected(address: Result) {

        Log.i(TAG, "onAddressSelected: address1 " + placeNameEditText.text.toString())
        Log.i(TAG, "onAddressSelected: city " + address.components.city)
        Log.i(TAG, "onAddressSelected: country " + address.components.country)
        Log.i(TAG, "onAddressSelected: country_code " + address.components.country_code)
        Log.i(TAG, "onAddressSelected: country_name " + address.components.country)
        Log.i(TAG, "onAddressSelected: province " + address.components.state)

        viewModel.addAddress(
            CustomerAddressUpdate(
                CustomerAddress(
                    address1 = placeNameEditText.text.toString(),
                    city = address.components.state,
                    country = address.components.country,
                    country_code = address.components.country_code,
                    country_name = address.components.country,
                    default = false,
                    province = address.components.state
                )
            )
        )

    }
}