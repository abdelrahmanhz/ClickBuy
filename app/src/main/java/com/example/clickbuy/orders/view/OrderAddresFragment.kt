package com.example.clickbuy.orders.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.category.view.CategoryFragment
import com.example.clickbuy.home.view.HomeFragment
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.orders.viewmodel.OrdersAddressViewModel
import com.example.clickbuy.orders.viewmodel.OrdersAddressViewModelFactory
import com.example.clickbuy.productdetails.view.ProductDetailsFragment

import androidx.core.content.res.ResourcesCompat

import androidx.core.content.ContextCompat
import com.example.clickbuy.R
import com.example.clickbuy.models.Address
import com.example.clickbuy.payment.view.AddressInterface
import com.example.clickbuy.payment.view.PaymentFragment

import com.shuhart.stepview.StepView




private const val TAG = "OrderAddresFragment"

class OrderAddresFragment : Fragment() , AddressInterface {

    private lateinit var addressOrderAdapter : AddressOrderAdapter
    private lateinit var orderFactory: OrdersAddressViewModelFactory
    private lateinit var addressOrderRecyclerView: RecyclerView
    private lateinit var viewModel: OrdersAddressViewModel
   // private lateinit var stepView: StepView
    private lateinit var nextButtonFirst : Button

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
       // setUpViewStep()
        setUpAddressOrderRecyclerView()

        viewModel = ViewModelProvider(this, orderFactory).get(OrdersAddressViewModel::class.java)
        viewModel.getAddressOrder("5745222516875")
        viewModel.address.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "brand: $it")
                addressOrderAdapter.setListOfAddreses(it.addresses)
            }
        }

    }
    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameOrderAddress, fragment).commit()
    }
//    private fun setUpViewStep() {
//        stepView.getState()
//            .selectedTextColor(ContextCompat.getColor(requireContext(), com.example.clickbuy.R.color.black))
//            .animationType(StepView.ANIMATION_CIRCLE)
//            .selectedCircleColor(ContextCompat.getColor(requireContext(), com.example.clickbuy.R.color.black))
//            .selectedCircleRadius(resources.getDimensionPixelSize(com.example.clickbuy.R.dimen.cardview_default_radius))
//            .selectedStepNumberColor(
//                ContextCompat.getColor(requireContext(), com.example.clickbuy.R.color.black))
//            .steps(object : ArrayList<String?>() {
//                init {
//                    add("First step")
//                    add("Second step")
//                    add("Third step")
//                }
//            })
//            .stepsNumber(4)
//            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
//            .stepLineWidth(resources.getDimensionPixelSize(com.example.clickbuy.R.dimen.cardview_default_radius))
//            .textSize(resources.getDimensionPixelSize(com.example.clickbuy.R.dimen.cardview_default_radius))
//            .stepNumberTextSize(resources.getDimensionPixelSize(com.example.clickbuy.R.dimen.cardview_default_radius))
//            .typeface(
//                ResourcesCompat.getFont(context!!, R.font.font_bold)).commit()
//    }

    private fun initUI(view: View) {
        addressOrderRecyclerView = view.findViewById(R.id.addressOrderRecycleView)
     //   stepView = view.findViewById(R.id.stepView)

    }

    private fun setUpAddressOrderRecyclerView() {
        val layoutManager = LinearLayoutManager(HomeFragment().context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        addressOrderAdapter = AddressOrderAdapter(requireContext(),this)
        addressOrderRecyclerView.layoutManager = layoutManager
        addressOrderRecyclerView.adapter = addressOrderAdapter
    }

    override fun showAddress(address: Address) {
        val paymentFragment = PaymentFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameOrderAddress, paymentFragment).commit()
        paymentFragment.setAddress(address)
    }

}