package com.example.clickbuy.bag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.RepositoryInterface
import com.example.clickbuy.models.ShoppingBag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "BagViewModel"

class BagViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo
    private var _shoppingBag = MutableLiveData<ShoppingBag>()
    val shoppingBag: LiveData<ShoppingBag> = _shoppingBag

    fun getAllItemsInBag() {
        viewModelScope.launch {
            val response = _iRepo.getAllItemInBag()
            withContext(Dispatchers.Main) {
                if (response.code() == 200 && response.body()?.draft_order != null) {
                    Log.i(
                        com.example.clickbuy.me.viewmodel.TAG,
                        "getAllItemsInBag: " + response.body()
                    )
                    _shoppingBag.postValue(response.body())
                } else {
                    Log.i(
                        com.example.clickbuy.me.viewmodel.TAG,
                        "getAllItemsInBag: response.body()-----> " + response.body()
                    )
                }
            }
        }

    }


    init {
        Log.i(TAG, "init: ")
    }


    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared: ")
    }

}