package com.example.clickbuy.search.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.Brands
import com.example.clickbuy.models.Products
import com.example.clickbuy.models.RepositoryInterface
import com.example.clickbuy.models.SubCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "searchViewModel"

class SearchViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private var _products = MutableLiveData<Products>()
    var products: LiveData<Products> = _products

    fun getAllProducts() {
        viewModelScope.launch {
            var products: Products? = null
            val brandResponse =
                repo.getAllProducts("", "", "")
            if (brandResponse.code() == 200) {
                products = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _products.postValue(products!!)
                Log.i(
                    TAG,
                    "getAllProducts View Model--------------------->: ${products.products.size}"
                )
            }
        }
    }
}