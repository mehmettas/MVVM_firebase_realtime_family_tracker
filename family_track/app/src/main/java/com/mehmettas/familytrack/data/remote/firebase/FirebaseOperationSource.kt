package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirebaseOperationSource {

    private val firebaseDatabase:FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun writeMessage(message:String)
    {
        GlobalScope.launch {
            val myRef = firebaseDatabase.getReference("message")
            myRef.setValue(message)
        }

    }



}