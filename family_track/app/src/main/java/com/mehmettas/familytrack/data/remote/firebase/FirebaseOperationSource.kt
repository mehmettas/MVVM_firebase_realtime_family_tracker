package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.model.family.Family
import com.mehmettas.familytrack.data.remote.model.family.Member
import com.mehmettas.familytrack.data.remote.model.location.MemberLocation
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

    fun retrieveFamily(documentReference: DocumentReference,
                       isExist:Any,
                       notExist:Any,
                       navigator: Any){
        documentReference
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    val document = it.result!!.toObject(Member::class.java)
                    (isExist as Method).invoke(navigator,document)
                }
                if(it.isCanceled)
                {
                    (notExist as Method).invoke(navigator)
                }
            }
    }

    fun retrieveMembers(collectionReference: CollectionReference,
                        familyId:String,
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
                        if(it.toObject(Member::class.java).family_id==familyId)
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

    fun setCurrentUserLocation(modelData:Any,
                               documentReference: DocumentReference,
                               userLocationSetSuccess:Any,
                               userLocationSetFailure:Any,
                               navigator: Any){
        documentReference.set(modelData)
            .addOnSuccessListener {
                (userLocationSetSuccess as Method).invoke(navigator)
            }
            .addOnFailureListener {
                (userLocationSetFailure as Method).invoke(navigator)
            }
    }

    fun listenForOtherFamilyMembers(documentReferences: ArrayList<DocumentReference>,
                                    listenSuccess:Any,
                                    listenFailure:Any,
                                    navigator: Any)
    {
        val listLocation:ArrayList<MemberLocation> = arrayListOf()

        for(i in 0 until documentReferences.size)
        {
            documentReferences[i]
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        var item = it.result!!.toObject(MemberLocation::class.java)
                        listLocation.add(item!!)

                        if (listLocation.size==documentReferences.size)
                            (listenSuccess as Method).invoke(navigator,listLocation)
                    }
                    if(it.isCanceled)
                    {
                        (listenFailure as Method).invoke(navigator)
                    }
                }
        }




    }




}