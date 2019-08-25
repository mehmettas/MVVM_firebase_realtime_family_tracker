package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable

class FirebaseOperationSource {

    private val firebaseDatabase:FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun writeMessage(message:String) = Completable.create { emitter ->
        val myRef = firebaseDatabase.getReference("message")
        myRef.setValue(message).addOnCompleteListener {
            if (!emitter.isDisposed){
                if (it.isSuccessful){
                    emitter.onComplete()
                }
                else{
                    emitter.onError(it.exception!!)
                }
            }
        }
    }


}