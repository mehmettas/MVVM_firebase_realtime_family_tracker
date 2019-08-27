package com.mehmettas.familytrack.ui.login

import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.ui.base.BaseActivity

import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity(), ILoginNavigator {
    private val viewModel by viewModel<LoginViewModel>()

    override val layoutId: Int?
        get() = R.layout.activity_main

    override fun initNavigator() {
        viewModel.setNavigator(this)
    }

    override fun initUI() {

    }

    override fun initListener() {

    }
}
