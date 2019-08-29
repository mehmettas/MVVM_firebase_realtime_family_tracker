package com.mehmettas.familytrack.data.remote

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.firebase.FirebaseOperationSource
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataManager(
    private val firebaseOperationSource:FirebaseOperationSource
): IRemoteDataManager
{
    override suspend fun createFamily(
        model: HashMap<String, Any>,
        listener: ICallbackListener,
        documentReference: DocumentReference) =
            withContext(Dispatchers.IO) {
                (firebaseOperationSource.writeOnFamily(model,listener,documentReference))
            }

}