package com.mehmettas.familytrack.ui.login

import com.mehmettas.familytrack.data.remote.model.family.Family
import com.mehmettas.familytrack.ui.base.IBaseNavigator

interface ILoginNavigator: IBaseNavigator {
    fun writeOnFamilySuccess()
    fun writeOnFamilyFailure()
    fun documentNotExist()
    fun documentExist()
    fun familyExist(familyData:Family)
    fun familyNotExist()
}