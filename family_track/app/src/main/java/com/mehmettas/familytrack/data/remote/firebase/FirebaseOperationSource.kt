package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mehmettas.cent.data.remote.network.ResultWrapper


class FirebaseOperationSource {

    private val firebaseDatabase: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun writeMessage(model: HashMap<String, Any>, listener: ICallbackListener, documentReference: DocumentReference){

        documentReference.set(model)
            .addOnSuccessListener {
                listener.onSuccess()
                ResultWrapper.Success(it)
            }
            .addOnFailureListener {
                listener.onFailure()
                ResultWrapper.Error(it)
            }

    }

}