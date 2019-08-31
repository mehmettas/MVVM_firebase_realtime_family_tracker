package com.mehmettas.familytrack.data.repository

import android.app.Activity
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.RemoteDataManager
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.ui.login.ILoginNavigator
import com.mehmettas.familytrack.ui.main.IMainNavigator
import io.reactivex.Completable

class DataManager(
    private val remoteDataManager: RemoteDataManager): IDataManager {

    override suspend fun createFamily(
        model: HashMap<String, Any>,
        documentReference: DocumentReference,
        navigator: Any
    ) = remoteDataManager.createFamily(model,documentReference,navigator)
}