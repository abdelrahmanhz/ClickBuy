package com.example.clickbuy.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface
import java.lang.IllegalArgumentException

class OrdersAddressViewModelFactory(private val _irepo: RepositoryInterface) :
    ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OrdersAddressViewModel::class.java)){
            OrdersAddressViewModel(_irepo) as T
        }
        else{
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }
}
