package com.mehmettas.familytrack.ui.main

import com.mehmettas.familytrack.data.remote.model.location.MemberLocation
import com.mehmettas.familytrack.ui.base.IBaseNavigator

interface IMainNavigator: IBaseNavigator {
    fun setUserLocationSuccess()
    fun setUserLocationFailure()
    fun listenLocationsSuccess(data:ArrayList<MemberLocation>)
    fun listenLocationFailures()
    fun memberExist()
    fun memberNotExist()
    fun memberMoveSuccess(items:ArrayList<Any>)
    fun memberMoveFailure()
}