package com.mehmettas.familytrack.utils.service

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import java.util.*
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.mehmettas.familytrack.R
import com.mehmettas.familytrack.data.remote.model.family.Member
import com.mehmettas.familytrack.data.remote.model.location.MemberLocation
import com.mehmettas.familytrack.ui.main.MainActivity
import com.mehmettas.familytrack.utils.PrefUtils
import kotlin.collections.ArrayList

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class LocationMonitoringService : Service(),
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    var counter = 0
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null


    private val TAG = LocationMonitoringService::class.java!!.getSimpleName()
    lateinit var mLocationClient: GoogleApiClient
    internal var mLocationRequest = LocationRequest()


    val ACTION_LOCATION_BROADCAST =
        LocationMonitoringService::class.java!!.getName() + "LocationBroadcast"
    val EXTRA_LATITUDE = "extra_latitude"
    val EXTRA_LONGITUDE = "extra_longitude"


    companion object {
        const val LOCATION_INTERVAL = 5000L
        const val FASTEST_LOCATION_INTERVAL = 5000L
        var SERVICE_STOPPED = false

        var currentMemberLocation:MemberLocation= MemberLocation(0.0,0.0)

        // All family members to update their location
        var allMembers:ArrayList<Member> = arrayListOf()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startTimer()

        mLocationClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mLocationRequest.interval = LOCATION_INTERVAL
        mLocationRequest.fastestInterval = FASTEST_LOCATION_INTERVAL

        val priority = LocationRequest.PRIORITY_HIGH_ACCURACY //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes

        mLocationRequest.priority = priority
        mLocationClient.connect()

        return START_STICKY
    }

    /*
     * LOCATION CALLBACKS
     */
    override fun onConnected(dataBundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted")
            //Permission not granted by user so cancel the further execution.

            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mLocationClient,
            mLocationRequest,
            this
        )

        Log.d(TAG, "Connected to Google API")
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    override fun onConnectionSuspended(i: Int) {
        Log.d(TAG, "Connection suspended")
    }

    //to get the location change
    override fun onLocationChanged(location: Location?) {
        Log.d(TAG, "Location changed")
        if (location != null) {
            Log.d(TAG, "== location != null")
            Log.d(TAG, "** location ${location.latitude}, ${location.longitude}")

            currentMemberLocation!!.lat = location.latitude
            currentMemberLocation!!.lng = location.longitude

            // Update Current Member Location on every change
            if(PrefUtils.isLoggedFamily())
            {
                MainActivity.sendCurrentMemberLocation(currentMemberLocation)
                if(MainActivity.map!=null)
                {
                    // CONTINUE ...
                    // createMarker(this,MainActivity.map!!, currentMemberLocation.lat, currentMemberLocation.lng,R.drawable.girl)
                }
            }

        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "Failed to connect to Google API")
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground()
        } else {
            startForeground(1, Notification())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("EXIT", "ondestroy!")
        val broadcastIntent = Intent(this, ListenerServiceRestarter::class.java)
        if (!SERVICE_STOPPED) sendBroadcast(broadcastIntent)
        stopTimerTask()
    }

    private fun startTimer() {
        timer = Timer()
        initializeTimerTask()
        timer!!.schedule(timerTask!!, 1000, 1000)
    }

    private fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                Log.i("in timer", "in timer ++++  " + counter++)
            }
        }
    }

    private fun stopTimerTask() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    // for executing in the foreground
    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

}