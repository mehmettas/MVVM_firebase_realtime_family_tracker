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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mehmettas.familytrack.data.remote.model.family.Family
import com.mehmettas.familytrack.data.remote.model.family.Member
import com.mehmettas.familytrack.utils.PrefUtils
import com.mehmettas.familytrack.utils.extensions.zoomToAllMarkers
import com.google.gson.*
import com.mehmettas.familytrack.data.remote.model.location.MemberLocation
import com.mehmettas.familytrack.utils.AppConstants.FAMILIES
import com.mehmettas.familytrack.utils.AppConstants.FAMILY_ID
import com.mehmettas.familytrack.utils.AppConstants.FAMILY_MEMBERS
import com.mehmettas.familytrack.utils.AppConstants.LOCATION
import com.mehmettas.familytrack.utils.AppConstants.MEMBER_ID
import com.mehmettas.familytrack.utils.service.LocationMonitoringService

class MainActivity : BaseActivity(), IMainNavigator, OnMapReadyCallback,
    FamilyAdapter.FamilyAdapterListener {
    private val viewModel by viewModel<MainViewModel>()

    private var markersData:ArrayList<MarkerData>?= arrayListOf()
    private var markers:ArrayList<Marker> = arrayListOf()
    private var invitedId:String?= null

    private val familyMembersAdapter by lazy {
        FamilyAdapter(arrayListOf(),this)
    }

    override val layoutId: Int?
        get() = R.layout.activity_main

    override fun initNavigator() {
        viewModel.setNavigator(this)
    }

    override fun initUI() {
        companionViewModel = viewModel
        retrieveAllMembersFromPref()
        rvMemberList.setHasFixedSize(true)
        rvMemberList.adapter = familyMembersAdapter
        observeViewModel()
        listenForOtherMembers()
    }

    private fun retrieveAllMembersFromPref() {

        val data = PrefUtils.getFamily()
        val gson = GsonBuilder().create()
        val showType = object : TypeToken<ArrayList<Member>>() {}.type
        val parser = JsonParser()
        val element = parser.parse(data[2].toString())
        val parsedElement = element.getAsJsonArray()
        allMembers = gson.fromJson(parsedElement, showType) as ArrayList<Member>

        LocationMonitoringService.allMembers = allMembers
        setDummy(allMembers)
    }

    companion object{
        private val db = FirebaseFirestore.getInstance()

        var allMembers:ArrayList<Member> = arrayListOf()
        var map:GoogleMap?=null
        lateinit var companionViewModel: MainViewModel

        val data = PrefUtils.getFamily()
        val family = data[0] as Family
        val member = data[1] as Member

        fun sendCurrentMemberLocation(location:MemberLocation)
        {
            val documentReference = db.collection(FAMILY_MEMBERS)
                .document(MEMBER_ID+ member.member_id)
                .collection(LOCATION)
                .document(MEMBER_ID+ member.member_id)

            companionViewModel.setCurrrentUserLocation(location,documentReference)
        }

        fun listenForOtherMembers()
        {
            val documentReferences = arrayListOf<DocumentReference>()

            allMembers.forEach {
                if(it.member_id!= member.member_id && it.member_id!="")
                {
                    documentReferences.add(db.collection(FAMILY_MEMBERS)
                        .document(MEMBER_ID+it.member_id)
                        .collection(LOCATION)
                        .document(MEMBER_ID+it.member_id)
                    ) }
                }
            companionViewModel.listenForFamilyMembers(documentReferences)
        }
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

    private fun setDummy(members:ArrayList<Member>)
    {
        var lat = 41.0195955
        var lng = 28.9877938
        for(x in 0 .. 1)
        {
            markersData?.add(MarkerData(lat,lng))
            lat = lat.minus(0.020)
            lng = lng.minus(0.020)
        }

        //markersData?.add(MarkerData(LocationMonitoringService.currentMemberLocation.lat,LocationMonitoringService.currentMemberLocation.lng))

        familyMembersAdapter.addData(members)
        members.add(Member("","",""))
        familyMembersAdapter.addData(members)
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
                override fun sendInvitation(memberID:String) {
                    invitedId = memberID
                    val docReference = db.collection(FAMILY_MEMBERS).document(MEMBER_ID+memberID)
                    viewModel.isMemberExist(docReference)
                }
            })
    }

    private fun showFamilyInfoPopup() {

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

    override fun onFamilyMemberSelected(item: Member) {
        Toast.makeText(this,"${item.member_id}",Toast.LENGTH_LONG).show()
    }

    override fun onNewFamilyMemberSelected() {
        showInvitationDialog()
    }

    override fun setUserLocationSuccess() {
        hideLoading()
    }

    override fun setUserLocationFailure() {
        hideLoading()
    }

    override fun listenLocationsSuccess(memberLocations:ArrayList<MemberLocation>) {
        hideLoading()
        listenForOtherMembers()
    }

    override fun listenLocationFailures() {
        hideLoading()
    }

    override fun memberExist() {
        val docReference = db.collection(FAMILY_MEMBERS).document(MEMBER_ID+invitedId)
        viewModel.retrieveMember(docReference)
    }

    override fun memberNotExist() {}

    override fun memberMoveSuccess(items:ArrayList<Any>) {
        var memberForMovement:Member?=null
        if(!(items[0] as Boolean)) // is callback for to give notice for "member has retrieved"
        {
            memberForMovement = items[1] as Member
            val docNewFamilyReference = db.collection(FAMILIES)
                .document(FAMILY_ID+memberForMovement.family_id)
                .collection(FAMILY_MEMBERS)
                .document(MEMBER_ID+memberForMovement.member_id)
            viewModel.moveMemberToNewFamily(memberForMovement,docNewFamilyReference)
        }
        else
        {

        }
    }

    override fun memberMoveFailure() {

    }
}
