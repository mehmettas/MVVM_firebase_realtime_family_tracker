package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.model.family.Family
import com.mehmettas.familytrack.data.remote.model.family.Member
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

    fun retriveFamily(documentReference: DocumentReference,
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

    fun retrieveMembers(collectionReference: CollectionReference,
                      membersRetrieved:Any,
                      membersNotRetrieved:Any,
                      navigator: Any){
        collectionReference
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    val members = arrayListOf<Member>()
                    it.result!!.iterator().forEach {
                        members.add(it.toObject(Member::class.java))
                    }
                    (membersRetrieved as Method).invoke(navigator,members)
                }
                if(it.isCanceled)
                {
                    (membersNotRetrieved as Method).invoke(navigator)
                }
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