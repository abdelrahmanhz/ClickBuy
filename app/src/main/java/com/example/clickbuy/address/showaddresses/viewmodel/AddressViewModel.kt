package com.example.clickbuy.address.showaddresses.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.CustomerAddress
import com.example.clickbuy.models.RepositoryInterface
import com.example.clickbuy.models.ShoppingBag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "AddressViewModel"

class AddressViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo
    private var _addresses = MutableLiveData<List<CustomerAddress>>()
    val addresses: LiveData<List<CustomerAddress>> = _addresses

    fun getAllAddresses() {
        viewModelScope.launch {
            val response = _iRepo.getAllAddresses()
            withContext(Dispatchers.Main) {
                if (response.code() == 200 && response.body()?.addresses != null) {
                    Log.i(TAG, "getAllAddresses in if: response.body()-------> " + response.body())
                    _addresses.postValue(response.body()?.addresses)
                } else {
                    Log.i(TAG, "getAllAddresses in else: response.body()-----> " + response.body())
                    _addresses.postValue(emptyList())
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