package com.example.hotelreservationapp.viewmodel

import androidx.lifecycle.*
import com.example.hotelreservationapp.api.ApiServiceInstance
import com.example.hotelreservationapp.model.Hotel

class HotelListViewModel : ViewModel() {
    private val _hotels = MutableLiveData<List<Hotel>>()
    val hotels: LiveData<List<Hotel>> = _hotels

    suspend fun fetchHotels() {
        try {
            val response = ApiServiceInstance.apiService.getAllHotels()
            _hotels.value = response
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}