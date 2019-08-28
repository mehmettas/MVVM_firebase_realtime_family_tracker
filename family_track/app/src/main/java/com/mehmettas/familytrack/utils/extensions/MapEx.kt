package com.mehmettas.familytrack.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mehmettas.familytrack.R
import de.hdodenhof.circleimageview.CircleImageView

 fun createMarker(context:Context,map: GoogleMap, lat:Double, lng:Double, iconResource:Int): Marker {
    return map.addMarker(
        MarkerOptions()
            .position(LatLng(lat,lng))
            .anchor(0.5f, 0.5f)
            .title("")
            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(context,iconResource)))
    )
 }

 fun getMarkerBitmapFromView(context:Context,@DrawableRes resId: Int): Bitmap {

     val customMarkerView = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        .inflate(
            R.layout.layout_custom_marker,
            null
        )

    val markerImageView = customMarkerView.findViewById(R.id.imgFamilyMember) as CircleImageView
    markerImageView.setImageResource(resId)
    customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight())
    customMarkerView.buildDrawingCache()
    val returnedBitmap = Bitmap.createBitmap(
        customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(returnedBitmap)
    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
    val drawable = customMarkerView.getBackground()
    if (drawable != null)
        drawable!!.draw(canvas)
    customMarkerView.draw(canvas)
    return returnedBitmap

 }