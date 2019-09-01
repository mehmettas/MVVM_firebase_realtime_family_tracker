package com.mehmettas.familytrack.ui.login

import com.google.firebase.firestore.FirebaseFirestore
import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.utils.IDGenerator
import com.mehmettas.familytrack.ui.base.BaseActivity
import com.mehmettas.familytrack.ui.main.MainActivity
import com.mehmettas.familytrack.utils.DialogUtils
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

        var lat = "41.2342"
        var lng = "38.2315"




        val memberContent: HashMap<String, Any> = hashMapOf(
            "member_id" to memberId,
            "member_name_surname" to "Mehmet Taş"
        )

        val locationContent: HashMap<String, Any> = hashMapOf(
            "lat" to lat,
            "lng" to lng
        )

        val docReferenceForMember = db.collection("families")
            .document("family_id_${familyId}")
            .collection("members")
            .document("member_id_${memberId}")


        val docReferenceForLocation = db.collection("families")
            .document("family_id_${familyId}")
            .collection("members")
            .document("member_id_${memberId}")
            .collection("location")
            .document("current")

        //viewModel.writeOnFamily(familyContent,docReferenceForFamily)


    }

    override fun initListener() {
        btnCreateFamily.setOnClickListener {
            familyExistRequest()
        }

        btnJoin.setOnClickListener {
            launchActivity<MainActivity> {  }
        }
    }

    private fun familyExistRequest() {
        val documentReference = db.collection("families").document("family_id_${familyId}")
        viewModel.isDocumentExist(documentReference)
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

    override fun documentExist() {
        hideLoading()
        newFamilyId = IDGenerator.GetBase62(6)  // consume new id and resend the request
        familyExistRequest()
    }

    override fun documentNotExist() {
        hideLoading()

        var familyId =  "Z1D9WR"

        val familyContent: HashMap<String, Any> = hashMapOf(
            "family_id" to familyId,
            "family_member_count" to "1"
        )

        val docReferenceForFamily = db.collection("families")
            .document("family_id_Z1D9WR")

        viewModel.writeOnFamily(familyContent,docReferenceForFamily)
    }

    override fun writeOnFamilySuccess() {
        hideLoading()
    }

    override fun writeOnFamilyFailure() {
        hideLoading()
    }


}
