package com.mehmettas.familytrack.data.remote

import io.reactivex.Completable

interface IRemoteDataManager {

    suspend fun setSampleMessage(message:String): Completable

}