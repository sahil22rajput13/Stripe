package com.example.stripe.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.stripe.utils.SharedPreference
import com.example.stripe.utils.SharedPreferenceModule
import com.example.stripe.utils.UtilityModule
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class ClientInterceptor @Inject constructor(
    @ApplicationContext private val mContext: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalBuilder: Request = chain.request()
        val response = originalBuilder.newBuilder()
        val token = SharedPreferenceModule.provideSharedPreference(mContext).getString(
            SharedPreference.Key.TOKEN, "")
        Log.d(token, "intercept$token")
        if (UtilityModule.provideUtility().isNetworkConnected(mContext)) {
            response.addHeader("Accept", "application/json")
            response.addHeader("Content-Type", "text/json; charset=utf-8")
            response.addHeader("Authorization", "Bearer $token").build()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(mContext, "No Internet", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
        val request = response.build()
        return chain.proceed(request)
    }
}