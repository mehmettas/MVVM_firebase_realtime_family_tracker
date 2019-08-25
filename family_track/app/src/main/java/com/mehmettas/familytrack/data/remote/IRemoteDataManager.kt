package com.mehmettas.familytrack.data.remote

import com.mehmettas.cent.data.remote.network.ResultWrapper
import io.reactivex.Completable

interface IRemoteDataManager {

    suspend fun setSampleMessage(message:String)

}