package com.example.clickbuy.productdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface

class ProductDetailsViewModelFactory(private val repo: RepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)){
            ProductDetailsViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }
}