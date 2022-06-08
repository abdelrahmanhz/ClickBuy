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
import com.example.clickbuy.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.moveToSignupSignIn.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_signupFragment) }
        binding.signIn.setOnClickListener { submitForm() }
    }

    private fun submitForm() {
        binding.emailSignIn.helperText = validEmail()
        binding.passwordSignIn.helperText = validPassword()

        val validEmail = binding.emailSignIn.helperText == null
        val validPassword = binding.passwordSignIn.helperText == null

        if (validEmail && validPassword)
            signUp()
    }

    private fun signUp()
    {

//            binding.emailSignInEditText.text.toString()
//            binding.passwordSignInEditText.text.toString()

        Toast.makeText(requireContext(), binding.emailSignInEditText.text.toString(), Toast.LENGTH_LONG).show()
    }

    private fun validEmail(): String?
    {
        val emailText = binding.emailSignInEditText.text.toString()
        if(emailText.isNullOrBlank())
            return "Required"
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
            return "Invalid Email Address"
        return null
    }

    private fun validPassword(): String?
    {
        val passwordText = binding.passwordSignInEditText.text.toString()
        if(passwordText.isNullOrBlank())
            return "Required"
        if(passwordText.length < 8)
            return "Minimum 8 Character Password"
        return null
    }
}