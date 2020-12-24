package com.getmontir.customer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getmontir.lib.data.repository.eloquent.AuthRepository
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository
): ViewModel() {

    private val _token = MutableLiveData<ResultWrapper<String>>()
    val token: LiveData<ResultWrapper<String>> get() = _token

    @InternalCoroutinesApi
    fun login(
        email: String?,
        password: String?
    ) {
        viewModelScope.launch {
            repo.customerLogin(email,password)
                .collect {
                    _token.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun loginSocial(
        token: String,
        fcmToken: String,
        channel: String,
        device: String
    ) {
       viewModelScope.launch {
           repo.customerLoginSocial(
               token, fcmToken, channel, device
           )
               .collect {
                   _token.value = it
               }
       }
    }
}