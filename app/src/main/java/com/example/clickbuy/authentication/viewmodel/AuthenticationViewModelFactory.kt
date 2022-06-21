package com.example.clickbuy.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface

class AuthenticationViewModelFactory(private val repo: RepositoryInterface):
ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)){
            AuthenticationViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }
}