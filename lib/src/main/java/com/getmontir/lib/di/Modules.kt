package com.getmontir.lib.di

import com.getmontir.lib.data.network.APIService
import com.getmontir.lib.data.repository.VersionRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val remoteModule = module {
    single { APIService.createService(androidContext(), get()) }
}

val repositoryModule = module {
    factory { VersionRepository( androidContext(), get() ) }
}