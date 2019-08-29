package com.mehmettas.familytrack.data.remote

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.ui.main.IMainNavigator

interface IRemoteDataManager {

    suspend fun createFamily(model:HashMap<String,Any>,documentReference: DocumentReference,navigator: IMainNavigator)
}