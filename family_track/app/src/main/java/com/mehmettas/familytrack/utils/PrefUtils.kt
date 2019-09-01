package com.mehmettas.familytrack.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.mehmettas.familytrack.CoreApp
import com.mehmettas.familytrack.data.remote.model.family.Family
import com.mehmettas.familytrack.data.remote.model.family.Member
import com.mehmettas.familytrack.utils.AppConstants.FAMILY_DETAIL
import com.mehmettas.familytrack.utils.AppConstants.FIRST_TIME
import com.mehmettas.familytrack.utils.AppConstants.IS_FAMILY_LOGGED
import com.mehmettas.familytrack.utils.AppConstants.MEMBER_DETAIL
import com.mehmettas.familytrack.utils.AppConstants.PREF_NAME
import com.mehmettas.familytrack.utils.extensions.setValue
import java.util.*
import kotlin.collections.ArrayList

object PrefUtils {

    val instance: SharedPreferences = CoreApp.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun checkIsFirstTimeOpen(): Boolean {
        val firstTime = instance.getBoolean(FIRST_TIME, true)
        if (firstTime) {
            instance.setValue(FIRST_TIME, false)
            return true
        }
        return false
    }

    fun createFamily(family:String,member:String) {
        if (!isLoggedFamily()) {
            instance.setValue(IS_FAMILY_LOGGED, true)
        }
        instance.setValue(FAMILY_DETAIL, family)
        instance.setValue(MEMBER_DETAIL, member)

    }

    fun getFamily(): ArrayList<Any> {
        val data = arrayListOf<Any>()
        data.add(GsonBuilder().create().fromJson(instance.getString(FAMILY_DETAIL,""), Family::class.java))
        data.add(GsonBuilder().create().fromJson(instance.getString(MEMBER_DETAIL,""), Member::class.java))
        return data
    }

    fun isLoggedFamily(): Boolean = instance.getBoolean(IS_FAMILY_LOGGED, false)

}