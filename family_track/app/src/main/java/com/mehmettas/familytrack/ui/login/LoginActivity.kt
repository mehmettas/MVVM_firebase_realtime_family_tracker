package com.mehmettas.familytrack.ui.login

import com.google.firebase.firestore.FirebaseFirestore
import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.utils.IDGenerator
import com.mehmettas.familytrack.ui.base.BaseActivity
import com.mehmettas.familytrack.ui.main.MainActivity
import com.mehmettas.familytrack.utils.DialogUtils
import com.mehmettas.familytrack.utils.extensions.launchActivity
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


        val db = FirebaseFirestore.getInstance()

        var familyId = IDGenerator.GetBase62(6)
        var memberId = IDGenerator.GetBase62(6)


        var lat = "41.2342"
        var lng = "38.2315"

        val memberContent: HashMap<String, Any> = hashMapOf(
            "member_id" to memberId,
            "member_name_surname" to "Mehmet Taş"
        )

        val docReferenceForMember = db.collection("families")
            .document("family_id_${familyId}")
            .collection("members")
            .document("member_id_${memberId}")

        viewModel.writeOnFamily(memberContent,docReferenceForMember)

    }

    override fun initListener() {
        btnCreateFamily.setOnClickListener {
            createFamily()
        }

        btnJoin.setOnClickListener {
            launchActivity<MainActivity> {  }
        }
    }

    private fun showFamilyCreationPopup() {

        var familyId = "123456"
        val model = DialogUtils.DialogModel(
            "",
            resources.getString(R.string.familyCreationSuccess),
            resources.getString(R.string.familyIDtext),
            familyId,
            "",
            "",
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

    private fun createFamily()
    {
        showFamilyCreationPopup()
    }

    override fun writeOnFamilySuccess() {
        val x = 0
    }

    override fun writeOnFamilyFailure() {
        val x = 0
    }
}
