package com.example.clickbuy.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.CustomerParent
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "AuthenticationViewModel"

class AuthenticationViewModel(private val repo: RepositoryInterface): ViewModel() {

    private var _isRegistered = MutableLiveData<Boolean>()
    var isRegistered: LiveData<Boolean> = _isRegistered

    fun registerCustomer(customerParent: CustomerParent){
        viewModelScope.launch {
            val response = repo.registerCustomer(customerParent)

            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Log.i(TAG, response.body().toString())
                    _isRegistered.postValue(response.body()?.toString()?.contains("email"))
                }
            } else
                Log.i(com.example.clickbuy.productdetails.viewmodel.TAG, "registerCustomer response failed!")
        }
    }
}