package com.example.clickbuy.me.view.loged

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.R
import com.example.clickbuy.address.view.AddressFragment
import com.example.clickbuy.bag.view.BagFragment
import com.example.clickbuy.currency.view.CurrencyFragment
import com.example.clickbuy.me.view.guest.GuestFragment
import com.example.clickbuy.me.viewmodel.CustomerViewModel
import com.example.clickbuy.me.viewmodel.CustomerViewModelFactory
import com.example.clickbuy.models.Customer
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.util.isRTL
import com.example.clickbuy.orders.OrdersFragment


private const val TAG = "HomeView"

class MeFragment : Fragment() {
    private lateinit var welcomeTextView: TextView
    private lateinit var editProfileRelativeLayout: RelativeLayout
    private lateinit var addressRelativeLayout: RelativeLayout
    private lateinit var wishListRelativeLayout: RelativeLayout
    private lateinit var bagRelativeLayout: RelativeLayout
    private lateinit var currencyRelativeLayout: RelativeLayout
    private lateinit var orderHistoryRelativeLayout: RelativeLayout
    private lateinit var logOutRelativeLayout: RelativeLayout

    private lateinit var editProfileImageView: ImageView
    private lateinit var addressImageView: ImageView
    private lateinit var wishListImageView: ImageView
    private lateinit var bagImageView: ImageView
    private lateinit var currencyImageView: ImageView
    private lateinit var orderHistoryImageView: ImageView
    private lateinit var logOutImageView: ImageView

    private lateinit var viewModelFactory: CustomerViewModelFactory
    private lateinit var viewModel: CustomerViewModel
    private lateinit var customerDetails: Customer

    private val profileEditFragment = ProfileEditFragment()
    private val addressFragment = AddressFragment()
    private val currencyFragment = CurrencyFragment()
    private val bagFragment = BagFragment()
    private val orderHistoryFragment = OrdersFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_me, container, false)

        initView(view)
        initViewModel()
        checkRTL()

        viewModel.customerDetails.observe(viewLifecycleOwner) {
            customerDetails = it[0]
            welcomeTextView.text = getString(R.string.welcome).plus(customerDetails.first_name)

        }


        editProfileRelativeLayout.setOnClickListener {
            Log.i("TAG", "onCreateView: editProfileRelativeLayout")
            replaceFragment(profileEditFragment)
        }

        addressRelativeLayout.setOnClickListener {
            Log.i("TAG", "onCreateView: addressRelativeLayout")
            replaceFragment(addressFragment)
        }

        wishListRelativeLayout.setOnClickListener {
            Log.i("TAG", "wishListRelativeLayout")
            //Replace AddressFragment() with FavoriteFragment()
        }

        bagRelativeLayout.setOnClickListener {
            Log.i("TAG", "bagRelativeLayout")
            replaceFragment(bagFragment)
        }

        currencyRelativeLayout.setOnClickListener {
            Log.i("TAG", "currencyRelativeLayout")
            replaceFragment(currencyFragment)
        }

        orderHistoryRelativeLayout.setOnClickListener {
            Log.i("TAG", "orderHistoryRelativeLayout")
            replaceFragment(orderHistoryFragment)
        }

        logOutRelativeLayout.setOnClickListener {
            Log.i("TAG", "logOutRelativeLayout")
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, GuestFragment()).commit()
            viewModel.deleteSavedSettings()
        }

        Log.i(TAG, "onCreateView: ")
        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .addToBackStack(null).commit()
    }

    private fun initView(view: View) {
        welcomeTextView = view.findViewById(R.id.welcome_textView)
        editProfileRelativeLayout = view.findViewById(R.id.edit_profile_relativeLayout)
        addressRelativeLayout = view.findViewById(R.id.address_relativeLayout)
        wishListRelativeLayout = view.findViewById(R.id.wishList_relativeLayout)
        bagRelativeLayout = view.findViewById(R.id.bag_relativeLayout)
        currencyRelativeLayout = view.findViewById(R.id.currency_relativeLayout)
        orderHistoryRelativeLayout = view.findViewById(R.id.order_history_relativeLayout)
        logOutRelativeLayout = view.findViewById(R.id.logout_relativeLayout)

        editProfileImageView = view.findViewById(R.id.edit_profile_image)
        addressImageView = view.findViewById(R.id.address_image)
        wishListImageView = view.findViewById(R.id.wishList_image)
        bagImageView = view.findViewById(R.id.bag_image)
        currencyImageView = view.findViewById(R.id.currency_image)
        orderHistoryImageView = view.findViewById(R.id.order_history_image)
        logOutImageView = view.findViewById(R.id.logout_image)

    }

    private fun initViewModel() {
        viewModelFactory = CustomerViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CustomerViewModel::class.java)

        viewModel.getCustomerDetails("3bdorafaat@gmail.com")
    }

    private fun checkRTL() {
        if (isRTL()) {
            editProfileImageView.setImageResource(R.drawable.ic_arrow_left)
            addressImageView.setImageResource(R.drawable.ic_arrow_left)
            wishListImageView.setImageResource(R.drawable.ic_arrow_left)
            bagImageView.setImageResource(R.drawable.ic_arrow_left)
            currencyImageView.setImageResource(R.drawable.ic_arrow_left)
            orderHistoryImageView.setImageResource(R.drawable.ic_arrow_left)
            logOutImageView.setImageResource(R.drawable.ic_arrow_left)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")

    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach: ")
    }
}