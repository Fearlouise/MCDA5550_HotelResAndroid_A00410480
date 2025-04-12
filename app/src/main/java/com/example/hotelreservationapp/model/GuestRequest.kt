package com.example.hotelreservationapp.model

import com.example.hotelreservationapp.model.CustomerIdOnly
import com.example.hotelreservationapp.model.ReservationIdOnly

data class GuestRequest(
    val name: String,
    val gender: String,
    val reservation: ReservationIdOnly,
    val customer: CustomerIdOnly
)