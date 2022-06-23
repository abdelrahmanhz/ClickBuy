package com.example.clickbuy.authentication.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clickbuy.R
import com.example.clickbuy.databinding.FragmentLoginBinding
import com.example.clickbuy.mainscreen.view.MainActivity
import com.example.clickbuy.util.ConnectionLiveData

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
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

        binding.loginEnableConnection.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            } else {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
        }
    }

    private fun showFormAndHideAnimation(){
        binding.loginNoInternetAnimation.visibility = View.GONE
        binding.loginEnableConnection.visibility = View.GONE
        binding.loginBackShape.visibility = View.VISIBLE
        binding.titleSignIn.visibility = View.VISIBLE
        binding.cardViewSignIn.visibility = View.VISIBLE
        binding.signupOption.visibility = View.VISIBLE
    }

    private fun hideFormAndShowAnimation(){
        binding.loginNoInternetAnimation.visibility = View.VISIBLE
        binding.loginEnableConnection.visibility = View.VISIBLE
        binding.loginBackShape.visibility = View.GONE
        binding.titleSignIn.visibility = View.GONE
        binding.cardViewSignIn.visibility = View.GONE
        binding.signupOption.visibility = View.GONE
    }

    private fun setupButtonsListeners() {
        binding.moveToSignupSignIn.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_signupFragment) }
        binding.signIn.setOnClickListener { submitForm() }
    }


    private fun submitForm() {
        binding.emailSignIn.helperText = validEmail()
        binding.passwordSignIn.helperText = validPassword()

        val validEmail = binding.emailSignIn.helperText == null
        val validPassword = binding.passwordSignIn.helperText == null

        if (validEmail && validPassword)
            signIn()
    }

    private fun signIn() {
        val email = binding.emailSignInEditText.text.toString()
        val password = binding.passwordSignInEditText.text.toString()
        val viewModel = (requireActivity() as AuthenticationActivity).viewModel
        viewModel.signIn(email, password)
        viewModel.loggingResult.observe(viewLifecycleOwner) {
            when (it) {
                "No such user" -> {
                    binding.emailSignIn.helperText = getString(R.string.no_such_user)
                    binding.passwordSignIn.helperText = ""
                }
                "Something went wrong" -> {
                    binding.emailSignIn.helperText = ""
                    binding.passwordSignIn.helperText = ""
                    Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                }
                "Entered a wrong password" -> {
                    binding.passwordSignIn.helperText = getString(R.string.entered_wrong_password)
                    binding.emailSignIn.helperText = ""
                }
                "Logged in successfully" -> {
                    binding.emailSignIn.helperText = ""
                    binding.passwordSignIn.helperText = ""
                    clearFields()
                    Toast.makeText(requireContext(), getString(R.string.logged_in_successfully), Toast.LENGTH_LONG).show()

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            }
        }
    }

    private fun clearFields() {
        binding.emailSignInEditText.text?.clear()
        binding.passwordSignInEditText.text?.clear()
    }

    private fun validEmail(): String? {
        val emailText = binding.emailSignInEditText.text.toString()
        if (emailText.isBlank())
            return getString(R.string.required)
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
            return getString(R.string.invalid_email)
        return null
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordSignInEditText.text.toString()
        if (passwordText.isBlank())
            return getString(R.string.required)
        if (passwordText.length < 8)
            return getString(R.string.invalid_password)
        return null
    }
}