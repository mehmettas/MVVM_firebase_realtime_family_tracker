package com.mehmettas.familytrack.data.remote

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener

interface IRemoteDataManager {

    suspend fun setSampleMessage(model:HashMap<String,Any>,listener:ICallbackListener,documentReference: DocumentReference)
    suspend fun createFamily(model:HashMap<String,Any>,listener:ICallbackListener,documentReference: DocumentReference)


}