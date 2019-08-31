package com.mehmettas.familytrack.data.remote

import com.google.firebase.firestore.DocumentReference

object Generic {

    inline fun <reified T> returnMessage(model: HashMap<String, Any>, documentReference: DocumentReference): Any{
        val x = T::class.java.name
        return x
    }
}