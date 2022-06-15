package com.example.clickbuy.splashscreen.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface

class SplashViewModelFactory(private val repo: RepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            SplashViewModel(repo) as T
        } else {
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }

}