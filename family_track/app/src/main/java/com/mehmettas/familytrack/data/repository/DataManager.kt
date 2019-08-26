package com.mehmettas.familytrack.data.repository

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.RemoteDataManager
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import io.reactivex.Completable

class DataManager(
    private val remoteDataManager: RemoteDataManager): IDataManager {

    override suspend fun setSampleMessage(
        model: HashMap<String, Any>,
        listener: ICallbackListener,
        documentReference: DocumentReference
    ) = remoteDataManager.setSampleMessage(model,listener,documentReference)
}