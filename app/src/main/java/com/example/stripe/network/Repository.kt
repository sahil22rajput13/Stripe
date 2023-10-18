package com.example.stripe.network

import javax.inject.Inject

class Repository @Inject constructor(
    private val apiServices: ApiServices
) {
    suspend fun createCustomer() = apiServices.createCustomer()

    suspend fun createEphemeralKeys(customer: String) =
        apiServices.createEphemeralKeys(customer)

    suspend fun createPaymentIntent(
        customer: String,
        amount: String,
        currency: String,
        automaticPayment: Boolean?
    ) = apiServices.createPaymentIntent(customer, amount, currency, automaticPayment)


}
