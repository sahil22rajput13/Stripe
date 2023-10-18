package com.example.stripe.base

import android.app.Application
import com.example.stripe.utils.PUBLISHABLE_KEY
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(applicationContext, PUBLISHABLE_KEY)
    }
}