package com.mehmettas.familytrack.ui.main

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.data.repository.DataManager
import com.mehmettas.familytrack.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(dataManager: DataManager): BaseViewModel<IMainNavigator>(dataManager) {

    fun writeOnFamily(model:HashMap<String,Any>, documentReference: DocumentReference)
    {
        getNavigator().showLoading()
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO){ dataManager.createFamily(model,documentReference,getNavigator())}
        }
    }
}