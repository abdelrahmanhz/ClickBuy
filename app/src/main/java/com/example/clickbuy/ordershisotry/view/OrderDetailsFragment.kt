package com.example.clickbuy.ordershisotry.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.clickbuy.R
import com.example.clickbuy.models.BagItem
import com.example.clickbuy.models.NoteAttribute
import com.example.clickbuy.models.Order
import com.example.clickbuy.util.ConnectionLiveData
import com.example.clickbuy.util.calculatePrice
import com.example.clickbuy.util.connectInternet
import com.example.clickbuy.util.isRTL
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*

class OrderDetailsFragment : Fragment() {
    private lateinit var order: Order
    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var enableConnection: TextView

    private lateinit var dateTV: TextView
    private lateinit var dateTextView: TextView
    private lateinit var timeTV: TextView
    private lateinit var timeTextView: TextView
    private lateinit var priceTV: TextView
    private lateinit var priceTextView: TextView
    private lateinit var ordersItemsTV: TextView
    private lateinit var ordersItemsTextView: TextView

    private lateinit var backButton: ImageView
    private lateinit var orderDetailsRecyclerView: RecyclerView
    private lateinit var lineItemList: List<BagItem>
    private lateinit var itemImageList: List<NoteAttribute>
    private lateinit var orderDetailsAdapter: OrderDetailsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_details, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkConnection()
        initUI(view)
        setUpOrderDetailsRecyclerView()

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        enableConnection.setOnClickListener {
            connectInternet(requireContext())
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI(view: View) {
        enableConnection = view.findViewById(R.id.enable_connection)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
        backButton = view.findViewById(R.id.BackButtonOrderDetails)
        dateTV = view.findViewById(R.id.dateTV)
        dateTextView = view.findViewById(R.id.order_date_text_view)
        timeTV = view.findViewById(R.id.timeTV)
        timeTextView = view.findViewById(R.id.order_time_text_view)
        priceTV = view.findViewById(R.id.priceTV)
        priceTextView = view.findViewById(R.id.order_price_text_view)
        ordersItemsTV = view.findViewById(R.id.orderItemsTV)
        ordersItemsTextView = view.findViewById(R.id.order_items_text_view)
        orderDetailsRecyclerView = view.findViewById(R.id.orderDetailsRecyclerView)
        if (isRTL())
            backButton.setImageResource(R.drawable.ic_arrow_right)

        /*  val date =
              order.created_at?.split(Regex("T"), order.created_at?.length!!)
          val time = date?.get(1)?.split(Regex("\\+"), date.size)
          val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
          val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
          val date2 = dateFormatter.parse(date?.get(0))
          val time2 = timeFormatter.parse(time?.get(0))
          dateTextView.text = dateFormatter.format(date2)
          timeTextView.text = timeFormatter.format(time2)*/
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
        val dateTime: LocalDateTime = LocalDateTime.parse(order.created_at, formatter)
        val dateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy, MMM d", Locale.getDefault())
                .withDecimalStyle(DecimalStyle.of(Locale.getDefault()))

        val timeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss", Locale.getDefault())
                .withDecimalStyle(DecimalStyle.of(Locale.getDefault()))

        dateTextView.text = dateTime.format(dateFormatter)
        timeTextView.text = dateTime.format(timeFormatter)
        priceTextView.text = calculatePrice(order.total_price!!)
        ordersItemsTextView.text = DecimalFormat.getInstance().format(order.line_items?.size)
    }

    private fun setUpOrderDetailsRecyclerView() {
        val layoutManager = LinearLayoutManager(OrderDetailsFragment().context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        orderDetailsAdapter = OrderDetailsAdapter(requireContext())
        orderDetailsRecyclerView.layoutManager = layoutManager
        orderDetailsRecyclerView.adapter = orderDetailsAdapter
        orderDetailsAdapter.setListOfOrdersDetails(order.line_items!!, order.note_attributes!!)
    }

    private fun checkConnection() {
        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                setVisibility(View.VISIBLE)
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
            } else {
                setVisibility(View.GONE)
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
            }
        }
    }

    private fun setVisibility(visibility: Int) {
        dateTV.visibility = visibility
        dateTextView.visibility = visibility
        timeTV.visibility = visibility
        timeTextView.visibility = visibility
        priceTV.visibility = visibility
        priceTextView.visibility = visibility
        ordersItemsTV.visibility = visibility
        ordersItemsTextView.visibility = visibility
        orderDetailsRecyclerView.visibility = visibility
    }

    fun setListOrderDetails(lineItemList: List<BagItem>?, itemImageList: List<NoteAttribute>?) {
        this.lineItemList = lineItemList!!
        this.itemImageList = itemImageList!!
    }

    fun setListOrderDetails(order: Order) {
        this.order = order
    }
}