package com.getmontir.customer.di

import com.getmontir.customer.data.network.APIService
import com.getmontir.customer.data.repository.VersionRepository
import com.getmontir.customer.presentation.ui.main.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val remoteModule = module {
    single { APIService.createService() }
}

val repositoryModule = module {
    factory { VersionRepository( androidContext(), get() ) }
}

val viewModelModule = module {
    viewModel { SplashViewModel( get() ) }
}

val appModules = listOf(
    remoteModule,
    repositoryModule,
    viewModelModule
)