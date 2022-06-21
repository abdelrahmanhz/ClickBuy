package com.example.clickbuy.ordershisotry.view

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
import com.example.clickbuy.models.BagItem
import com.example.clickbuy.models.NoteAttribute
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.ordershisotry.OrderDetailsInterface
import com.example.clickbuy.ordershisotry.viewmodel.OrdersViewModel
import com.example.clickbuy.ordershisotry.viewmodel.OrdersViewModelFactory
import com.example.clickbuy.util.ConstantsValue

private const val TAG = "OrdersFragment"

class OrdersFragment : Fragment(), OrderDetailsInterface {
    private lateinit var orderAdapter: OrdersAdapter
    private lateinit var orderFactory: OrdersViewModelFactory
    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var viewModel: OrdersViewModel
    private lateinit var backButton: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderFactory = OrdersViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        initUI(view)
        setUpOrderRecyclerView()
        viewModel = ViewModelProvider(this, orderFactory).get(OrdersViewModel::class.java)
        viewModel.getAllOrdersForSpecificCustomer(ConstantsValue.userID)
        viewModel.order.observe(requireActivity()) {
            if (it != null) {
                Log.i(TAG, "brand: $it")
                orderAdapter.setListOfBrands(it.orders)
            }
        }

    }

    fun initUI(view: View) {
        orderRecyclerView = view.findViewById(R.id.allOrdersRecyclerView)
        backButton = view.findViewById(R.id.BackButtonOrder)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun setUpOrderRecyclerView() {
        val layoutManager = LinearLayoutManager(OrdersFragment().context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        orderAdapter = OrdersAdapter(requireContext(), this)
        orderRecyclerView.layoutManager = layoutManager
        orderRecyclerView.adapter = orderAdapter
    }

    override fun showOrderDetails(lineItemList: List<BagItem>?, itemImageList: List<NoteAttribute>?) {
        Log.i(TAG, "setOrderDetails: ---------> $lineItemList")
        val orderDetails = OrderDetailsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, orderDetails).addToBackStack(null).commit()
        orderDetails.setListOrderDetails(lineItemList, itemImageList)
    }
}