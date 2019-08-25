package com.mehmettas.familytrack.di

import com.mehmettas.familytrack.data.remote.RemoteDataManager
import com.mehmettas.familytrack.data.repository.DataManager
import org.koin.dsl.module.module

val managerModule = module {
    single { DataManager(get())}
    single { RemoteDataManager(get())}
}