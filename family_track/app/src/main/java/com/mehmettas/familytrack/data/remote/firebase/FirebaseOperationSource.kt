package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.ui.main.IMainNavigator

class FirebaseOperationSource {

    fun writeOnFamily(model: HashMap<String, Any>, documentReference: DocumentReference,navigator: IMainNavigator){
        documentReference.set(model)
            .addOnSuccessListener {
                navigator.writeOnFamilySuccess()
            }
            .addOnFailureListener {
                navigator.writeOnFamilyFailure()
            }
    }


}