package com.example.clickbuy.payment.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.clickbuy.R
import com.example.clickbuy.models.*
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.orders.view.AddressOrderActivity
import com.example.clickbuy.payment.viewmodel.OrderViewModel
import com.example.clickbuy.payment.viewmodel.OrderViewModelFactory
import com.example.clickbuy.payment.viewmodel.PaymentViewModel
import com.example.clickbuy.payment.viewmodel.PaymentViewModelFactory
import com.example.clickbuy.util.*
import com.example.clickbuy.util.Constants.PUBLISH_KEY
import com.example.clickbuy.util.Constants.SECRET_KEY
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

private const val TAG = "PaymentFragment"

class PaymentFragment : Fragment() {

    private lateinit var enableConnection: AppCompatButton
    private lateinit var noInternetAnimation: LottieAnimationView

    private lateinit var backButton: ImageView
    private lateinit var requiredTV: TextView
    private lateinit var requiredAmountTextView: TextView
    private lateinit var discountCodeEditText: EditText
    private lateinit var validateCodeButton: AppCompatButton
    private lateinit var discountTV: TextView
    private lateinit var discountAmountTextView: TextView
    private lateinit var totalTV: TextView
    private lateinit var totalAmountTextView: TextView
    private lateinit var payButton: AppCompatButton

    private lateinit var viewModelFactory: PaymentViewModelFactory
    private lateinit var viewModel: PaymentViewModel
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderFactory: OrderViewModelFactory

    private var isCash: Boolean = false
    private var discountAmount: String = ConstantsValue.discountAmount
    private var address: Address = Address()
    private var bagList: List<BagItem> = emptyList()
    private var imagesList: List<NoteAttribute> = emptyList()

    //Payment
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var customerId: String
    private lateinit var ephemeralKey: String
    private lateinit var clientSecret: String
    private var kaser = "00"
    private lateinit var amountRequired: String

