package com.example.clickbuy.payment.paymentmethod

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import com.example.clickbuy.R
import com.example.clickbuy.models.Address
import com.example.clickbuy.models.CustomerAddress
import com.example.clickbuy.payment.view.PaymentFragment
import com.example.clickbuy.util.isRTL

private const val TAG = "PaymentMethodFragment"

class PaymentMethodFragment : Fragment() {

    private var address: CustomerAddress = CustomerAddress()
    private lateinit var stripeRadioButton: RadioButton
    private lateinit var cashRadioButton: RadioButton
    private lateinit var backButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_payment_method, container, false)

        stripeRadioButton = view.findViewById(R.id.stripe_radio_button)
        cashRadioButton = view.findViewById(R.id.cash_radio_button)
        backButton = view.findViewById(R.id.arrow_back_imageView_payment)


        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        if (isRTL())
            backButton.setImageResource(R.drawable.ic_arrow_right)

        stripeRadioButton.setOnClickListener {
            replaceFragment(false)
        }

        cashRadioButton.setOnClickListener {
            replaceFragment(true)
        }
        return view
    }

    private fun replaceFragment(isCash: Boolean) {
        val paymentFragment = PaymentFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameOrderAddress, paymentFragment).addToBackStack(null).commit()
        paymentFragment.setData(address, isCash)
    }

    fun setAddress(address: CustomerAddress) {
        this.address = address
        Log.i(TAG, "address chosen: -------> $address")
    }

}