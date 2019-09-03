package com.mehmettas.familytrack.ui.main

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.data.remote.model.location.MemberLocation
import com.mehmettas.familytrack.data.repository.DataManager
import com.mehmettas.familytrack.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(dataManager: DataManager): BaseViewModel<IMainNavigator>(dataManager) {

    fun setCurrrentUserLocation(model:Any,
                                documentReference: DocumentReference)
    {
        GlobalScope.launch(Dispatchers.Main) {

            val userLocationSetSuccess = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "setUserLocationSuccess" }!!
            val userLocationSetFailure = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "setUserLocationFailure" }!!

            withContext(Dispatchers.IO){
                dataManager.setCurrentUserLocation(model,documentReference,userLocationSetSuccess,userLocationSetFailure,getNavigator())}
        }
    }

    fun listenForFamilyMembers(documentReferences: ArrayList<DocumentReference>)
    {
        GlobalScope.launch(Dispatchers.Main) {

            val listenSucces = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "listenLocationsSuccess" }!!
            val listenFailure = getNavigator()::class.java.interfaces[0].declaredMethods.find {it.name == "listenLocationFailures" }!!

            withContext(Dispatchers.IO){
                dataManager.listenForFamilyMemberLocations(documentReferences,listenSucces,listenFailure,getNavigator())}
        }
    }
}