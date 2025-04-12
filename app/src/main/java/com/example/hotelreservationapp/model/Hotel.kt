package com.example.hotelreservationapp.model

data class Hotel(
    val id: Int,
    val hotelName: String,
    val address: String,
    val city: String,
    val province: String,
    val postalCode: String
)