package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.firestore.DocumentReference
import java.lang.reflect.Method

class FirebaseOperationSource {

    fun writeOnFamily(model: Any,
                      documentReference: DocumentReference,
                      success:Any, failure:
                      Any, navigator:Any){

        documentReference.set(model)
            .addOnSuccessListener {
                (success as Method).invoke(navigator)
            }
            .addOnFailureListener {
                (failure as Method).invoke(navigator)
            }
    }

    fun documentExist(documentReference: DocumentReference,
                      isExist:Any,
                      notExist:Any,
                      navigator: Any){
        documentReference
            .get()
            .addOnSuccessListener {
                if(it.exists())
                    (isExist as Method).invoke(navigator)
                else
                    (notExist as Method).invoke(navigator)
            }
    }

}