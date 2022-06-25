package com.example.clickbuy.orders.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.*
import com.example.clickbuy.util.ConstantsValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

const val TAG = "OrdersAddressViewModel"

class OrdersAddressViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo

    private var _address = MutableLiveData<List<CustomerAddress>>()
    var address: LiveData<List<CustomerAddress>> = _address
    fun getAddressOrder() {
        ConstantsValue.userID
        viewModelScope.launch {
            val addressResponse = _iRepo.getAllAddresses()
            if (addressResponse.code() == 200) {
                withContext(Dispatchers.Main) {
                    _address.postValue(addressResponse.body()?.addresses)
                }
            } else {
                _address.postValue(emptyList())
            }

        }
    }
}

