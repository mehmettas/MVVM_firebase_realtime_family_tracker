package com.mehmettas.familytrack.ui.main

import android.content.res.Resources
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
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
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mehmettas.familytrack.data.remote.model.family.Family
import com.mehmettas.familytrack.data.remote.model.family.Member
import com.mehmettas.familytrack.utils.PrefUtils
import com.mehmettas.familytrack.utils.extensions.zoomToAllMarkers


class MainActivity : BaseActivity(), IMainNavigator, OnMapReadyCallback,
    FamilyAdapter.FamilyAdapterListener {

    private val viewModel by viewModel<MainViewModel>()
    private var markersData:ArrayList<MarkerData>?= arrayListOf()
    private var markers:ArrayList<Marker> = arrayListOf()
    private var allMembers:ArrayList<Member> = arrayListOf()

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

        val data = PrefUtils.getFamily()
        allMembers =  GsonBuilder().create().fromJson(
            Gson().toJson(data[2]),
            object : TypeToken<ArrayList<Member>>() {
            }.type
        ) as ArrayList<Member>

        setDummy()
    }

    companion object{
        var map:GoogleMap?=null
    }

    private fun initMap()
    {
        spin_kit.visibility = View.VISIBLE
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapMain) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
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
        for(x in 0 .. 1)
        {
            markersData?.add(MarkerData(lat,lng))
            lat = lat.minus(0.020)
            lng = lng.minus(0.020)
        }

        //markersData?.add(MarkerData(LocationMonitoringService.currentMemberLocation.lat,LocationMonitoringService.currentMemberLocation.lng))

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

}
