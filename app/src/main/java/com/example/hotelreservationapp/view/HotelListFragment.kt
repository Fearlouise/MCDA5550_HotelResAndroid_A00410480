package com.example.hotelreservationapp.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelreservationapp.R
import com.example.hotelreservationapp.adapter.HotelAdapter
import com.example.hotelreservationapp.databinding.FragmentHotelListBinding
import com.example.hotelreservationapp.viewmodel.HotelListViewModel
import kotlinx.coroutines.launch

class HotelListFragment : Fragment() {

    private var _binding: FragmentHotelListBinding? = null
    private val binding get() = _binding!!

    private lateinit var hotelListViewModel: HotelListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotelListBinding.inflate(inflater, container, false)

        val checkInDate = arguments?.getString("check_in_date")
        val checkOutDate = arguments?.getString("check_out_date")
        val guestCount = arguments?.getInt("guest_count")
        val userName = arguments?.getString("user_name")
        val customerId = arguments?.getInt("customer_id")

        binding.checkInDateText.text = "Check-in Date: $checkInDate"
        binding.checkOutDateText.text = "Check-out Date: $checkOutDate"
        binding.guestCountText.text = "Number of Guests: $guestCount"
        binding.userNameText.text = "Name: $userName"

        hotelListViewModel = ViewModelProvider(this)[HotelListViewModel::class.java]
        val hotelAdapter = HotelAdapter { selectedHotel ->
            val bundle = Bundle().apply {
                putString("hotel_name", selectedHotel.hotelName)
                putInt("hotel_id", selectedHotel.id)
                putString("check_in_date", checkInDate)
                putString("check_out_date", checkOutDate)
                putInt("guest_count", guestCount ?: 0)
                putInt("customer_id", customerId ?: -1)
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BookingFragment::class.java, bundle)
                .addToBackStack(null)
                .commit()
        }

        binding.hotelRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = hotelAdapter
        }

        hotelListViewModel.hotels.observe(viewLifecycleOwner) { hotelList ->
            hotelAdapter.submitList(hotelList)
        }

        lifecycleScope.launch {
            hotelListViewModel.fetchHotels()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}