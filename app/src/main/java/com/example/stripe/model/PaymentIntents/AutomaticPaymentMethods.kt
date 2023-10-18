package com.example.stripe.model.PaymentIntents

data class AutomaticPaymentMethods(
    val allow_redirects: String,
    val enabled: Boolean
)