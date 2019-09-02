package com.mehmettas.familytrack.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.location.Location
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.github.florent37.rxgps.RxGps
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.maps.model.*
import com.mehmettas.familytrack.data.remote.model.MarkerData
import com.mehmettas.familytrack.ui.main.FamilyAdapter.FamilyAdapter
import com.mehmettas.familytrack.utils.DialogUtils
import com.mehmettas.familytrack.utils.extensions.createMarker
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mehmettas.familytrack.data.remote.model.family.Family
import com.mehmettas.familytrack.data.remote.model.family.Member
import com.mehmettas.familytrack.data.remote.model.location.MemberLocation
import com.mehmettas.familytrack.utils.IDGenerator
import com.mehmettas.familytrack.utils.PrefUtils
import com.mehmettas.familytrack.utils.extensions.isLocationEnabled
import com.mehmettas.familytrack.utils.extensions.zoomToAllMarkers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : BaseActivity(), IMainNavigator, OnMapReadyCallback,
    FamilyAdapter.FamilyAdapterListener, LocationListener {
    private val viewModel by viewModel<MainViewModel>()
    private var markersData:ArrayList<MarkerData>?= arrayListOf()
    private var markers:ArrayList<Marker> = arrayListOf()

    private var currentMemberLocation:MemberLocation?=null

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

        val db = FirebaseFirestore.getInstance()

        var familyId = IDGenerator.GetBase62(6)
        var memberId = IDGenerator.GetBase62(6)


        var lat = "41.2342"
        var lng = "38.2315"



        val docReferenceForFamily = db.collection("families")
            .document("family_id_${familyId}")

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


        //viewModel.writeOnFamily(memberContent,docReferenceForMember)

    }

    private fun initMap()
    {
        spin_kit.visibility = View.VISIBLE
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapMain) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try{
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,R.raw.style_json
                )
            )
        }
        catch (e: Resources.NotFoundException) {
        }

        for(x in 0 .. markersData?.size!!-1){
            if (x==(markersData?.size!!.minus(1)))
                markers.add(createMarker(this,googleMap,markersData!![x].lat,markersData!![x].lng,R.drawable.boy))
            else
                markers.add(createMarker(this,googleMap,markersData!![x].lat,markersData!![x].lng,R.drawable.ic_sample_member))
        }

        googleMap.setOnMapLoadedCallback {
            googleMap.isMyLocationEnabled = true
            zoomToAllMarkers(googleMap,markers)
            spin_kit.visibility = View.GONE
        }
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

        initMap()
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

        val data = PrefUtils.getFamily()
        val family = data[0] as Family
        val member = data[1] as Member

        var familyId = family.family_id
        var memberId = member.member_id
        var memberCount = family.family_member_count.toString()

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

    override fun writeOnFamilySuccess() {
    }

    override fun writeOnFamilyFailure() {
    }


    override fun onLocationChanged(location: Location?) {
        currentMemberLocation!!.lat = location!!.latitude
        currentMemberLocation!!.lng = location.longitude
    }
}
