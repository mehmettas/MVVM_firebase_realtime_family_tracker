package com.mehmettas.familytrack.ui.main

import com.google.firebase.firestore.DocumentReference
import com.mehmettas.cent.ui.base.BaseViewModel
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.data.repository.DataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(dataManager: DataManager): BaseViewModel<IMainNavigator>(dataManager) {

    fun firebaseTest(model:HashMap<String,Any>,listener: ICallbackListener,documentReference: DocumentReference)
    {
        getNavigator().showLoading()
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO){dataManager.setSampleMessage(model,listener,documentReference)}
        }
    }
}