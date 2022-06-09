package com.example.clickbuy.bag.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface
import java.lang.IllegalArgumentException

class BagViewModelFactory(private val _iRepo: RepositoryInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BagViewModel::class.java)) {
            BagViewModel(_iRepo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}