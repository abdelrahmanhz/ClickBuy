package com.example.clickbuy.orders.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.clickbuy.R
import com.example.clickbuy.payment.view.PaymentFragment

class PaymentWayFragment : Fragment() {
    private lateinit var placeOrderButton : Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var paypalRadioButton :RadioButton
    private lateinit var cashRadioButton :RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_way, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radioGroup = view.findViewById(R.id.radioGroup)
        placeOrderButton = view.findViewById(R.id.placeOrderButton)
        paypalRadioButton = view.findViewById(R.id.payPalRadioButton)
        cashRadioButton = view.findViewById(R.id.cashRadioButton)
        radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                when (radioGroup.checkedRadioButtonId) {
                    R.id.payPalRadioButton -> {
                        paypalRadioButton.isChecked = true
                        Log.i("TAG", "onViewCreated: paypaaaaall " )
                        Toast.makeText(requireContext(), "On click : paypal", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.frame, PaymentFragment()).commit()
                    }
                    R.id.payPalRadioButton -> {
                        cashRadioButton.isChecked = true
                        placeOrderButton.visibility = View.VISIBLE
                        placeOrderButton.setOnClickListener {

                        }
                        Log.i("TAG", "onViewCreated: cashhhhhh " )
                        Toast.makeText(requireContext(), "On click : cashhhh", Toast.LENGTH_SHORT).show()

                    }
                }
            })




    }

        }

