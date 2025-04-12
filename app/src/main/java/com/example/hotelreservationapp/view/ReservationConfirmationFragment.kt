package com.example.hotelreservationapp.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hotelreservationapp.databinding.FragmentReservationConfirmationBinding
import com.example.hotelreservationapp.viewmodel.ReservationViewModel

class ReservationConfirmationFragment : Fragment() {

    private var _binding: FragmentReservationConfirmationBinding? = null
    private val binding get() = _binding!!

    private val reservationViewModel: ReservationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationConfirmationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val sharedPrefs = requireActivity().getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE)
        val customerName = sharedPrefs.getString("user_name", "Guest") ?: "Guest"

        // ✅ Observe and set confirmation message
        reservationViewModel.confirmationNumber.observe(viewLifecycleOwner) { number ->
            val message = "Thank you for your reservation, $customerName!\n\nYour reservation number is:\n$number"
            binding.confirmationMessageText.text = message
        }

        // ✅ Observe and set check-in, check-out, guest count
        reservationViewModel.checkInDate.observe(viewLifecycleOwner) { checkIn ->
            binding.checkInText.text = "Check-in Date: $checkIn"
        }

        reservationViewModel.checkOutDate.observe(viewLifecycleOwner) { checkOut ->
            binding.checkOutText.text = "Check-out Date: $checkOut"
        }

        reservationViewModel.guestCount.observe(viewLifecycleOwner) { guests ->
            binding.guestCountText.text = "Number of Guests: $guests"
        }

        reservationViewModel.reservationFailed.observe(viewLifecycleOwner) { failed ->
            if (failed) {
                Toast.makeText(requireContext(), "Reservation failed", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}