package com.mehmettas.familytrack.ui.login

import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.ui.base.BaseActivity
import com.mehmettas.familytrack.utils.DialogUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity(), ILoginNavigator {
    private val viewModel by viewModel<LoginViewModel>()

    override val layoutId: Int?
        get() = R.layout.activity_login

    override fun initNavigator() {
        viewModel.setNavigator(this)
    }

    override fun initUI() {

    }

    override fun initListener() {
        btnCreateFamily.setOnClickListener {
            showFamilyCreationPopup()
        }
        btnJoin.setOnClickListener {

        }
    }

    private fun showFamilyCreationPopup() {

        var familyId = "123456"
        val model = DialogUtils.DialogModel(
            "",
            resources.getString(R.string.familyCreationSuccess),
            resources.getString(R.string.familyIDtext),
            familyId,
            R.drawable.img_family_one
        )
        DialogUtils.showAlertDialog(this,
            model,
            object:DialogUtils.DialogAlertListener{
                override fun onPositiveClick() {
                    textFamilyId.setText(familyId)
                }

                override fun onNegativeClick() {

                }
            })
    }


}
