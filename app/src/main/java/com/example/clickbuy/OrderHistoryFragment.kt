package com.example.clickbuy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickbuy.OrderHistoryAdapter
import com.example.clickbuy.R
import com.example.clickbuy.models.OrderHistory
import java.util.*

private const val TAG = "OrderHistoryFragment"

class OrderHistoryFragment : Fragment() {
    private lateinit var arrowBackImageView: ImageView
    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderAdapter: OrderHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var orderList = arrayListOf<OrderHistory>()
        orderList.add(OrderHistory("29 mey 2022", "First", "100$"))
        orderList.add(OrderHistory("28 mar 2022", "Second", "200$"))
        orderList.add(OrderHistory("27 apr 2022", "Third", "300$"))
        orderList.add(OrderHistory("22 jun 2022", "Fourth", "400$"))
        orderList.add(OrderHistory("12 dec 2022", "Fifth", "500$"))
        orderList.add(OrderHistory("02 nov 2022", "Sixth", "600$"))
        orderList.add(OrderHistory("5 mey 2022", "Seventh", "700$"))

        var view = inflater.inflate(R.layout.fragment_order_history, container, false)
        orderRecyclerView = view.findViewById(R.id.recyclerView_currency)
        orderAdapter = OrderHistoryAdapter(view.context)
        orderRecyclerView.layoutManager = LinearLayoutManager(view.context)
        orderRecyclerView.adapter = orderAdapter
        orderAdapter.setList(orderList)
        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)

        if (Locale.getDefault() == Locale.ENGLISH) {
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_left)
            Log.i(TAG, "onCreateView: in if")
        }
        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


        return view
    }


}