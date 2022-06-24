package com.example.clickbuy.orders.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.home.view.HomeFragment
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.orders.viewmodel.OrdersAddressViewModel
import com.example.clickbuy.orders.viewmodel.OrdersAddressViewModelFactory
import com.example.clickbuy.R
import com.example.clickbuy.models.Address
import com.example.clickbuy.payment.paymentmethod.PaymentMethodFragment
import com.example.clickbuy.payment.view.AddressInterface
import com.example.clickbuy.util.ConstantsValue


private const val TAG = "OrderAddressFragment"

class OrderAddressFragment : Fragment(), AddressInterface {

    private lateinit var addressOrderAdapter: AddressOrderAdapter
    private lateinit var orderFactory: OrdersAddressViewModelFactory
    private lateinit var addressOrderRecyclerView: RecyclerView
    private lateinit var viewModel: OrdersAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_addres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderFactory = OrdersAddressViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        initUI(view)
        setUpAddressOrderRecyclerView()

        viewModel = ViewModelProvider(this, orderFactory).get(OrdersAddressViewModel::class.java)
        viewModel.getAddressOrder(ConstantsValue.userID)
        viewModel.address.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "brand: $it")
                addressOrderAdapter.setListOfAddreses(it.addresses)
            }
        }

    }

    private fun initUI(view: View) {
        addressOrderRecyclerView = view.findViewById(R.id.addressOrderRecycleView)

    }

    private fun setUpAddressOrderRecyclerView() {
        val layoutManager = LinearLayoutManager(HomeFragment().context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        addressOrderAdapter = AddressOrderAdapter(requireContext(), this)
        addressOrderRecyclerView.layoutManager = layoutManager
        addressOrderRecyclerView.adapter = addressOrderAdapter
    }

    override fun showAddress(address: Address) {
        val paymentFragment = PaymentMethodFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameOrderAddress, paymentFragment).addToBackStack(null).commit()
        paymentFragment.setAddress(address)
    }

}