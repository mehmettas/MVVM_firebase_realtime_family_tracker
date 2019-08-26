package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.firestore.DocumentReference

class FirebaseOperationSource {

    fun writeMessage(model: HashMap<String, Any>, listener: ICallbackListener, documentReference: DocumentReference){

        documentReference.set(model)
            .addOnSuccessListener {
                listener.onSuccess()
            }
            .addOnFailureListener {
                listener.onFailure()
            }
    }

}