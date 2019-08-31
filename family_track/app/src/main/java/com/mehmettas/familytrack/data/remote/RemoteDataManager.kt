package com.mehmettas.familytrack.data.remote

import android.app.Activity
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.firebase.FirebaseOperationSource
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.ui.login.ILoginNavigator
import com.mehmettas.familytrack.ui.main.IMainNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataManager(
    private val firebaseOperationSource:FirebaseOperationSource
): IRemoteDataManager
{
    override suspend fun createFamily(
        model: HashMap<String, Any>,
        documentReference: DocumentReference,
        navigator: Any
    ) =
            withContext(Dispatchers.IO) {
                (firebaseOperationSource.writeOnFamily(model,documentReference,navigator))
            }

}