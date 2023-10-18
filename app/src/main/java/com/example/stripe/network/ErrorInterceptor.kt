package com.example.stripe.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)
        when (response.code) {
            401 -> {
                Log.d(
                    "responseError",
                    "intercept: errorCode ${response.code}  ************ error Message ${response.message}"
                )
            }

            else -> {
                Log.d(
                    "responseError",
                    "intercept: errorCode ${response.code}  ************ error Message ${response.message}"
                )
            }

        }
        return response
    }
}
