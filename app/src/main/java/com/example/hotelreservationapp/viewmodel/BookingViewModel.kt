package com.example.hotelreservationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookingViewModel : ViewModel() {

    private val _hotelName = MutableLiveData<String>()
    val hotelName: LiveData<String> get() = _hotelName

    private val _checkInDate = MutableLiveData<String>()
    val checkInDate: LiveData<String> get() = _checkInDate

    private val _checkOutDate = MutableLiveData<String>()
    val checkOutDate: LiveData<String> get() = _checkOutDate

    private val _guestCount = MutableLiveData<Int>()
    val guestCount: LiveData<Int> get() = _guestCount

    fun setHotelName(name: String) {
        _hotelName.value = name
    }

    fun setCheckInDate(date: String) {
        _checkInDate.value = date
    }

    fun setCheckOutDate(date: String) {
        _checkOutDate.value = date
    }

    fun setGuestCount(count: Int) {
        _guestCount.value = count
    }
}