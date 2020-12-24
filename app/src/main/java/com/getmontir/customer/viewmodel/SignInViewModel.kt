package com.getmontir.customer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getmontir.lib.data.data.entity.ProvinceEntity
import com.getmontir.lib.data.repository.eloquent.ProvinceRepository
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignInViewModel(
    private val repo: ProvinceRepository
) : ViewModel() {

    private val _provinces = MutableLiveData<ResultWrapper<List<ProvinceEntity>>>()
    val provinces: LiveData<ResultWrapper<List<ProvinceEntity>>> get() = _provinces

    @InternalCoroutinesApi
    fun loadProvince() {
        viewModelScope.launch {
            repo.provinces.collect {
                _provinces.value = it
            }
        }
    }
}