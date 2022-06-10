package com.example.clickbuy.orders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.home.BrandsAdapter
import com.example.clickbuy.home.OrdersAdapter
import com.example.clickbuy.home.SalesAdapter
import com.example.clickbuy.home.viewmodel.HomeViewModelFactory
import com.example.clickbuy.models.ItemImage
import com.example.clickbuy.models.LineItem
private const val TAG = "OrderDetailsFragment"

class OrderDetailsFragment : Fragment(),OrderDetailsInterface {
        private lateinit var lineItemList: List<LineItem>
        private lateinit var itemImageList: List<ItemImage>
    private lateinit var orderDetailsAdapter: OrderDetailsAdapter
    private lateinit var orderDetailsRecyclerView: RecyclerView
    private lateinit var backButton :Button
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton = view.findViewById(R.id.BackButtonOrderDetails)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        orderDetailsRecyclerView = view.findViewById(R.id.orderDetailsRecyclerView)
        setUpOrderDetailsRecyclerView()
        orderDetailsAdapter.setListOfOrdersDetails(lineItemList,itemImageList)

    }

    fun setListOrderDetails(lineItemList: List<LineItem>, itemImageList: List<ItemImage>) {
        this.lineItemList = lineItemList
        this.itemImageList =itemImageList
        Log.i(TAG, "setOrderDetails: -------> $lineItemList")
    }
    private fun setUpOrderDetailsRecyclerView() {
        val layoutManager = LinearLayoutManager(OrderDetailsFragment().context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        orderDetailsAdapter = OrderDetailsAdapter(requireContext(),this)
        orderDetailsRecyclerView.layoutManager = layoutManager
        orderDetailsRecyclerView.adapter = orderDetailsAdapter
    }

    override fun showOrderDetails(lineItemList: List<LineItem>, itemImageList: List<ItemImage>) {
        setListOrderDetails(lineItemList,itemImageList)
    }
}