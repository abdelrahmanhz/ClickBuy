package com.example.clickbuy.currency.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.Currency
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "CurrencyViewModel"

class CurrencyViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo
    private var _currencies = MutableLiveData<List<Currency>>()
    val currencies: LiveData<List<Currency>> = _currencies

    fun getCurrencies() {
        viewModelScope.launch {
            var response = _iRepo.getCurrencies()
            withContext(Dispatchers.Main) {
                if (response.code() == 200 && !response.body()?.currencies.isNullOrEmpty()) {
                    _currencies.postValue(response.body()!!.currencies)
                    Log.i(TAG, "getCurrencies: " + response.body())
                } else {
                    Log.i(
                        TAG,
                        "getCurrencies: size----------------> " + response.body()?.currencies?.size
                    )
                    Log.i(TAG, "getCurrencies: response.body()-----> " + response.body())
                    _currencies.postValue(emptyList())
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