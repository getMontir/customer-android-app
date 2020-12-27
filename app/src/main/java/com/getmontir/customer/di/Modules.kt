package com.getmontir.customer.di

import com.getmontir.customer.services.FirebaseAuthModule
import com.getmontir.customer.services.FirebaseDatabaseModule
import com.getmontir.customer.services.GoogleClientModule
import com.getmontir.customer.viewmodel.AuthViewModel
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
    factory { FirebaseAuthModule.create() }
    factory { GoogleClientModule.create(androidContext()) }
    factory { FirebaseDatabaseModule.create() }
}

val viewModelModule = module {
    viewModel { SplashViewModel( get() ) }
    viewModel { AuthViewModel( get() ) }
}

val appModules = listOf(
    commonModule,
    remoteModule,
    databaseModules,
    repositoryModule,
    viewModelModule
)