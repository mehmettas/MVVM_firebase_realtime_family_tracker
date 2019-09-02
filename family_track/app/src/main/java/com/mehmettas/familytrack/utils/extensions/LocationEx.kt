package com.mehmettas.familytrack.utils.extensions

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils

fun isLocationEnabled(context: Context?): Boolean {
    var locationMode = 0
    val locationProviders: String

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        try {
            locationMode = Settings.Secure.getInt(context?.contentResolver, Settings.Secure.LOCATION_MODE)

        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            return false
        }

        return locationMode != Settings.Secure.LOCATION_MODE_OFF

    } else {
        locationProviders =
            Settings.Secure.getString(context?.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
        return !TextUtils.isEmpty(locationProviders)
    }
}