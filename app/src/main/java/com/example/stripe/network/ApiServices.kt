package com.example.stripe.network

import com.example.stripe.model.PaymentIntents.PaymentIntentsResponse
import com.example.stripe.model.customers.CustomerResponse
import com.example.stripe.model.ephemeralKeys.EphemeralKeysResponse
import com.example.stripe.utils.BEARER_TOKEN
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiServices {
    @Headers("Authorization:Bearer $BEARER_TOKEN")
    @POST("v1/customers")
    suspend fun createCustomer() : Response<CustomerResponse>

    @Headers("Authorization: Bearer $BEARER_TOKEN", "Stripe-Version: 2023-08-16")
    @POST("v1/ephemeral_keys")
    suspend fun createEphemeralKeys(@Query("customer") customer: String): Response<EphemeralKeysResponse>

    @Headers("Authorization: Bearer $BEARER_TOKEN")
    @POST("v1/payment_intents")
    suspend fun createPaymentIntent(
        @Query("customer") customer: String,
        @Query("amount") amount: String,
        @Query("currency") currency: String,
        @Query("automatic_payment_methods[enabled]") automaticPayment: Boolean?
    ): Response<PaymentIntentsResponse>

}
