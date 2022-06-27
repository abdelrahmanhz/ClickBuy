package com.example.clickbuy.orders.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.clickbuy.home.view.HomeFragment
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.orders.viewmodel.OrdersAddressViewModel
import com.example.clickbuy.orders.viewmodel.OrdersAddressViewModelFactory
import com.example.clickbuy.R
import com.example.clickbuy.models.CustomerAddress
import com.example.clickbuy.payment.paymentmethod.PaymentMethodFragment
import com.example.clickbuy.payment.view.AddressInterface
import com.example.clickbuy.util.ConnectionLiveData
import com.example.clickbuy.util.connectInternet
import com.example.clickbuy.util.isRTL

private const val TAG = "OrderAddressFragment"

class OrderAddressFragment : Fragment(), AddressInterface {

    private lateinit var enableConnection: TextView
    private lateinit var noInternetAnimation: LottieAnimationView

    private lateinit var addressOrderAdapter: AddressOrderAdapter
    private lateinit var orderFactory: OrdersAddressViewModelFactory
    private lateinit var addressOrderRecyclerView: RecyclerView
    private lateinit var viewModel: OrdersAddressViewModel
    private lateinit var backImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_addres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
                viewModel.getAddressOrder()
                addressOrderRecyclerView.visibility = View.VISIBLE
            } else {
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
                addressOrderRecyclerView.visibility = View.GONE
            }
        }

        initUI(view)
        setUpAddressOrderRecyclerView()
        initViewModel()
        observeViewModel()

        enableConnection.setOnClickListener {
            connectInternet(requireContext())
        }

    }

    private fun initUI(view: View) {
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
        enableConnection = view.findViewById(R.id.enable_connection)
        addressOrderRecyclerView = view.findViewById(R.id.addressOrderRecycleView)
        backImageView = view.findViewById(R.id.arrow_back_imageView_address)
        backImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        if (isRTL())
            backImageView.setImageResource(R.drawable.ic_arrow_right)
    }

    private fun initViewModel() {
        orderFactory = OrdersAddressViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        viewModel = ViewModelProvider(this, orderFactory).get(OrdersAddressViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.address.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "brand: $it")
                addressOrderAdapter.setListOfAddresses(it)
            } else {
                addressOrderAdapter.setListOfAddresses(emptyList())
            }
        }
    }

    private fun setUpAddressOrderRecyclerView() {
        val layoutManager = LinearLayoutManager(HomeFragment().context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        addressOrderAdapter = AddressOrderAdapter(requireContext(), this)
        addressOrderRecyclerView.layoutManager = layoutManager
        addressOrderRecyclerView.adapter = addressOrderAdapter
    }

    override fun showAddress(address: CustomerAddress) {
        val paymentFragment = PaymentMethodFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameOrderAddress, paymentFragment).addToBackStack(null).commit()
        paymentFragment.setAddress(address)
    }

}