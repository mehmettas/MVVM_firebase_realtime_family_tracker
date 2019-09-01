package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.ui.base.BaseActivity
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

    fun documentExist(documentReference: DocumentReference,
                      notExist:Any,
                      navigator: Any){
        documentReference
            .get()
            .addOnSuccessListener {
                if(!it.exists())
                    (notExist as Method).invoke(navigator)
            }
    }


}