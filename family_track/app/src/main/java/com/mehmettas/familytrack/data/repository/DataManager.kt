package com.mehmettas.familytrack.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.RemoteDataManager

class DataManager(
    private val remoteDataManager: RemoteDataManager): IDataManager {

    override suspend fun createFamily(
        model: HashMap<String, Any>,
        documentReference: DocumentReference,
        success:Any,
        failure:Any,
        navigator: Any
    ) = remoteDataManager.createFamily(model,documentReference,success,failure,navigator)

    override suspend fun isDocumentExist(
        documentReference: DocumentReference,
        notExist: Any,
        navigator: Any
    ) = remoteDataManager.isDocumentExist(documentReference,notExist,navigator)
}