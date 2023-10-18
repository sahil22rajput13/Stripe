package com.example.stripe.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stripe.model.PaymentIntents.PaymentIntentsResponse
import com.example.stripe.model.customers.CustomerResponse
import com.example.stripe.model.ephemeralKeys.EphemeralKeysResponse
import com.example.stripe.network.Repository
import com.example.stripe.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CheckoutViewmodel @Inject constructor(private val repository: Repository) : ViewModel() {
    val resultCustomer = MutableLiveData<Resource<CustomerResponse>>()
    val resultEphemeralKeys = MutableLiveData<Resource<EphemeralKeysResponse>>()
    val resultPaymentIntents = MutableLiveData<Resource<PaymentIntentsResponse>>()

    fun onCustomerId() {
        resultCustomer.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.createCustomer()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        resultCustomer.value =
                            Resource.success(response.body(), response.message().toString())
                    } else {
                        Log.d("resultCustomer", "${response.errorBody()}")
                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultCustomer.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onEphemeralKeys(customer: String) {
        resultCustomer.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.createEphemeralKeys(customer)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful)
                        resultEphemeralKeys.value = Resource.success(
                            response.body(),
                            response.message().toString()
                        )
                    else {
                        Log.d("resultCustomer", "error response 2")
                    }
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultEphemeralKeys.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }

    fun onPaymentIntents(
        customer: String,
        amount: String,
        currency: String,
        automaticPayment: Boolean
    ) {
        resultCustomer.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    repository.createPaymentIntent(customer, amount, currency, automaticPayment)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful)
                        resultPaymentIntents.value = Resource.success(
                            response.body(),
                            response.message().toString()
                        )
                    else
                        Log.d("resultCustomer", "error response 3")
                }
            } catch (t: Exception) {
                withContext(Dispatchers.Main) {
                    resultPaymentIntents.value = Resource.error(null, t.message.toString())
                }
            }
        }
    }
}