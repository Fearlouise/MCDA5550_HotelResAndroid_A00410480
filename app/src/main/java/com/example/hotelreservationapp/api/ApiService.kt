package com.example.hotelreservationapp.api

import com.example.hotelreservationapp.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("api/hotels")
    suspend fun getAllHotels(): List<Hotel>

    @POST("api/customers")
    suspend fun addCustomer(@Body request: CustomerRequest): CustomerResponse

    @POST("api/reservations")
    suspend fun makeReservation(@Body request: ReservationRequest): ReservationResponse

    @POST("api/guests")
    suspend fun addGuests(@Body guestListWrapper: GuestListWrapper): List<GuestResponse>
}