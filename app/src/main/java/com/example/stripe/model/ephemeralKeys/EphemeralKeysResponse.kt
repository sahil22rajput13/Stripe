package com.example.stripe.model.ephemeralKeys

data class EphemeralKeysResponse(
    val associated_objects: ArrayList<AssociatedObject>,
    val created: Int,
    val expires: Int,
    val id: String,
    val livemode: Boolean,
    val `object`: String,
    val secret: String
)