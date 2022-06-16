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
    private val TAG = "OrderViewModel"
    private var _orders = MutableLiveData<OrderPojo>()
    var order: LiveData<OrderPojo> = _orders
    fun postOrder(order: OrderPojo) {
        viewModelScope.launch {
            var orders: OrderPojo? = null
            val orderResponse = _iRepo.postOrders(order)
            Log.i(TAG, "order: ${orderResponse.code()}")

            if (orderResponse.code() == 200) {
                orders = orderResponse.body()!!
                withContext(Dispatchers.Main) {
                    _orders.postValue(orders!!)
                    orders.order
                }
            } else {
                Log.i(TAG, "postOrder: ------> " + order)
                Log.i(TAG, "postOrder: ------> " + orderResponse)
            }
        }
    }
}