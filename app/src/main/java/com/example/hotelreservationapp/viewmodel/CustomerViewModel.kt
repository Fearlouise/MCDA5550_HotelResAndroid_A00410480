package com.example.hotelreservationapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hotelreservationapp.api.ApiServiceInstance
import com.example.hotelreservationapp.model.CustomerRequest
import com.example.hotelreservationapp.model.CustomerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomerViewModel : ViewModel() {
    suspend fun createCustomer(request: CustomerRequest): CustomerResponse? {
        return try {
            withContext(Dispatchers.IO) {
                ApiServiceInstance.apiService.addCustomer(request)
            }
        } catch (e: Exception) {
            null
        }
    }
}