    private var discountCodes: MutableList<DiscountCodes> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_payment, container, false)

        initUI(view)
        initViewModel()
        observeViewModel()
        setPaymentMethod()

        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                noInternetAnimation.visibility = View.GONE
                enableConnection.visibility = View.GONE
                setVisibility(View.VISIBLE)
            } else {
                noInternetAnimation.visibility = View.VISIBLE
                enableConnection.visibility = View.VISIBLE
                setVisibility(View.GONE)
            }
        }

        if (isRTL())
            backButton.setImageResource(R.drawable.ic_arrow_right)

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        validateCodeButton.setOnClickListener {
            if (discountCodeEditText.text.trim().isEmpty()) {
                discountCodeEditText.error = resources.getString(R.string.coupon_empty)
            } else {
                viewModel.validateCoupons(discountCodeEditText.text.toString())
            }
        }

        payButton.setOnClickListener {
            if (isCash)
                placeOrder()
            else
                paymentFlow()
        }

        enableConnection.setOnClickListener {
            connectInternet(requireContext())
        }
        return view
    }

    private fun initUI(view: View) {

        noInternetAnimation = view.findViewById(R.id.no_internet_animation)
        enableConnection = view.findViewById(R.id.enable_connection)

        backButton = view.findViewById(R.id.arrow_back_imageView_payment)
        requiredTV = view.findViewById(R.id.requiredTV)
        requiredAmountTextView = view.findViewById(R.id.required_amount_textView)
        discountCodeEditText = view.findViewById(R.id.discount_code_editText)
        validateCodeButton = view.findViewById(R.id.validate_code_button)
        discountTV = view.findViewById(R.id.discountTV)
        discountAmountTextView = view.findViewById(R.id.discount_amount_textView)
        totalTV = view.findViewById(R.id.totalTV)
        totalAmountTextView = view.findViewById(R.id.total_amount_textView)
        payButton = view.findViewById(R.id.pay_button)

        amountRequired = (requireActivity() as AddressOrderActivity).totalAmountPrice

        requiredAmountTextView.text =
            calculatePrice(amountRequired)

        bagList = (requireActivity() as AddressOrderActivity).bagList
        imagesList = (requireActivity() as AddressOrderActivity).imagesList
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
                discountCodes.add(
                    0, DiscountCodes(
                        it.discount_code.code,
                        (-ConstantsValue.discountAmount.toDouble()).toString(),
                        "fixed_amount"
                    )
                )
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

    private fun setPaymentMethod() {
        if (isCash) {
            payButton.text = getString(R.string.placeOrder)
        } else {
            initPayment()
        }
    }

    private fun setVisibility(visibility: Int) {
        requiredTV.visibility = visibility
        requiredAmountTextView.visibility = visibility
        discountCodeEditText.visibility = visibility
        validateCodeButton.visibility = visibility
        discountTV.visibility = visibility
        discountAmountTextView.visibility = visibility
        totalTV.visibility = visibility
        totalAmountTextView.visibility = visibility
        payButton.visibility = visibility
    }

    private fun initPayment() {
        PaymentConfiguration.init(requireContext(), PUBLISH_KEY)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        val request: StringRequest =
            object : StringRequest(
                Method.POST, "https://api.stripe.com/v1/customers",
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        customerId = jsonObject.getString("id")
                        /* Toast.makeText(
                             requireContext(),
                             "Customer Id: $customerId",
                             Toast.LENGTH_SHORT
                         ).show()*/
                        Log.i(TAG, "initPayment: Customer Id-------------------->  $customerId")
                        getEphemeralKey()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    //
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val header: HashMap<String, String> = HashMap<String, String>()
                    header.put("Authorization", "Bearer $SECRET_KEY")
                    return header
                }
            }

        Log.i(TAG, "initPayment: amountRequired----------> $amountRequired")
        amountRequired =
            NumberFormat.getNumberInstance(Locale.US).format(amountRequired.toDouble())
        val amounts = amountRequired.split(".")
        amountRequired = amounts[0]
        if (amounts.size > 1)
            kaser = amounts[1]

        Log.i(TAG, "getParams: amountRequired-----------> $amountRequired")
        Log.i(TAG, "getParams: kaser--------------------> $kaser")
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(request)
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        if (paymentSheetResult is PaymentSheetResult.Completed) {
            Toast.makeText(requireContext(), "Payment Success!!", Toast.LENGTH_SHORT).show()
            placeOrder()
        }
    }

    private fun getEphemeralKey() {
        val request: StringRequest =
            object : StringRequest(
                Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        ephemeralKey = jsonObject.getString("id")
                        /* Toast.makeText(
                             requireContext(),
                             "Epherical Key: $ephemeralKey",
                             Toast.LENGTH_SHORT
                         ).show()*/
                        Log.i(TAG, "getEphemeralKey: Ephemeral Key-----------------> $ephemeralKey")
                        getClientSecret(customerId)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    //
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val header: HashMap<String, String> = HashMap<String, String>()
                    header.put("Authorization", "Bearer $SECRET_KEY")
                    header.put("Stripe-Version", "2020-08-27")
                    return header
                }

                override fun getParams(): Map<String, String> {
                    val param: HashMap<String, String> = HashMap<String, String>()
                    param.put("customer", customerId)
                    return param
                }
            }

        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(request)
    }

    private fun getClientSecret(customerId: String) {
        val request: StringRequest =
            object : StringRequest(
                Method.POST, "https://api.stripe.com/v1/payment_intents",
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        clientSecret = jsonObject.getString("client_secret")
                        /*   Toast.makeText(
                               requireContext(),
                               "Client Secret: " + clientSecret,
                               Toast.LENGTH_SHORT
                           ).show()*/
                        Log.i(TAG, "getClientSecret: Client Secret-----------------> $clientSecret")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    //
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val header: HashMap<String, String> = HashMap<String, String>()
                    header.put("Authorization", "Bearer $SECRET_KEY")
                    return header
                }

                override fun getParams(): Map<String, String> {
                    val param: HashMap<String, String> = HashMap<String, String>()
                    param.put("customer", customerId)
                    param.put("amount", amountRequired + kaser)
                    param.put("currency", ConstantsValue.to)
                    param.put("automatic_payment_methods[enabled]", "true")
                    return param
                }
            }

        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(request)
    }

    private fun placeOrder() {
        orderViewModel.postOrder(
            OrderPojo(
                Order(
                    email = ConstantsValue.email,
                    line_items = bagList,
                    note_attributes = imagesList,
                    billing_address = address,
                    discount_codes = discountCodes
                )
            )
        )
    }

    fun setData(address: Address, isCash: Boolean) {
        this.address = address
        this.isCash = isCash
        Log.i(TAG, "address chosen: -------> $address")
    }

    private fun paymentFlow() {
        paymentSheet.presentWithPaymentIntent(
            clientSecret, PaymentSheet.Configuration(
                "ITI",
                PaymentSheet.CustomerConfiguration(customerId, ephemeralKey)
            )
        )
    }
}