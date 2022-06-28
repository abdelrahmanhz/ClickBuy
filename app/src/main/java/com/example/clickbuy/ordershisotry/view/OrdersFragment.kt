package com.example.clickbuy.ordershisotry.view

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
import com.example.clickbuy.R
import com.example.clickbuy.models.BagItem
import com.example.clickbuy.models.NoteAttribute
import com.example.clickbuy.models.Order
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.ordershisotry.viewmodel.OrdersViewModel
import com.example.clickbuy.ordershisotry.viewmodel.OrdersViewModelFactory
import com.example.clickbuy.util.ConnectionLiveData
import com.example.clickbuy.util.ConstantsValue
import com.example.clickbuy.util.isRTL
import com.facebook.shimmer.ShimmerFrameLayout

class OrdersFragment : Fragment(), OrderDetailsInterface {

    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var enableConnection: TextView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var emptyOrdersAnimation: LottieAnimationView
    private lateinit var emptyOrdersTextView: TextView
    private lateinit var arrowBackImageView: ImageView
    private lateinit var orderRecyclerView: RecyclerView

    private lateinit var orderAdapter: OrdersAdapter
    private lateinit var orderFactory: OrdersViewModelFactory
    private lateinit var viewModel: OrdersViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                shimmerFrameLayout.visibility = View.VISIBLE
                shimmerFrameLayout.startShimmerAnimation()
                viewModel.getAllOrdersForSpecificCustomer(ConstantsValue.userID)
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
            } else {
                orderRecyclerView.visibility = View.GONE
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmerAnimation()
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
            }
        }

        initUI(view)
        setUpOrderRecyclerView()
        initViewModel()
        observeViewModel()


        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    fun initUI(view: View) {
        enableConnection = view.findViewById(R.id.enable_connection)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
        shimmerFrameLayout = view.findViewById(R.id.shimmer_frame_layout)
        emptyOrdersAnimation = view.findViewById(R.id.empty_order_imageView)
        emptyOrdersTextView = view.findViewById(R.id.empty_order_textView)
        orderRecyclerView = view.findViewById(R.id.allOrdersRecyclerView)
        arrowBackImageView = view.findViewById(R.id.BackButtonOrder)
        if (isRTL())
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_right)

    }

    private fun setUpOrderRecyclerView() {
        val layoutManager = LinearLayoutManager(OrdersFragment().context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        orderAdapter = OrdersAdapter(requireContext(), this)
        orderRecyclerView.layoutManager = layoutManager
        orderRecyclerView.adapter = orderAdapter
    }

    private fun initViewModel() {
        orderFactory = OrdersViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        viewModel = ViewModelProvider(this, orderFactory).get(OrdersViewModel::class.java)
    }

    private fun observeViewModel() {
        Log.i("TAG", "observeViewModel: ")
        viewModel.order.observe(viewLifecycleOwner) {
            if (it != null && it.orders.isNotEmpty()) {
                Log.i("TAG", "observeViewModel: exist data")
                orderAdapter.setListOfOrders(it.orders)
                emptyOrdersAnimation.visibility = View.GONE
                emptyOrdersTextView.visibility = View.GONE
                orderRecyclerView.visibility = View.VISIBLE
            } else {
                Log.i("TAG", "observeViewModel: no data")
                orderAdapter.setListOfOrders(emptyList())
                emptyOrdersAnimation.visibility = View.VISIBLE
                emptyOrdersTextView.visibility = View.VISIBLE
                orderRecyclerView.visibility = View.GONE
            }
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }
    }

    override fun showOrderDetails(
        lineItemList: List<BagItem>?,
        itemImageList: List<NoteAttribute>?
    ) {
        val orderDetails = OrderDetailsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, orderDetails).addToBackStack(null).commit()
        orderDetails.setListOrderDetails(lineItemList, itemImageList)
    }

    override fun showOrderDetails(order: Order) {
        val orderDetails = OrderDetailsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, orderDetails).addToBackStack(null).commit()
        orderDetails.setListOrderDetails(order)
    }
}