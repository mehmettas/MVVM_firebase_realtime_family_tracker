package com.mehmettas.familytrack.data.remote

import android.app.Activity
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.ui.login.ILoginNavigator
import com.mehmettas.familytrack.ui.main.IMainNavigator

interface IRemoteDataManager {
    suspend fun createFamily(model:HashMap<String,Any>,documentReference: DocumentReference,navigator: Any)
}