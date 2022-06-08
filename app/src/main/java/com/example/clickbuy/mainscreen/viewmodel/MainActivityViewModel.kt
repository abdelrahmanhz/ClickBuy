package com.example.clickbuy.mainscreen.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.CurrencyConverter
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "MainActivityViewModel"

class MainActivityViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo
    private var _currencyConverter = MutableLiveData<CurrencyConverter>()
    val currencyConverter: LiveData<CurrencyConverter> = _currencyConverter

    fun getQualifiedValueCurrency(to: String) {
        viewModelScope.launch {
            var response = _iRepo.getQualifiedValueCurrency(to)
            withContext(Dispatchers.Main) {
                if (response.code() == 200 && response.body()?.success == true) {
                    Log.i(
                        com.example.clickbuy.me.viewmodel.TAG,
                        "getQualifiedValueCurrency: " + response.body()
                    )
                    _currencyConverter.postValue(response.body())
                } else {
                    Log.i(
                        com.example.clickbuy.me.viewmodel.TAG,
                        "getQualifiedValueCurrency: response.body()-----> " + response.body()
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