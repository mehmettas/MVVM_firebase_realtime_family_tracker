package com.mehmettas.familytrack.ui.login

import com.mehmettas.familytrack.ui.base.IBaseNavigator

interface ILoginNavigator: IBaseNavigator {
    fun writeOnFamilySuccess()
    fun writeOnFamilyFailure()
    fun documentNotExist()
}