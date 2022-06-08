package com.example.clickbuy.payment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.Coupon
import com.example.clickbuy.models.CurrencyConverter
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "PaymentViewModel"

class PaymentViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo
    private var _validationCoupon = MutableLiveData<Coupon>()
    val validationCoupon: LiveData<Coupon> = _validationCoupon

    fun validateCoupons(code: String) {
        viewModelScope.launch {
            var response = _iRepo.validateCoupons(code)
            withContext(Dispatchers.Main) {
                if (response.code() == 200 && !response.body()?.discount_code?.code.isNullOrEmpty()) {
                    Log.i(
                        com.example.clickbuy.me.viewmodel.TAG,
                        "validateCoupons: " + response.body()
                    )
                    _validationCoupon.postValue(response.body())
                } else {
                    Log.i(
                       TAG,
                        "validateCoupons: response.body()-----> " + response.body()
                    )
                    _validationCoupon.postValue( response.body())
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