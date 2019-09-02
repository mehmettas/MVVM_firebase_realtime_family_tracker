package com.mehmettas.familytrack.ui.login

import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.data.remote.model.family.Family
import com.mehmettas.familytrack.data.remote.model.family.Member
import com.mehmettas.familytrack.utils.IDGenerator
import com.mehmettas.familytrack.ui.base.BaseActivity
import com.mehmettas.familytrack.ui.main.MainActivity
import com.mehmettas.familytrack.utils.DialogUtils
import com.mehmettas.familytrack.utils.PrefUtils
import com.mehmettas.familytrack.utils.extensions.launchActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_family_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity(), ILoginNavigator {

    private val viewModel by viewModel<LoginViewModel>()
    private val db = FirebaseFirestore.getInstance()
    private var newFamilyId = IDGenerator.GetBase62(6)
    private var newMemberId = IDGenerator.GetBase62(6)

    override val layoutId: Int?
        get() = R.layout.activity_login

    override fun initNavigator() {
        viewModel.setNavigator(this)
    }

    override fun initUI() {

        if(PrefUtils.isLoggedFamily())
        {
            launchActivity<MainActivity> {  }
            finish()
        }

        val docReferenceForLocation = db.collection("families")
            .document("family_id_${familyId}")
            .collection("members")
            .document("member_id_${memberId}")
            .collection("location")
            .document("current")
    }

    override fun initListener() {
        btnCreateFamily.setOnClickListener {
            familyExistRequest()
        }

        btnJoin.setOnClickListener {
            if(!textFamilyId.text.isNullOrEmpty())
            {
                val familyId = textFamilyId.text
                val documentReference = db.collection("families").document("family_id_${familyId}")
                viewModel.isFamilyExist(documentReference)
            }
        }
    }

    private fun familyExistRequest() {
        val documentReference = db.collection("families").document("family_id_${newFamilyId}")
        viewModel.isDocumentExist(documentReference)
    }

    private fun createFamily() {
        val family = Family(newFamilyId, 1)

        val docReferenceForFamily = db.collection("families")
            .document("family_id_${family.family_id}")

        viewModel.writeOnFamily(family, docReferenceForFamily)
        createMember(family)
    }

    private fun createMember(family: Family)
    {
        val member = Member(newMemberId,"Mehmet","")
        val docReferenceForMember = db.collection("families")
            .document("family_id_${family.family_id}")
            .collection("family_member")
            .document("member_id_${member.member_id}")
        viewModel.writeOnFamily(member,docReferenceForMember)

        PrefUtils.createFamily(Gson().toJson(family),Gson().toJson(member))
        showFamilyCreationPopup(family.family_id)
    }

    override fun familyExist() {
        hideLoading()
        Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
    }

    override fun familyNotExist() {
        hideLoading()
        Toast.makeText(this,"Can't find any family with given id",Toast.LENGTH_LONG).show()
    }

    override fun documentExist() {
        hideLoading()
        newFamilyId = IDGenerator.GetBase62(6)
        newMemberId = IDGenerator.GetBase62(6)
        familyExistRequest() // consume new id and resend the request
    }

    override fun documentNotExist() {
        hideLoading()
        createFamily()
    }

    override fun writeOnFamilySuccess() {
        hideLoading()
    }

    override fun writeOnFamilyFailure() {
        hideLoading()
    }

    private fun showFamilyCreationPopup(familyId:String) {

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
}
