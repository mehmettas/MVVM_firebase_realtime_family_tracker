package com.mehmettas.familytrack.ui.login

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.model.family.Family
import com.mehmettas.familytrack.data.repository.DataManager
import com.mehmettas.familytrack.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(dataManager: DataManager): BaseViewModel<ILoginNavigator>(dataManager) {

    fun writeOnFamily(model:Any, documentReference: DocumentReference)
    {
        getNavigator().showLoading()
        GlobalScope.launch(Dispatchers.Main) {

            val success = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "writeOnFamilySuccess" }!!
            val failure = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "writeOnFamilyFailure" }!!
            withContext(Dispatchers.IO){
                dataManager.writeOnFamily(model,documentReference,success,failure,getNavigator())}
        }
    }

    fun isDocumentExist(documentReference: DocumentReference)
    {
        getNavigator().showLoading()
        GlobalScope.launch(Dispatchers.Main) {

            val isNotExist = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "documentNotExist" }!!
            val isExist = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "documentExist" }!!

            withContext(Dispatchers.IO){
                dataManager.isDocumentExist(documentReference,isExist,isNotExist,getNavigator())}
        }
    }

    fun retrieveFamily(documentReference: DocumentReference)
    {
        getNavigator().showLoading()
        GlobalScope.launch(Dispatchers.Main) {

            val isNotExist = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "familyNotExist" }!!
            val isExist = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "familyExist" }!!

            withContext(Dispatchers.IO){
                dataManager.retriveFamily(documentReference,isExist,isNotExist,getNavigator())}
        }
    }

    fun retrieveFamilyMembers(collectionReference: CollectionReference,familyID:String)
    {
        getNavigator().showLoading()
        GlobalScope.launch(Dispatchers.Main) {

            val membersRetrieved = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "membersRetrieved" }!!
            val membersNotRetrieved = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "membersNotRetrieved" }!!

            withContext(Dispatchers.IO){
                dataManager.retriveFamilyMembers(collectionReference,familyID,membersRetrieved,membersNotRetrieved,getNavigator())}
        }
    }
}