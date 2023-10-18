package com.example.stripe.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreference@Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Hilt", Context.MODE_PRIVATE)

    fun putString(key: Key, value: String) {
        sharedPreferences.edit().putString(key.name, value).apply()
    }

    fun putInt(key: Key, value: Int) {
        sharedPreferences.edit().putInt(key.name, value).apply()
    }

    fun putBoolean(key: Key, value: Boolean) {
        sharedPreferences.edit().putBoolean(key.name, value).apply()
    }

    fun getString(key: Key, valueDefault: String = ""): String? {
        return sharedPreferences.getString(key.name, valueDefault)
    }

    fun getInt(key: Key, valueDefault: Int = 0): Int {
        return sharedPreferences.getInt(key.name, valueDefault)
    }

    fun getBoolean(key: Key, valueDefault: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key.name, valueDefault)
    }
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
    enum class Key {
        OTP, EMAILOTP, COUNTRY, LANGUAGE, LATITUDE, LONGITUDE, ADDRESS, PASSWORD,
        TRADESMENNAME, TOKEN, ISUSERLOGIN, FIRSTNAME, LASTNAME, PHONE, POSTCODE,
        USERDETIALS, GENDER, DOB, INJURIES, USERID, USERTYPE, ISLANDINGCOMPLETE,
        ISTRADESMENLANDINGCOMPLETE, DEVICETOKEN, ISGREEN, ISRED, ISGREY, USERNAME,
        EMAIL, Address, PROFILEIMAGE, TOTALCREDIT, ISCARDNOTEMPTY
    }
}
