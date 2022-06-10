package com.example.clickbuy.me.view

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.R
import com.example.clickbuy.bag.view.BagFragment
import com.example.clickbuy.currency.view.CurrencyFragment
import com.example.clickbuy.me.viewmodel.CustomerViewModel
import com.example.clickbuy.me.viewmodel.CustomerViewModelFactory
import com.example.clickbuy.models.Customer
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
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
    private lateinit var viewModelFactory: CustomerViewModelFactory
    private lateinit var viewModel: CustomerViewModel
    private lateinit var customerDetails: Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_me, container, false)
        welcomeTextView = view.findViewById(R.id.welcome_textView)
        editProfileRelativeLayout = view.findViewById(R.id.edit_profile_relativeLayout)
        addressRelativeLayout = view.findViewById(R.id.address_relativeLayout)
        wishListRelativeLayout = view.findViewById(R.id.wishList_relativeLayout)
        bagRelativeLayout = view.findViewById(R.id.bag_relativeLayout)
        currencyRelativeLayout = view.findViewById(R.id.currency_relativeLayout)
        orderHistoryRelativeLayout = view.findViewById(R.id.order_history_relativeLayout)
        logOutRelativeLayout = view.findViewById(R.id.logout_relativeLayout)


        viewModelFactory = CustomerViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CustomerViewModel::class.java)

        viewModel.getCustomerDetails("3bdorafaat@gmail.com")

        viewModel.customerDetails.observe(viewLifecycleOwner, Observer {
            customerDetails = it[0]
            welcomeTextView.text = getString(R.string.welcome).plus(customerDetails.first_name)

        })


        editProfileRelativeLayout.setOnClickListener {
            Log.i("TAG", "onCreateView: editProfileRelativeLayout")
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, ProfileEditFragment())

                .addToBackStack(null).commit()
        }

        addressRelativeLayout.setOnClickListener {
            Log.i("TAG", "onCreateView: addressRelativeLayout")
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, AddressFragment())
                .addToBackStack(null).commit()
        }

        wishListRelativeLayout.setOnClickListener {
            Log.i("TAG", "wishListRelativeLayout")
            //Replace AddressFragment() with FavoriteFragment()
            /* requireActivity().supportFragmentManager.beginTransaction()
                 .replace(R.id.frame, AddressFragment())
                 .addToBackStack(null).commit()*/
        }

        bagRelativeLayout.setOnClickListener {
            Log.i("TAG", "bagRelativeLayout")
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, BagFragment())
                .addToBackStack(null).commit()
        }

        currencyRelativeLayout.setOnClickListener {
            Log.i("TAG", "currencyRelativeLayout")
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, CurrencyFragment()).addToBackStack(null)
                .commit()
        }

        orderHistoryRelativeLayout.setOnClickListener {
            Log.i("TAG", "orderHistoryRelativeLayout")
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, OrdersFragment())
                .addToBackStack(null).commit()
        }

        logOutRelativeLayout.setOnClickListener {
            Log.i("TAG", "logOutRelativeLayout")
            //Code to logout from backend
            //go to login Activity
        }

        Log.i(TAG, "onCreateView: ")
        return view
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