package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirebaseOperationSource {

    private val firebaseDatabase:FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun writeMessage(message:String)
    {
        val family = hashMapOf(
            "id" to "12345",
            "message" to message
        )

        GlobalScope.launch {
            firebaseDatabase.collection("families").document("all")
                .set(family)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }

    }


}