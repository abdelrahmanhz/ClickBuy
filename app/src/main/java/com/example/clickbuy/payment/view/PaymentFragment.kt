package com.example.clickbuy.payment.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.R
import com.example.clickbuy.models.*
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.orders.view.AddressOrderActivity
import com.example.clickbuy.payment.viewmodel.OrderViewModel
import com.example.clickbuy.payment.viewmodel.OrderViewModelFactory
import com.example.clickbuy.payment.viewmodel.PaymentViewModel
import com.example.clickbuy.payment.viewmodel.PaymentViewModelFactory
import com.example.clickbuy.util.ConstantsValue
import com.example.clickbuy.util.calculatePrice
import com.example.clickbuy.util.getEquivalentCurrencyValue
import com.example.clickbuy.util.isRTL
import java.text.DecimalFormat

private const val TAG = "PaymentFragment"

class PaymentFragment : Fragment() {
    private lateinit var requiredAmountTextView: TextView
    private lateinit var discountAmountTextView: TextView
    private lateinit var totalAmountTextView: TextView
    private lateinit var discountCodeEditText: EditText
    private lateinit var validateCodeButton: AppCompatButton
    private lateinit var payButton: AppCompatButton
    private lateinit var viewModelFactory: PaymentViewModelFactory
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderFactory: OrderViewModelFactory

    private lateinit var placeOrderButton: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var paypalRadioButton: RadioButton
    private lateinit var cashRadioButton: RadioButton

    private lateinit var viewModel: PaymentViewModel

    private var discountAmount: String = ConstantsValue.discountAmount

    private var address: Address = Address()

    var bagList: List<BagItem> = emptyList()
    var imagesList: List<NoteAttribute> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_payment, container, false)

        initUI(view)
        initViewModel()
        observeViewModel()
        bagList = (requireActivity() as AddressOrderActivity).bagList
        imagesList = (requireActivity() as AddressOrderActivity).imagesList

        Log.i(TAG, "bagList:-------------------------------------$bagList")
        Log.i(TAG, "imagesList: ---------------------------------$imagesList")

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            when (radioGroup.checkedRadioButtonId) {
                R.id.payPalRadioButton -> {
                    paypalRadioButton.isChecked = true
                    Log.i("TAG", "onViewCreated: paypaaaaall ")
                    Toast.makeText(requireContext(), "On click : paypal", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.payPalRadioButton -> {
                    cashRadioButton.isChecked = true
                    placeOrderButton.visibility = View.VISIBLE
                    placeOrderButton.setOnClickListener {

                    }
                    Log.i("TAG", "onViewCreated: cashhhhhh ")
                    Toast.makeText(requireContext(), "On click : cashhhh", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        validateCodeButton.setOnClickListener {
            if (discountCodeEditText.text.trim().isEmpty()) {
                discountCodeEditText.error = resources.getString(R.string.coupon_empty)
            } else {
                viewModel.validateCoupons(discountCodeEditText.text.toString())
            }
        }

        payButton.setOnClickListener {
            orderViewModel.postOrder(
                OrderPojo(
                    Order(
                        email = "3bdorafaat@gmail.com",
                        line_items = bagList,
                        note_attributes = imagesList,
                        billing_address = address
                    )
                )
            )

        }

        return view
    }

    private fun initUI(view: View) {
        radioGroup = view.findViewById(R.id.radioGroup)
        paypalRadioButton = view.findViewById(R.id.payPalRadioButton)
        cashRadioButton = view.findViewById(R.id.cashRadioButton)

        requiredAmountTextView = view.findViewById(R.id.required_amount_textView)
        discountAmountTextView = view.findViewById(R.id.discount_amount_textView)
        totalAmountTextView = view.findViewById(R.id.total_amount_textView)
        discountCodeEditText = view.findViewById(R.id.discount_code_editText)
        validateCodeButton = view.findViewById(R.id.validate_code_button)
        payButton = view.findViewById(R.id.pay_button)

        //    formattedNumber = (requiredAmount * ConstantsValue.currencyValue)
        requiredAmountTextView.text =
            calculatePrice((requireActivity() as AddressOrderActivity).totalAmountPrice)
    }

    private fun initViewModel() {
        orderFactory = OrderViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        viewModelFactory = PaymentViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                requireContext()
            )
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(PaymentViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.validationCoupon.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.i(TAG, "observeViewModel: discountAmount-----------------> $discountAmount")
                discountAmountTextView.text = getEquivalentCurrencyValue(discountAmount)

                val amount: Double =
                    ((requireActivity() as AddressOrderActivity).totalAmountPrice.toDouble()) + (discountAmount.toDouble() * ConstantsValue.currencyValue)

                totalAmountTextView.text = calculatePrice(amount.toString())

                Log.i(TAG, "onCreateView: amount--------> $amount")
            } else {
                discountCodeEditText.error = resources.getString(R.string.coupon_invalid)
            }
        }

        orderViewModel.order.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.i(TAG, "order: $it")
            }
        }
    }

    fun setAddress(address: Address) {
        this.address = address
        Log.i(TAG, "address chossen: -------> $address")
    }
}