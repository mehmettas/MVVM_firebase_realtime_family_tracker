package com.mehmettas.familytrack.data.remote

import com.google.android.gms.common.api.Response
import com.mehmettas.cent.data.remote.network.ResultWrapper
import com.mehmettas.familytrack.data.remote.firebase.FirebaseOperationSource
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class RemoteDataManager(
    private val firebaseOperationSource:FirebaseOperationSource
): IRemoteDataManager
{
    override suspend fun setSampleMessage(message: String) =
        withContext(Dispatchers.IO) {
            firebaseOperationSource.writeMessage(message)
        }


}