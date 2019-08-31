package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.firestore.DocumentReference
import java.lang.reflect.Method

class FirebaseOperationSource {

    fun writeOnFamily(model: HashMap<String, Any>,
                      documentReference: DocumentReference,
                      success:Any,failure:
                      Any,navigator:Any){

        documentReference.set(model)
            .addOnSuccessListener {
                (success as Method).invoke(navigator)
            }
            .addOnFailureListener {
                (failure as Method).invoke(navigator)
            }
    }


}