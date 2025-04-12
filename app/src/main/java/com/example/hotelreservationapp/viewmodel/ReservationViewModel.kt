package com.example.hotelreservationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotelreservationapp.api.ApiServiceInstance
import com.example.hotelreservationapp.model.ReservationRequest
import com.example.hotelreservationapp.model.ReservationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReservationViewModel : ViewModel() {

    private val _confirmationNumber = MutableLiveData<String>()
    val confirmationNumber: LiveData<String> get() = _confirmationNumber

    private val _reservationFailed = MutableLiveData<Boolean>()
    val reservationFailed: LiveData<Boolean> get() = _reservationFailed

    private val _checkInDate = MutableLiveData<String>()
    val checkInDate: LiveData<String> get() = _checkInDate

    private val _checkOutDate = MutableLiveData<String>()
    val checkOutDate: LiveData<String> get() = _checkOutDate

    private val _guestCount = MutableLiveData<Int>()
    val guestCount: LiveData<Int> get() = _guestCount

    suspend fun makeReservation(request: ReservationRequest): ReservationResponse? {
        return try {
            val response = withContext(Dispatchers.IO) {
                ApiServiceInstance.apiService.makeReservation(request)
            }
            _confirmationNumber.postValue(response.reservationNumber)
            _reservationFailed.postValue(false)
            response
        } catch (e: Exception) {
            _reservationFailed.postValue(true)
            null
        }
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

    fun setConfirmationNumber(number: String) {
        _confirmationNumber.value = number
    }

    fun setReservationFailed(failed: Boolean) {
        _reservationFailed.value = failed
    }
}