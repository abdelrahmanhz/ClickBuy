package com.example.clickbuy.payment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.OrderPojo
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo
    private var _orders = MutableLiveData<OrderPojo>()
    var order: LiveData<OrderPojo> = _orders
    fun postOrder(order: OrderPojo) {
        viewModelScope.launch {
            val orderResponse = _iRepo.postOrders(order)

            if (orderResponse.code() == 200) {
                withContext(Dispatchers.Main) {
                    _orders.postValue(orderResponse.body())
                }
            } else {
                _orders.postValue(orderResponse.body())
            }
        }
    }
}