package com.mehmettas.familytrack.ui.login

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.repository.DataManager
import com.mehmettas.familytrack.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(dataManager: DataManager): BaseViewModel<ILoginNavigator>(dataManager) {

    fun writeOnFamily(model:HashMap<String,Any>, documentReference: DocumentReference)
    {
        getNavigator().showLoading()
        GlobalScope.launch(Dispatchers.Main) {

            val success = getNavigator()::class.java.interfaces[0].declaredMethods[1]
            val failure = getNavigator()::class.java.interfaces[0].declaredMethods[0]

            withContext(Dispatchers.IO){
                dataManager.createFamily(model,documentReference,success,failure,getNavigator())}
        }
    }

}