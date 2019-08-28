package com.mehmettas.familytrack.data.remote.firebase

interface ICallbackListener {
    fun serviceOnSuccess()
    fun serviceOnFailure()
}