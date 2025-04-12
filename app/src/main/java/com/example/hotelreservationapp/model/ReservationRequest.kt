package com.example.hotelreservationapp.model

import com.example.hotelreservationapp.model.CustomerIdOnly
import com.example.hotelreservationapp.model.HotelIdOnly

data class ReservationRequest(
    val hotel: HotelIdOnly,
    val customer: CustomerIdOnly,
    val checkInDate: String,
    val checkOutDate: String,
    val numberOfGuests: Int
)