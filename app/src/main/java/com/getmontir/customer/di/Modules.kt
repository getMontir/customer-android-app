package com.getmontir.customer.di

import com.getmontir.customer.viewmodel.SignInViewModel
import com.getmontir.customer.viewmodel.SplashViewModel
import com.getmontir.lib.di.databaseModules
import com.getmontir.lib.di.remoteModule
import com.getmontir.lib.di.repositoryModule
import com.getmontir.lib.presentation.utils.SessionManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    single { SessionManager(androidContext(), "getMontirCustomerPref") }
}

val viewModelModule = module {
    viewModel { SplashViewModel( get() ) }
    viewModel { SignInViewModel(get()) }
}

val appModules = listOf(
    commonModule,
    remoteModule,
    databaseModules,
    repositoryModule,
    viewModelModule
)