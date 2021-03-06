package com.getmontir.customer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getmontir.lib.data.repository.eloquent.VersionRepository
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repo: VersionRepository
) : ViewModel() {
    private val _customerVersion = MutableLiveData<ResultWrapper<Boolean>>()
    val customerVersion: LiveData<ResultWrapper<Boolean>> get() = _customerVersion

    @InternalCoroutinesApi
    fun loadCustomerVersion(version: Int ) {
        viewModelScope.launch {
            repo.getVersionCustomer( version )
                .collect {
                    _customerVersion.value = it
                }
        }
    }
}