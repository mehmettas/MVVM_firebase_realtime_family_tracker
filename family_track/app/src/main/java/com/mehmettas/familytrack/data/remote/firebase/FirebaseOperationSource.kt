package com.mehmettas.familytrack.data.remote.firebase

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
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
            myRef.setValue(message).addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful){

                }
                else{

                }
            })
        }

    }


}