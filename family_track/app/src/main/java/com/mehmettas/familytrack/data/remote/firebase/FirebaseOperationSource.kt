package com.mehmettas.familytrack.data.remote.firebase

import com.google.firebase.firestore.DocumentReference
import java.lang.reflect.Method


class FirebaseOperationSource {

    fun writeOnFamily(model: HashMap<String, Any>, documentReference: DocumentReference,navigator:Any){

        //var methods = Class.forName(navigator.toString()).declaredMethods

        val arguments = arrayOfNulls<Any>(Int.SIZE_BITS)

        var methods:Array<out Method>?=null
        var nav:Any?=null

        (navigator as Array<out Method>)[0].invoke(this,*arguments)


        navigator.apply {
            methods =  javaClass.interfaces[0].declaredMethods
            nav = javaClass.interfaces[0]
            val instance = nav!!::class.java.newInstance()
            (methods as Array<out Method>)[0].invoke(nav,*arguments)
        }


        val x=  9

        documentReference.set(model)
            .addOnSuccessListener {



            }
            .addOnFailureListener {

            }
    }

    inline fun <reified T: Any> Any.cast(): T{
        return this as T
    }

}