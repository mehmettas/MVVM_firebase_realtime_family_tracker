package com.mehmettas.familytrack.data.remote

import com.google.firebase.firestore.DocumentReference

interface IRemoteDataManager {
    suspend fun createFamily(model:HashMap<String,Any>,documentReference: DocumentReference,success:Any,failure:Any,navigator: Any)
}