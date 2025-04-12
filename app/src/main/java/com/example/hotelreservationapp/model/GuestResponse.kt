package com.example.hotelreservationapp.model

data class GuestResponse(
    val id: Long,
    val name: String,
    val gender: String,
    val reservation: ReservationIdOnly,
    val customer: CustomerIdOnly
)