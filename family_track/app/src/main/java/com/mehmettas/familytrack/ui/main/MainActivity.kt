package com.mehmettas.familytrack.ui.main

import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.data.remote.firebase.ICallbackListener
import com.mehmettas.familytrack.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), IMainNavigator, ICallbackListener, OnMapReadyCallback {
    private val viewModel by viewModel<MainViewModel>()

    override val layoutId: Int?
        get() = R.layout.activity_main

    override fun initNavigator() {
        viewModel.setNavigator(this)
    }

    override fun initUI() {

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

    override fun onMapReady(p0: GoogleMap?) {

    }

    override fun initListener() {
    }

    override fun onSuccess() {
    }

    override fun onFailure() {
    }
}
