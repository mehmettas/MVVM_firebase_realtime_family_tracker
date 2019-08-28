package com.mehmettas.familytrack.ui.main

import android.content.res.Resources
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.mehmettas.familytrack.ui.main.FamilyAdapter.FamilyAdapter
import com.mehmettas.familytrack.utils.DialogUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), IMainNavigator, ICallbackListener, OnMapReadyCallback,
    FamilyAdapter.FamilyAdapterListener {
    private val viewModel by viewModel<MainViewModel>()

    private val familyMembersAdapter by lazy {
        FamilyAdapter(arrayListOf(),this)
    }

    override val layoutId: Int?
        get() = R.layout.activity_main

    override fun initNavigator() {
        viewModel.setNavigator(this)
    }

    override fun initUI() {
        observeViewModel()
        initMap()
        rvMemberList.setHasFixedSize(true)
        rvMemberList.adapter = familyMembersAdapter
        setDummy()

        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("families").document("all")

        val family: HashMap<String, Any> = hashMapOf(
            "id" to "12345",
            "message" to ""
        )
        viewModel.firebaseTest(family,this,documentReference)
    }

    private fun initMap()
    {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapMain) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        try{
            val success:Boolean = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,R.raw.style_json
                )
            )
        }
        catch (e: Resources.NotFoundException) {

        }
        val location = LatLng(40.9882728, 29.0343543)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,18.0f));

    }

    private fun observeViewModel()
    {

    }

    private fun setDummy()
    {
        var values:ArrayList<String> = arrayListOf()
        for(x in 0 .. 10)
        {
            values.add(x.toString())
        }
        familyMembersAdapter.addData(values)
    }

    override fun initListener() {
        btnGetInfo.setOnClickListener {
            showFamilyInfoPopup()
        }
    }

    override fun onFamilyMemberSelected(item: String) {
        Toast.makeText(this,"new regular member",Toast.LENGTH_LONG).show()
    }

    override fun onNewFamilyMemberSelected() {
        Toast.makeText(this,"new member",Toast.LENGTH_LONG).show()
    }

    override fun onSuccess() {
    }

    override fun onFailure() {
    }


    private fun showFamilyInfoPopup() {

        var familyId = "123456"
        var memberId = "123456-3"
        var memberCount = "6"

        val model = DialogUtils.DialogModel(
            "",
            "",
            "",
            familyId,
            memberId,
            memberCount,
            R.drawable.img_family_two,
            true
        )
        DialogUtils.showAlertDialog(this,
            model,
            object: DialogUtils.DialogAlertListener{
                override fun onPositiveClick() {
                }

                override fun onNegativeClick() {

                }
            })
    }
}
