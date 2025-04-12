package com.example.hotelreservationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReservationConfirmationViewModel : ViewModel() {
    private val _reservationNumber = MutableLiveData<String>()
    val reservationNumber: LiveData<String> get() = _reservationNumber

    fun setReservationNumber(message: String) {
        _reservationNumber.value = message
    }
}