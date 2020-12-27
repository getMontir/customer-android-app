package com.getmontir.customer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getmontir.lib.data.data.entity.UserEntity
import com.getmontir.lib.data.repository.eloquent.AuthRepository
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository
): ViewModel() {

    private val _token = MutableLiveData<ResultWrapper<String>>()
    val token: LiveData<ResultWrapper<String>> get() = _token

    private val _user = MutableLiveData<ResultWrapper<UserEntity>>()
    val user: LiveData<ResultWrapper<UserEntity>> get() = _user

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
    fun loginGoogle(
        token: String,
        fcmToken: String
    ) {
       viewModelScope.launch {
           repo.customerLoginSocial(
               token, fcmToken, "google", "android"
           )
               .collect {
                   _token.value = it
               }
       }
    }

    @InternalCoroutinesApi
    fun loginFacebook(
        token: String,
        fcmToken: String
    ) {
        viewModelScope.launch {
           repo.customerLoginSocial(
               token, fcmToken, "facebook", "android"
           )
               .collect {
                   _token.value = it
               }
       }
    }

    @InternalCoroutinesApi
    fun register(
        name: String,
        phone: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
       viewModelScope.launch {
           repo.customerRegister( name, phone, email, password, passwordConfirmation )
               .collect {
                   _token.value = it
               }
       }
    }

    @InternalCoroutinesApi
    fun registerGoogle(
        token: String,
        fcmToken: String
    ) {
        viewModelScope.launch {
            repo.customerRegisterSocial( token, fcmToken, "google", "android" )
                .collect {
                    _token.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun registerFacebook(
        token: String,
        fcmToken: String
    ) {
        viewModelScope.launch {
            repo.customerRegisterSocial( token, fcmToken, "facebook", "android" )
                .collect {
                    _token.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun profile() {
        viewModelScope.launch {
            repo.profile()
                .collect {
                    _user.value = it
                }
        }
    }
}