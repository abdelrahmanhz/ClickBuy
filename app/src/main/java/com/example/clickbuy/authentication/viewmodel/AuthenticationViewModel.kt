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
    private var _loggingResult = MutableLiveData<String>()
    var loggingResult: LiveData<String> = _loggingResult

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

    fun signIn(email: String, password: String){
        viewModelScope.launch {
            val response = repo.signIn(email, password)
            withContext(Dispatchers.Main) {
                Log.i(TAG, response)
                _loggingResult.postValue(response)
            }
        }
    }
}