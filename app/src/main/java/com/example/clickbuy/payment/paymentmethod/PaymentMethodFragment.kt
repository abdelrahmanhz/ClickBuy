package com.example.clickbuy.payment.paymentmethod


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.clickbuy.R
import com.example.clickbuy.models.CustomerAddress
import com.example.clickbuy.payment.view.PaymentFragment
import com.example.clickbuy.util.ConnectionLiveData
import com.example.clickbuy.util.connectInternet
import com.example.clickbuy.util.isRTL

class PaymentMethodFragment : Fragment() {

    private lateinit var enableConnection: TextView
    private lateinit var noInternetAnimation: LottieAnimationView
    private lateinit var paymentMethodTextView: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var stripeRadioButton: RadioButton
    private lateinit var cashRadioButton: RadioButton
    private lateinit var backButton: ImageView
    private var address: CustomerAddress = CustomerAddress()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
                radioGroup.visibility = View.VISIBLE
                paymentMethodTextView.visibility = View.VISIBLE
            } else {
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
                radioGroup.visibility = View.GONE
                paymentMethodTextView.visibility = View.GONE
            }
        }

        radioGroup.setOnCheckedChangeListener { _, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.stripe_radio_button -> {
                    replaceFragment(false)
                    stripeRadioButton.isChecked = false

                }
                R.id.cash_radio_button -> {
                    replaceFragment(true)
                    cashRadioButton.isChecked = false
                }
            }
        }

        enableConnection.setOnClickListener {
            connectInternet(requireContext())
        }

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun initView(view: View) {
        paymentMethodTextView = view.findViewById(R.id.payment_method_textView)
        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
        enableConnection = view.findViewById(R.id.enable_connection)
        stripeRadioButton = view.findViewById(R.id.stripe_radio_button)
        cashRadioButton = view.findViewById(R.id.cash_radio_button)
        backButton = view.findViewById(R.id.arrow_back_imageView_payment)
        radioGroup = view.findViewById(R.id.radioGroup)
        if (isRTL())
            backButton.setImageResource(R.drawable.ic_arrow_right)

    }

    private fun replaceFragment(isCash: Boolean) {
        val paymentFragment = PaymentFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameOrderAddress, paymentFragment).addToBackStack(null).commit()
        paymentFragment.setData(address, isCash)
    }

    fun setAddress(address: CustomerAddress) {
        this.address = address
    }

}