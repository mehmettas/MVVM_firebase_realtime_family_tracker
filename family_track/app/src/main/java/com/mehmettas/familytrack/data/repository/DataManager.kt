package com.mehmettas.familytrack.data.repository

import com.mehmettas.familytrack.data.remote.RemoteDataManager
import io.reactivex.Completable

class DataManager(
    private val remoteDataManager: RemoteDataManager): IDataManager {

    override suspend fun setSampleMessage(message: String): Completable =
            remoteDataManager.setSampleMessage(message)


}