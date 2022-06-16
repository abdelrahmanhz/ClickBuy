package com.example.clickbuy.payment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface
import java.lang.IllegalArgumentException

class OrderViewModelFactory(private val _irepo: RepositoryInterface) :
    ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OrderViewModel::class.java)){
            OrderViewModel(_irepo) as T
        }
        else{
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }
}