package com.example.hotelreservationapp.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hotelreservationapp.databinding.FragmentHotelSearchBinding
import com.example.hotelreservationapp.model.CustomerRequest
import com.example.hotelreservationapp.viewmodel.CustomerViewModel
import kotlinx.coroutines.launch

class HotelSearchFragment : Fragment() {

    private var _binding: FragmentHotelSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var customerViewModel: CustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotelSearchBinding.inflate(inflater, container, false)
        customerViewModel = ViewModelProvider(this)[CustomerViewModel::class.java]

        binding.searchButton.setOnClickListener {
            val checkInDate = "${binding.checkInDatePicker.dayOfMonth}-${binding.checkInDatePicker.month + 1}-${binding.checkInDatePicker.year}"
            val checkOutDate = "${binding.checkOutDatePicker.dayOfMonth}-${binding.checkOutDatePicker.month + 1}-${binding.checkOutDatePicker.year}"
            val guestCount = binding.guestCountEdit.text.toString().toIntOrNull() ?: 0
            val userName = binding.enterNameEdit.text.toString()

            if (userName.isBlank()) {
                Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val customer = customerViewModel.createCustomer(CustomerRequest(name = userName))
                if (customer != null) {
                    val sharedPrefs = requireActivity().getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE)
                    sharedPrefs.edit().apply {
                        putString("check_in_date", checkInDate)
                        putString("check_out_date", checkOutDate)
                        putInt("guest_count", guestCount)
                        putString("user_name", userName)
                        putInt("customer_id", customer.id)
                        apply()
                    }

                    val bundle = Bundle().apply {
                        putString("check_in_date", checkInDate)
                        putString("check_out_date", checkOutDate)
                        putInt("guest_count", guestCount)
                        putString("user_name", userName)
                        putInt("customer_id", customer.id)
                    }

                    parentFragmentManager.beginTransaction()
                        .replace(com.example.hotelreservationapp.R.id.fragment_container, HotelListFragment::class.java, bundle)
                        .addToBackStack(null)
                        .commit()
                } else {
                    Toast.makeText(context, "Failed to create customer", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}