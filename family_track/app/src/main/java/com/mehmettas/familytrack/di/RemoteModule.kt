package com.mehmettas.familytrack.di

import com.mehmettas.familytrack.data.remote.firebase.FirebaseOperationSource
import org.koin.dsl.module.module

val remoteModule = module {
    factory { FirebaseOperationSource() }
}