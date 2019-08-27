package com.mehmettas.familytrack.ui.main

import com.google.firebase.firestore.FirebaseFirestore
import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), IMainNavigator, ICallbackListener {

    private val viewModel by viewModel<MainViewModel>()

    override val layoutId: Int?
        get() = R.layout.activity_main

    override fun initNavigator() {
        viewModel.setNavigator(this)
    }

    override fun initUI() {

        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("families").document("all")

        val family: HashMap<String, Any> = hashMapOf(
            "id" to "12345",
            "message" to ""
        )
        viewModel.firebaseTest(family,this,documentReference)

    }

    override fun initListener() {
    }

    override fun onSuccess() {
        val a = 0
    }

    override fun onFailure() {
        val a = 0
    }
}
