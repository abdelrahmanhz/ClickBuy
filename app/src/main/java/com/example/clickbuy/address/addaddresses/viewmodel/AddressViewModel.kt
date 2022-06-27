package com.example.clickbuy.address.addaddresses.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "AddressViewModel"

class AddressViewModel(iRepo: RepositoryInterface) : ViewModel() {
    private val _iRepo: RepositoryInterface = iRepo
    private var _addresses = MutableLiveData<AddressResponseAPI>()
    val addresses: LiveData<AddressResponseAPI> = _addresses

    private var _isAdded = MutableLiveData<Boolean>()
    val isAdded: LiveData<Boolean> = _isAdded

    fun addAddress(address: CustomerAddressUpdate) {
        viewModelScope.launch {
            val response = _iRepo.addAddress(address)
            withContext(Dispatchers.Main) {
                Log.i(TAG, "addAddress: code---------> " + response.code())
                Log.i(TAG, "addAddress: body---------> " + response.body())
                Log.i(TAG, "addAddress: response-----> $response")
                if (response.code() == 201 && response.body() != null)
                    _isAdded.postValue(true)
                else
                    _isAdded.postValue(false)
            }
        }
    }

    fun getAddress(placeName: String) {
        viewModelScope.launch {
            val response = _iRepo.getAddressFromApi(placeName)
            withContext(Dispatchers.Main) {
                if (response.code() == 200) {
                    val list: MutableList<Result> = mutableListOf()
                    if (response.code() == 200 && !response.body()?.results.isNullOrEmpty()) {
                        for (i in 0 until response.body()?.results!!.size) {
                            Log.i(TAG, "getAddress: " + response.body()!!.results[i].components.country)
                            if (response.body()!!.results[i].components.country == "Egypt") {
                                list.add(response.body()!!.results[i])
                            }
                        }
                    }
                    if (list.size > 0)
                    _addresses.postValue(AddressResponseAPI(list, Status(200, "success"), list.size))
                    else
                        _addresses.postValue(AddressResponseAPI(emptyList(), Status(400, "fail"), 0))

                } else {
                    Log.i(TAG, "getAddress: error-------------> " + response.body())
                    _addresses.postValue(AddressResponseAPI(emptyList(), Status(400, "fail"), 0))
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