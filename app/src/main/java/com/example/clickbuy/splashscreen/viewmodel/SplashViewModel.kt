package com.example.clickbuy.splashscreen.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickbuy.models.RepositoryInterface
import kotlinx.coroutines.launch

private const val TAG = "SplashViewModel"

class SplashViewModel(private val repo: RepositoryInterface) : ViewModel() {

    fun setupConstantsValue() {
        viewModelScope.launch {
            Log.i(TAG, "setupConstantsValue: ")
            repo.setupConstantsValue()
        }
    }

}