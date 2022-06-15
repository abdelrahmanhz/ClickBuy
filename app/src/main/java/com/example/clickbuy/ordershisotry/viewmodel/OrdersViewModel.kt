package com.example.clickbuy.ordershisotry.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "OrdersViewModel"

class OrdersViewModel(irepo: RepositoryInterface) : ViewModel() {
    private val _irepo: RepositoryInterface = irepo

    private var _order = MutableLiveData<Orders>()
    var order: LiveData<Orders> = _order


    fun getAllOrdersForSpecificCustomer(customerId : String) {
        viewModelScope.launch {
            var orders: Orders? = null
            val orderResponse = _irepo.getAllOrdersForSpecificCustomerById(customerId)
            if (orderResponse.code() == 200) {
                orders = orderResponse.body()!!
            }

            withContext(Dispatchers.Main) {
                _order.postValue(orders!!)
                Log.i(
                    TAG,
                    "getAllBrands View Model--------------------->: $orders"
                )
            }
        }
    }
    }