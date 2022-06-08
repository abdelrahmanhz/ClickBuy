package com.example.clickbuy.authentication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.R
import com.example.clickbuy.authentication.viewmodel.AuthenticationViewModel
import com.example.clickbuy.authentication.viewmodel.AuthenticationViewModelFactory
import com.example.clickbuy.databinding.ActivityAuthenticationBinding
import com.example.clickbuy.databinding.FragmentSignupBinding
import com.example.clickbuy.models.Favorite
import com.example.clickbuy.models.Repository
import com.example.clickbuy.network.RetrofitClient
import com.example.clickbuy.productdetails.view.TAG
import com.example.clickbuy.productdetails.viewmodel.ProductDetailsViewModel
import com.example.clickbuy.productdetails.viewmodel.ProductDetailsViewModelFactory

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    lateinit var viewModel: AuthenticationViewModel
    private lateinit var modelFactory: AuthenticationViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel() {
        modelFactory = AuthenticationViewModelFactory(
            Repository.getInstance(
                RetrofitClient.getInstance(),
                this
            )
        )
        viewModel = ViewModelProvider(this, modelFactory)
            .get(AuthenticationViewModel::class.java)
    }
}