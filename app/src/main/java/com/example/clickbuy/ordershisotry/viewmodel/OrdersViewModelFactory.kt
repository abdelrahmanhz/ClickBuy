package com.example.clickbuy.ordershisotry.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface

class OrdersViewModelFactory(private val _irepo: RepositoryInterface) :
    ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OrdersViewModel::class.java)){
            OrdersViewModel(_irepo) as T
        }
        else{
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }
}