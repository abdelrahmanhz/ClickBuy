package com.example.clickbuy.search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clickbuy.models.RepositoryInterface
import java.lang.IllegalArgumentException

class SearchViewModelFactory (private val repo: RepositoryInterface) :
    ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            SearchViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("View Model Class Not Found")
        }
    }
}