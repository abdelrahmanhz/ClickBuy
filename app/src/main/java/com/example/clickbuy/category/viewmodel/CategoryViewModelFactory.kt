package com.example.clickbuy.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface
import java.lang.IllegalArgumentException

class CategoryViewModelFactory (private val _irepo: RepositoryInterface) :
ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CategoryViewModel::class.java)){
            CategoryViewModel(_irepo) as T
        }
        else{
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }
}

