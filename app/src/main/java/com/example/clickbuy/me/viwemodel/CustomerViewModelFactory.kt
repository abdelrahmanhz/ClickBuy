package com.example.clickbuy.me.viwemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface
import java.lang.IllegalArgumentException

class CustomerViewModelFactory(private val _irepo: RepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            CustomerViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}