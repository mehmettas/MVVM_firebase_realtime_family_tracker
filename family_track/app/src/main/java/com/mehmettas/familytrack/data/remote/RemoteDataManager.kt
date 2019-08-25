package com.mehmettas.familytrack.data.remote

import com.mehmettas.familytrack.data.remote.firebase.FirebaseOperationSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataManager(
    private val firebaseOperationSource:FirebaseOperationSource
): IRemoteDataManager
{
    override suspend fun setSampleMessage(message: String) =
        withContext(Dispatchers.IO) {
            firebaseOperationSource.writeMessage(message)
        }


}