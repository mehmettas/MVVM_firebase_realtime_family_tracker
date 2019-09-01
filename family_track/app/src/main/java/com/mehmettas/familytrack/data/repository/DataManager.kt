package com.mehmettas.familytrack.data.repository

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.RemoteDataManager
import com.mehmettas.familytrack.data.remote.model.family.Family

class DataManager(
    private val remoteDataManager: RemoteDataManager): IDataManager {

    override suspend fun createFamily(
        model: Any,
        documentReference: DocumentReference,
        success:Any,
        failure:Any,
        navigator: Any
    ) = remoteDataManager.createFamily(model,documentReference,success,failure,navigator)

    override suspend fun isDocumentExist(
        documentReference: DocumentReference,
        isExist:Any,
        notExist: Any,
        navigator: Any
    ) = remoteDataManager.isDocumentExist(documentReference,isExist,notExist,navigator)
}