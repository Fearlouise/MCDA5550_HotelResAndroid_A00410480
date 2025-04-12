package com.example.hotelreservationapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hotelreservationapp.api.ApiServiceInstance
import com.example.hotelreservationapp.model.GuestListWrapper
import com.example.hotelreservationapp.model.GuestRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class GuestViewModel : ViewModel() {
    suspend fun addGuests(request: List<GuestRequest>) {
        withContext(Dispatchers.IO) {
            try {
                val response = ApiServiceInstance.apiService.addGuests(GuestListWrapper(request))
                Log.d("GuestViewModel", "Guests added successfully: $response")
            } catch (e: Exception) {
                Log.e("GuestViewModel", "Failed to add guests", e)
            }
        }
    }
}