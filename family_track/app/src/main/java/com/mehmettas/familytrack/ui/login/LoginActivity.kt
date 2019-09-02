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
import com.mehmettas.familytrack.utils.AppConstants.FAMILIES
import com.mehmettas.familytrack.utils.AppConstants.FAMILY_ID
import com.mehmettas.familytrack.utils.AppConstants.FAMILY_MEMBERS
import com.mehmettas.familytrack.utils.AppConstants.MEMBER_ID
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
    private var retrievedfamilyData:Family?=null
    private var retrievedMemberData:Member?=null

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

        val docReferenceForLocation = db.collection(FAMILIES)
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
            retrieveFamily()
        }
    }

    private fun retrieveFamily() {
        if (!textFamilyId.text.isNullOrEmpty()) {
            val familyId = textFamilyId.text
            val documentReference = db.collection(FAMILIES).document(FAMILY_ID + familyId)
            viewModel.retrieveFamily(documentReference)
        }
    }

    private fun retrieveMembers(){
        val familyId = textFamilyId.text
        val collectionReference = db.collection(FAMILIES).document(FAMILY_ID + familyId).collection(FAMILY_MEMBERS)
        viewModel.retrieveFamilyMembers(collectionReference)
    }

    private fun familyExistRequest() {
        val documentReference = db.collection(FAMILIES).document(FAMILY_ID+newFamilyId)
        viewModel.isDocumentExist(documentReference)
    }

    private fun createFamily() {
        val family = Family(newFamilyId, 1)
        val docReferenceForFamily = db.collection(FAMILIES)
            .document(FAMILY_ID+family.family_id)

        viewModel.writeOnFamily(family, docReferenceForFamily)
        createMember(family)
    }

    private fun createMember(family: Family)
    {
        val member = Member(newMemberId,"Mehmet","")
        val docReferenceForMember = db.collection(FAMILIES)
            .document(FAMILY_ID+family.family_id)
            .collection(FAMILY_MEMBERS)
            .document(MEMBER_ID+member.member_id)
        viewModel.writeOnFamily(member,docReferenceForMember)

        PrefUtils.createFamily(Gson().toJson(family),Gson().toJson(member))
        showFamilyCreationPopup(family.family_id)
    }

    override fun familyExist(familyData:Family) {
        hideLoading()
        retrievedfamilyData = familyData
        retrieveMembers()
    }

    override fun familyNotExist() {
        hideLoading()
        Toast.makeText(this,"Can't find any family with given id",Toast.LENGTH_LONG).show()
    }

    override fun membersRetrieved(members: ArrayList<Member>) {
        hideLoading()
        retrievedMemberData = members[0]
        PrefUtils.createFamily(Gson().toJson(retrievedfamilyData),Gson().toJson(retrievedMemberData))
    }

    override fun membersNotRetrieved() {
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
