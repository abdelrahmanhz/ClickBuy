package com.example.clickbuy.ordershisotry.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.R
import com.example.clickbuy.models.BagItem
import com.example.clickbuy.models.NoteAttribute
import com.example.clickbuy.ordershisotry.OrderDetailsAdapter
import com.example.clickbuy.ordershisotry.OrderDetailsInterface
import com.example.clickbuy.util.isRTL


class OrderDetailsFragment : Fragment(), OrderDetailsInterface {
        private lateinit var lineItemList: List<BagItem>
    private lateinit var itemImageList: List<NoteAttribute>
    private lateinit var orderDetailsAdapter: OrderDetailsAdapter
    private lateinit var orderDetailsRecyclerView: RecyclerView
    private lateinit var backButton: ImageView
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
        if (isRTL())
            backButton.setImageResource(R.drawable.ic_arrow_right)
        orderDetailsRecyclerView = view.findViewById(R.id.orderDetailsRecyclerView)
        setUpOrderDetailsRecyclerView()
        orderDetailsAdapter.setListOfOrdersDetails(lineItemList, itemImageList)
    }

    fun setListOrderDetails(lineItemList: List<BagItem>?, itemImageList: List<NoteAttribute>?) {
        this.lineItemList = lineItemList!!
        this.itemImageList = itemImageList!!
    }

    private fun setUpOrderDetailsRecyclerView() {
        val layoutManager = LinearLayoutManager(OrderDetailsFragment().context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        orderDetailsAdapter = OrderDetailsAdapter(requireContext())
        orderDetailsRecyclerView.layoutManager = layoutManager
        orderDetailsRecyclerView.adapter = orderDetailsAdapter
    }

    override fun showOrderDetails(lineItemList: List<BagItem>?, itemImageList: List<NoteAttribute>?) {
        setListOrderDetails(lineItemList, itemImageList)
    }
}