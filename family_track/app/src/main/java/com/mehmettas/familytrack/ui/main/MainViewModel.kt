package com.mehmettas.familytrack.ui.main

import com.mehmettas.cent.ui.base.BaseViewModel
import com.mehmettas.familytrack.data.repository.DataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(dataManager: DataManager): BaseViewModel<IMainNavigator>(dataManager) {

    fun firebaseTest(message:String)
    {
        getNavigator().showLoading()
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO){dataManager.setSampleMessage(message)}
        }

    }
}