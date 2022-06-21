package com.example.clickbuy.orders.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.*
import com.example.clickbuy.util.ConstantsValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "OrdersAddressViewModel"

class OrdersAddressViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo

    private var _address = MutableLiveData<Addresses>()
    var address: LiveData<Addresses> = _address
    fun getAddressOrder(id: String) {
        ConstantsValue.userID
        viewModelScope.launch {
            var addressess: Addresses? = null
            val brandResponse = _iRepo.getAllAddresesForSpecificCustomer(id)
            if (brandResponse.code() == 200) {
                addressess = brandResponse.body()!!
            }
            withContext(Dispatchers.Main) {
                _address.postValue(addressess!!)
                addressess.addresses
            }
        }
    }
}

