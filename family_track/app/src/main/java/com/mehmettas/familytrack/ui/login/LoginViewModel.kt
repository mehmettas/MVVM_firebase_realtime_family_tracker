package com.mehmettas.familytrack.ui.login

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.repository.DataManager
import com.mehmettas.familytrack.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mehmettas.familytrack.data.remote.Generic.returnMessage

class LoginViewModel(dataManager: DataManager): BaseViewModel<ILoginNavigator>(dataManager) {


    fun writeOnFamily(model:HashMap<String,Any>, documentReference: DocumentReference)
    {
        getNavigator().showLoading()
        GlobalScope.launch(Dispatchers.Main) {
            val navigator = returnMessage<ILoginNavigator>(model,documentReference)
            withContext(Dispatchers.IO){
                dataManager.createFamily(model,documentReference,getNavigator())}
        }
    }


}