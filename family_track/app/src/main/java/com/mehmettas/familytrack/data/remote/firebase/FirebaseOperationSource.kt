package com.mehmettas.familytrack.data.remote.firebase

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
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

    fun documentExist(collectionReference: CollectionReference,
                      document:String,
                      notExist:Any,
                      navigator: Any){
        collectionReference.whereEqualTo(document,document).limit(1)
            .get()
            .addOnCompleteListener(OnCompleteListener {
                if(it.result!!.documents.size<1)
                    (notExist as Method).invoke(navigator)
            })
    }


}