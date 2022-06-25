package com.example.clickbuy.authentication.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.clickbuy.R
import com.example.clickbuy.databinding.FragmentSignupBinding
import com.example.clickbuy.models.Customer
import com.example.clickbuy.models.CustomerParent
import com.example.clickbuy.util.ConnectionLiveData


class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonsListeners()
        checkInternetConnection()
    }

    private fun checkInternetConnection() {
        ConnectionLiveData.getInstance(requireContext()).observe(viewLifecycleOwner) {
            if (it) showFormAndHideAnimation()
            else hideFormAndShowAnimation()
        }

        binding.signupEnableConnection.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            else
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
    }

    private fun hideFormAndShowAnimation() {
        binding.signupNoInternetAnimation.visibility = View.VISIBLE
        binding.signupEnableConnection.visibility = View.VISIBLE
        binding.signupBackShape.visibility = View.GONE
        binding.titleSignup.visibility = View.GONE
        binding.cardViewSignup.visibility = View.GONE
        binding.loginOption.visibility = View.GONE
    }

    private fun showFormAndHideAnimation() {
        binding.signupNoInternetAnimation.visibility = View.GONE
        binding.signupEnableConnection.visibility = View.GONE
        binding.signupBackShape.visibility = View.VISIBLE
        binding.titleSignup.visibility = View.VISIBLE
        binding.cardViewSignup.visibility = View.VISIBLE
        binding.loginOption.visibility = View.VISIBLE
    }

    private fun setupButtonsListeners() {
        binding.moveToSignInSignup.setOnClickListener { findNavController().popBackStack() }
        binding.signUp.setOnClickListener { submitForm() }
    }

    private fun submitForm() {
        binding.firstNameSignup.helperText = validFirstName()
        binding.lastNameSignup.helperText = validLastName()
        binding.emailSignup.helperText = validEmail()
        binding.passwordSignup.helperText = validPassword()
        binding.passwordConfirmSignup.helperText = validPasswordConfirm()
        binding.phoneSignup.helperText = validPhone()

        val validFirstName = binding.firstNameSignup.helperText == null
        val validLastName = binding.lastNameSignup.helperText == null
        val validEmail = binding.emailSignup.helperText == null
        val validPassword = binding.passwordSignup.helperText == null
        val validPasswordConfirmation = binding.passwordConfirmSignup.helperText == null
        val validPhone = binding.phoneSignup.helperText == null

        if (validFirstName && validLastName && validEmail && validPassword && validPasswordConfirmation && validPhone)
            signUp()
    }

    private fun signUp() {
        val customer = Customer(
            first_name = binding.firstNameSignupEditText.text.toString(),
            last_name = binding.lastNameSignupEditText.text.toString(),
            email = binding.emailSignupEditText.text.toString(),
            tags = binding.passwordSignupEditText.text.toString(),
            phone = binding.phoneSignupSignupEditText.text.toString(),
        )

        val viewModel = (requireActivity() as AuthenticationActivity).viewModel
        viewModel.registerCustomer(CustomerParent(customer))
        viewModel.isRegistered.observe(viewLifecycleOwner) {
            if (it) {
                clearFields()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.successful_register),
                    Toast.LENGTH_LONG
                ).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.register_fail),
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    private fun clearFields() {
        binding.firstNameSignupEditText.text?.clear()
        binding.lastNameSignupEditText.text?.clear()
        binding.emailSignupEditText.text?.clear()
        binding.passwordSignupEditText.text?.clear()
        binding.passwordConfirmSignupSignupEditText.text?.clear()
        binding.phoneSignupSignupEditText.text?.clear()
    }

    private fun validFirstName(): String? {
        if (binding.firstNameSignupEditText.text.isNullOrBlank())
            return getString(R.string.required)
        return null
    }

    private fun validLastName(): String? {
        if (binding.lastNameSignupEditText.text.isNullOrBlank())
            return getString(R.string.required)
        return null
    }

    private fun validEmail(): String? {
        val emailText = binding.emailSignupEditText.text.toString()
        if (emailText.isBlank())
            return getString(R.string.required)
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
            return getString(R.string.invalid_email)
        return null
    }


    private fun validPassword(): String? {
        val passwordText = binding.passwordSignupEditText.text.toString()
        if (passwordText.isBlank())
            return getString(R.string.required)
        if (passwordText.length < 8)
            return getString(R.string.invalid_password)
        return null
    }


    private fun validPasswordConfirm(): String? {
        val passwordConfirmText = binding.passwordConfirmSignupSignupEditText.text.toString()
        if (passwordConfirmText.isBlank())
            return getString(R.string.required)
        if (passwordConfirmText != binding.passwordSignupEditText.text.toString())
            return getString(R.string.password_not_match)
        return null
    }

    private fun validPhone(): String? {
        val phoneText = binding.phoneSignupSignupEditText.text.toString()
        if (phoneText.isBlank())
            return getString(R.string.required)
        if (!phoneText.matches(".*[0-9].*".toRegex()))
            return getString(R.string.invalid_phone)
        if (phoneText.length != 11)
            return getString(R.string.invalid_phone_length)

        return null
    }
}