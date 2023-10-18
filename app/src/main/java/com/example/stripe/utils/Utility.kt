package com.example.stripe.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.stripe.utils.SharedPreference
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class Utility {
    @Suppress("DEPRECATION")
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm?.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    fun getDeviceToken(sharedPreference: SharedPreference) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("device_token", token)
            sharedPreference.putString(SharedPreference.Key.DEVICETOKEN, token)
        })
    }
}
