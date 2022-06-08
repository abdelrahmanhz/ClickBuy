package com.example.clickbuy.authentication.view

import android.os.Bundle
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


class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moveToSignInSignup.setOnClickListener { findNavController().navigate(R.id.action_signupFragment_to_loginFragment) }
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

    private fun signUp()
    {
        val customer = Customer(
            binding.firstNameSignupEditText.text.toString(),
            binding.lastNameSignupEditText.text.toString(),
            binding.emailSignupEditText.text.toString(),
            binding.passwordSignupEditText.text.toString(),
            "+2"+binding.phoneSignupSignupEditText.text.toString(),
        )

        val viewModel = (requireActivity() as AuthenticationActivity).viewModel
        viewModel.registerCustomer(CustomerParent(customer))
        viewModel.isRegistered.observe(viewLifecycleOwner){
            if(it){
                clearFields()
                Toast.makeText(requireContext(), "You have successfully signed up!", Toast.LENGTH_LONG).show()
            }
            else
                Toast.makeText(requireContext(), "Email is already registered, can't sign up!", Toast.LENGTH_LONG).show()
        }
        Toast.makeText(requireContext(), customer.toString(), Toast.LENGTH_LONG).show()
    }

    private fun clearFields() {
        binding.firstNameSignupEditText.text?.clear()
        binding.lastNameSignupEditText.text?.clear()
        binding.emailSignupEditText.text?.clear()
        binding.passwordSignupEditText.text?.clear()
        binding.phoneSignupSignupEditText.text?.clear()
    }

    private fun validFirstName(): String? {
        if(binding.firstNameSignupEditText.text.isNullOrBlank())
            return "Required"
        return null
    }

    private fun validLastName(): String? {
        if(binding.lastNameSignupEditText.text.isNullOrBlank())
            return "Required"
        return null
    }

    private fun validEmail(): String?
    {
        val emailText = binding.emailSignupEditText.text.toString()
        if(emailText.isNullOrBlank())
            return "Required"
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
            return "Invalid Email Address"
        return null
    }



    private fun validPassword(): String?
    {
        val passwordText = binding.passwordSignupEditText.text.toString()
        if(passwordText.isNullOrBlank())
            return "Required"
        if(passwordText.length < 8)
            return "Minimum 8 Character Password"
        return null
    }


    private fun validPasswordConfirm(): String?
    {
        val passwordConfirmText = binding.passwordConfirmSignupSignupEditText.text.toString()
        if(passwordConfirmText.isNullOrBlank())
            return "Required"
        if(passwordConfirmText == binding.passwordConfirmSignupSignupEditText.text.toString())
            return "Passwords don't match"
        return null
    }

    private fun validPhone(): String?
    {
        val phoneText = binding.phoneSignupSignupEditText.text.toString()
        if(phoneText.isNullOrBlank())
            return "Required"
        if(!phoneText.matches(".*[0-9].*".toRegex()))
            return "Must be all Digits"
        if(phoneText.length != 11)
            return "Must be 11 Digits"
        return null
    }
}