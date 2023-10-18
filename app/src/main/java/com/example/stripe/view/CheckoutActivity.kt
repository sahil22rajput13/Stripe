package com.example.stripe.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.stripe.databinding.ActivityMainBinding
import com.example.stripe.utils.Status.ERROR
import com.example.stripe.utils.Status.LOADING
import com.example.stripe.utils.Status.SUCCESS
import com.example.stripe.utils.toast
import com.example.stripe.viewmodel.CheckoutViewmodel
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // Initialize the ViewModel
    private val viewModel: CheckoutViewmodel by viewModels()
    private lateinit var paymentSheet: PaymentSheet
    private var paymentIntentClientSecret: String = ""
    private var customerId: String = ""
    private var ephemeralKey: String = ""
    // Format the amount as "10.00" in cents (1000)
    private val amountInCents = 1000
    // Convert it to a string
    private val amountString = amountInCents.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        viewModel.onCustomerId()
        observe()
        setContentView(binding.root)
        binding.btnPayment.setOnClickListener {
            if (paymentIntentClientSecret.isEmpty()) {
                toast("Wait verifying payment")
            } else
                presentPaymentSheet()
        }

    }

    private fun observe() {
        viewModel.resultCustomer.observe(this) { response ->
            when (response.status) {
                SUCCESS -> {
                    customerId = response.data?.id.toString()
                    viewModel.onEphemeralKeys(customerId)
                    Log.d("Checkout", customerId)
                }

                ERROR -> {
                    print("Error: ${response.message.toString()}")
                    toast("Error: ${response.message.toString()}")
                }

                LOADING -> {

                }
            }
        }
        viewModel.resultEphemeralKeys.observe(this) { response ->
            when (response.status) {
                SUCCESS -> {

                    ephemeralKey = response.data?.id.toString()
                    viewModel.onPaymentIntents(customerId, amountString, "Inr", true)
                    Log.d("Checkout", ephemeralKey)
                }

                ERROR -> {
                    print("Error: ${response.message.toString()}")
                    toast("Error: ${response.message.toString()}")
                }

                LOADING -> {

                }
            }
        }
        viewModel.resultPaymentIntents.observe(this) { response ->
            when (response.status) {
                SUCCESS -> {
                    paymentIntentClientSecret = response.data?.client_secret.toString()
                    Log.d("Checkout", paymentIntentClientSecret)
                }

                ERROR -> {
                    print("Error: ${response.message.toString()}")
                    toast("Error: ${response.message.toString()}")
                }

                LOADING -> {

                }
            }
        }
    }

    private fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Testers",
                PaymentSheet.CustomerConfiguration(customerId, ephemeralKey),
                // Set `allowsDelayedPaymentMethods` to true if your business handles
                // delayed notification payment methods like US bank accounts.
                allowsDelayedPaymentMethods = true
            )
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
                toast("Canceled")
            }

            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
                toast("Error: ${paymentSheetResult.error}")
            }

            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
                toast("Completed")
            }
        }
    }
}