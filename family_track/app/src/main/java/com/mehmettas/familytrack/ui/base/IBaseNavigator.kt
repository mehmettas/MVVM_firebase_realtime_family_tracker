package com.mehmettas.familytrack.ui.base

interface IBaseNavigator {

    fun showLoading()

    fun hideLoading()

    fun onError(errorMessage:String)

}