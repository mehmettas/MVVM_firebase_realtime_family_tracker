package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.model.family.Family
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
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    val document = it.result!!.toObject(Family::class.java)
                    (isExist as Method).invoke(navigator,document)
                }
                if(it.isCanceled)
                {
                    (notExist as Method).invoke(navigator)
                }
            }
    }

}