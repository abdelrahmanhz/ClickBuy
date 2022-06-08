package com.example.clickbuy.payment.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.R
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.payment.viewmodel.PaymentViewModel
import com.example.clickbuy.payment.viewmodel.PaymentViewModelFactory
import com.example.clickbuy.util.ConstantsValue
import java.text.DecimalFormat
import kotlin.math.log

private const val TAG = "PaymentFragment"

class PaymentFragment : Fragment() {

    private lateinit var requiredAmountTextView: TextView
    private lateinit var discountAmountTextView: TextView
    private lateinit var totalAmountTextView: TextView
    private lateinit var discountCodeEditText: EditText
    private lateinit var validateCodeButton: AppCompatButton
    private lateinit var payButton: AppCompatButton
    private lateinit var viewModelFactory: PaymentViewModelFactory
    private lateinit var viewModel: PaymentViewModel
    private var requiredAmount: Double = 100.0
    private var discountAmount: Double = 10.0
    private var formattedNumber = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_payment, container, false)

        initViewModel()
        initUI(view)

        validateCodeButton.setOnClickListener {
            if (discountCodeEditText.text.trim().isEmpty()) {
                discountCodeEditText.error = resources.getString(R.string.coupon_empty)
            } else {
                viewModel.validateCoupons(discountCodeEditText.text.toString())
            }
        }


        viewModel.validationCoupon.observe(viewLifecycleOwner) {
            if (it != null) {
                discountAmountTextView.text =
                    DecimalFormat("#.0").format(discountAmount).plus(" %")

                val amount: Double =
                    (requiredAmount * ConstantsValue.currencyValue) * discountAmount / 100

                totalAmountTextView.text = DecimalFormat("#.00")
                    .format((formattedNumber - amount)).plus(ConstantsValue.to)
                Log.i(TAG, "onCreateView: formattedNumber--------> $formattedNumber")
                Log.i(TAG, "onCreateView: amount--------> $amount")
            } else {
                discountCodeEditText.error = resources.getString(R.string.coupon_invalid)
            }
        }

        return view
    }

    private fun initViewModel() {
        viewModelFactory = PaymentViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(PaymentViewModel::class.java)
    }

    private fun initUI(view: View) {
        requiredAmountTextView = view.findViewById(R.id.required_amount_textView)
        discountAmountTextView = view.findViewById(R.id.discount_amount_textView)
        totalAmountTextView = view.findViewById(R.id.total_amount_textView)
        discountCodeEditText = view.findViewById(R.id.discount_code_editText)
        validateCodeButton = view.findViewById(R.id.validate_code_button)
        payButton = view.findViewById(R.id.pay_button)

        formattedNumber = (requiredAmount * ConstantsValue.currencyValue)
        requiredAmountTextView.text =
            DecimalFormat("#.00").format(formattedNumber).plus(" " + ConstantsValue.to)
    }

}