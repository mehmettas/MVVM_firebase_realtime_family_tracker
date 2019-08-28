package com.mehmettas.familytrack.ui.main

import android.content.res.Resources
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.maps.model.*
import com.mehmettas.familytrack.data.remote.model.MarkerData
import com.mehmettas.familytrack.ui.main.FamilyAdapter.FamilyAdapter
import com.mehmettas.familytrack.utils.DialogUtils
import com.mehmettas.familytrack.utils.extensions.createMarker
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.mehmettas.familytrack.utils.extensions.zoomToAllMarkers


class MainActivity : BaseActivity(), IMainNavigator, ICallbackListener, OnMapReadyCallback,
    FamilyAdapter.FamilyAdapterListener {
    private val viewModel by viewModel<MainViewModel>()
    private var markersData:ArrayList<MarkerData>?= arrayListOf()
    private var markers:ArrayList<Marker> = arrayListOf()

    private val familyMembersAdapter by lazy {
        FamilyAdapter(arrayListOf(),this)
    }

    override val layoutId: Int?
        get() = R.layout.activity_main

    override fun initNavigator() {
        viewModel.setNavigator(this)
    }

    override fun initUI() {
        rvMemberList.setHasFixedSize(true)
        rvMemberList.adapter = familyMembersAdapter
        observeViewModel()
        setDummy()
        initMap()

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

        for(x in 0 .. markersData?.size!!-1){
            markers.add(createMarker(this,googleMap,markersData!![x].lat,markersData!![x].lng,R.drawable.ic_sample_member))
        }

        zoomToAllMarkers(googleMap,markers)

        /*val location = LatLng(40.9882728, 29.0343543)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,18.0f));*/
    }


    private fun observeViewModel()
    {}

    private fun setDummy()
    {
        var values:ArrayList<String> = arrayListOf()
        for(x in 0 .. 10)
        {
            values.add(x.toString())
        }

        var lat = 41.0195955
        var lng = 28.9877938
        for(x in 0 .. 2)
        {
            markersData?.add(MarkerData(lat,lng))
            lat = lat.minus(1.0)
            lng = lng.minus(1.0)
        }

        familyMembersAdapter.addData(values)
    }

    override fun initListener() {
        btnGetInfo.setOnClickListener {
            showFamilyInfoPopup()
        }
    }

    private fun showInvitationDialog(){
        DialogUtils.showInvitationDialog(this,
            object:DialogUtils.DialogInvitationListener{
                override fun sendInvitation() {

                }
            })
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

    override fun onFamilyMemberSelected(item: String) {
        Toast.makeText(this,"new regular member",Toast.LENGTH_LONG).show()
    }

    override fun onNewFamilyMemberSelected() {
        showInvitationDialog()
    }

    override fun serviceOnSuccess() {
    }

    override fun serviceOnFailure() {
    }
}
