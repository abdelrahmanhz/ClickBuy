package com.example.clickbuy.me.view.logged

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.R
import com.example.clickbuy.me.viewmodel.CustomerViewModel
import com.example.clickbuy.me.viewmodel.CustomerViewModelFactory
import com.example.clickbuy.models.*
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.util.ConstantsValue
import com.example.clickbuy.util.isRTL
import com.google.android.material.textfield.TextInputEditText

class ProfileEditFragment : Fragment() {

    private lateinit var profileTextView: TextView
    private lateinit var arrowBackImageView: ImageView
    private lateinit var editImageView: ImageView
    private lateinit var firstNameEditText: TextInputEditText
    private lateinit var lastNameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var saveButton: AppCompatButton

    private lateinit var viewModelFactory: CustomerViewModelFactory
    private lateinit var viewModel: CustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)

        initUI(view)
        initViewModel()
        observeCustomerDetails()
        if (isRTL())
            arrowBackImageView.setImageResource(R.drawable.ic_arrow_right)

        editImageView.setOnClickListener {
            enableView(true)
            saveButton.visibility = View.VISIBLE
            editImageView.visibility = View.GONE
            profileTextView.text = getString(R.string.edit_profile)
        }

        saveButton.setOnClickListener {
            enableView(false)
            saveButton.visibility = View.GONE
            editImageView.visibility = View.VISIBLE
            profileTextView.text = getString(R.string.profile)

            viewModel.updateCustomerDetailsTest(
                CustomersTest(
                    CustomerTest(
                        first_name = firstNameEditText.text.toString(),
                        last_name = lastNameEditText.text.toString(),
                        tags = passwordEditText.text.toString(),
                        id = ConstantsValue.userID.toLong()
                    )
                )
            )
        }

        arrowBackImageView.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun observeCustomerDetails() {
        viewModel.customerDetails.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                lastNameEditText.setText(it[0].last_name)
                firstNameEditText.setText(it[0].first_name)
                passwordEditText.setText(it[0].tags)
            }
        }
    }

    private fun initUI(view: View) {
        profileTextView = view.findViewById(R.id.profile_textView)
        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)
        editImageView = view.findViewById(R.id.edit_imageView)
        lastNameEditText = view.findViewById(R.id.last_name_textInputEditText)
        firstNameEditText = view.findViewById(R.id.first_name_textInputEditText)
        passwordEditText = view.findViewById(R.id.password_textInputEditText)
        saveButton = view.findViewById(R.id.save_button)
        arrowBackImageView = view.findViewById(R.id.arrow_back_imageView)
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

        viewModel.getCustomerDetails(ConstantsValue.email)
    }

    private fun enableView(isEnabled: Boolean) {
        lastNameEditText.isEnabled = isEnabled
        firstNameEditText.isEnabled = isEnabled
        passwordEditText.isEnabled = isEnabled
    }
}