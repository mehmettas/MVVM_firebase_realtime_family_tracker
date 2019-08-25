package com.mehmettas.familytrack.ui.main

import com.mehmettas.cent.ui.base.BaseActivity
import com.mehmettas.familytrack.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), IMainNavigator {

    private val viewModel by viewModel<MainViewModel>()

    override val layoutId: Int?
        get() = R.layout.activity_main

    override fun initNavigator() {
        viewModel.setNavigator(this)
    }

    override fun initUI() {
        viewModel.firebaseTest("Hi Mate")
    }

    override fun initListener() {

    }
}
