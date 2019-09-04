package com.mehmettas.familytrack.ui.main

import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
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
    var memberForMovement:Member?=null

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

        val collectionReference = db.collection(FAMILY_MEMBERS) // CHECK HERE..
        viewModel.retrieveFamilyMembers(collectionReference, family.family_id!!)
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
        setData(allMembers)
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
                if(it.member_id!= member.member_id && it.member_id!="" && it!=null)
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

    @RequiresApi(Build.VERSION_CODES.N)
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

        allMembers.removeIf {
            it.member_id == member.member_id
        }
        allMembers.removeAt(allMembers.size-1)

        if(allMembers.size==1)
            markers.add(createMarker(this,googleMap,0.0,0.0,R.drawable.ic_sample_member))
        else
        {
            for(x in 0 .. allMembers?.size!!-1){
                markers.add(createMarker(this,googleMap,0.0,0.0,R.drawable.ic_sample_member))
            }
        }
        googleMap.setOnMapLoadedCallback {
            spin_kit.visibility = View.GONE
            if(!markers.isEmpty())
            {
            }
        }
    }


    private fun observeViewModel()
    {}

    private fun setData(members:ArrayList<Member>)
    {
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
        Toast.makeText(this,"${item.member_name}",Toast.LENGTH_LONG).show()
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


        if(memberLocations.size==1)
            markers[0].position = LatLng(memberLocations[0].lat,memberLocations[0].lng)

        else
        {
            for(i in 0 until memberLocations.size)
            {
                markers[i].position = LatLng(memberLocations[i].lat,memberLocations[i].lng)
            }
        }

        Handler().postDelayed({
            listenForOtherMembers()
            zoomToAllMarkers(map!!,markers)
        },5000)

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
        if(!(items[0] as Boolean)) // is callback for to give notice for "member has retrieved"
        {
            memberForMovement = items[1] as Member
            val docNewFamilyReference = db.collection(FAMILY_MEMBERS)
                .document(MEMBER_ID+memberForMovement?.member_id)

            memberForMovement?.family_id = family.family_id
            viewModel.moveMemberToNewFamily(memberForMovement!!,docNewFamilyReference)
        }
        else // is callback for representing that member has been moved / invited to a new family
        {
            allMembers.add(memberForMovement!!)
            familyMembersAdapter.addData(allMembers)
            val updateFamilyReference = db.collection(FAMILIES)
                .document(FAMILY_ID+ family.family_id)
            family.family_member_count = allMembers.size
            viewModel.writeOnFamily(family,updateFamilyReference)
            markers.add(createMarker(this, map!!,0.0,0.0,R.drawable.ic_sample_member))
            listenForOtherMembers()
        }
    }

    override fun membersRetrieved(members: ArrayList<Member>) {
        allMembers = members
        PrefUtils.createFamily(Gson().toJson(family),Gson().toJson(member),Gson().toJson(allMembers))
        familyMembersAdapter.addData(allMembers)
    }

    override fun memberMoveFailure() {
    }

    override fun writeOnFamilySuccess() {
    }

    override fun writeOnFamilyFailure() {
    }

    override fun membersNotRetrieved() {
    }
}
