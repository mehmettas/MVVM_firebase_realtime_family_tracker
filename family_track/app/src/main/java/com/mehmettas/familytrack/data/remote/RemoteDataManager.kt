package com.mehmettas.familytrack.data.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.firebase.FirebaseOperationSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataManager(
    private val firebaseOperationSource:FirebaseOperationSource
): IRemoteDataManager
{
    override suspend fun createFamily(
        model: HashMap<String, Any>,
        documentReference: DocumentReference,
        success:Any,
        failure:Any,
        navigator: Any
    ) =
            withContext(Dispatchers.IO) {
                (firebaseOperationSource.writeOnFamily(model,documentReference,success,failure,navigator))
            }

    override suspend fun isDocumentExist(
        documentReference: DocumentReference,
        isExist:Any,
        notExist: Any,
        navigator: Any
    ) =
        withContext(Dispatchers.IO) {
            (firebaseOperationSource.documentExist(documentReference,isExist,notExist,navigator))
        }

}