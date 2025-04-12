package com.example.hotelreservationapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hotelreservationapp.R
import com.example.hotelreservationapp.databinding.FragmentBookingBinding
import com.example.hotelreservationapp.model.*
import com.example.hotelreservationapp.viewmodel.BookingViewModel
import com.example.hotelreservationapp.viewmodel.GuestViewModel
import com.example.hotelreservationapp.viewmodel.ReservationViewModel
import com.example.hotelreservationapp.model.CustomerIdOnly
import com.example.hotelreservationapp.model.ReservationIdOnly
import com.example.hotelreservationapp.model.HotelIdOnly
import kotlinx.coroutines.launch

class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var guestViewModel: GuestViewModel

    private val guestNameInputs = mutableListOf<EditText>()
    private val guestGenderGroups = mutableListOf<RadioGroup>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)

        val sharedPrefs = requireActivity().getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE)
        val checkInDate = arguments?.getString("check_in_date") ?: ""
        val checkOutDate = arguments?.getString("check_out_date") ?: ""
        val customerId = arguments?.getInt("customer_id") ?: -1
        val guestCount = arguments?.getInt("guest_count") ?: 0
        val hotelName = arguments?.getString("hotel_name") ?: ""
        val hotelId = arguments?.getInt("hotel_id") ?: -1

        bookingViewModel = ViewModelProvider(requireActivity())[BookingViewModel::class.java]
        reservationViewModel = ViewModelProvider(requireActivity())[ReservationViewModel::class.java]
        guestViewModel = ViewModelProvider(this)[GuestViewModel::class.java]

        bookingViewModel.setHotelName(hotelName)
        bookingViewModel.setCheckInDate(checkInDate)
        bookingViewModel.setCheckOutDate(checkOutDate)
        bookingViewModel.setGuestCount(guestCount)

        reservationViewModel.setCheckInDate(checkInDate)
        reservationViewModel.setCheckOutDate(checkOutDate)
        reservationViewModel.setGuestCount(guestCount)

        binding.viewModel = bookingViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        for (i in 1..guestCount) {
            val nameInput = EditText(context).apply {
                hint = "Enter name for Guest $i"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            val genderGroup = RadioGroup(context).apply {
                orientation = RadioGroup.HORIZONTAL
                val male = RadioButton(context).apply { text = "Male" }
                val female = RadioButton(context).apply { text = "Female" }
                addView(male)
                addView(female)
            }
            guestNameInputs.add(nameInput)
            guestGenderGroups.add(genderGroup)

            binding.guestContainer.addView(nameInput)
            binding.guestContainer.addView(genderGroup)
        }

        binding.confirmButton.setOnClickListener {
            Toast.makeText(context, "Confirm clicked", Toast.LENGTH_SHORT).show()

            Log.d("BookingFragment", "hotelId: $hotelId, customerId: $customerId")
            Log.d("BookingFragment", "checkInDate: $checkInDate, checkOutDate: $checkOutDate")

            val formattedCheckIn = checkInDate.convertToIsoDate()
            val formattedCheckOut = checkOutDate.convertToIsoDate()

            val reservationRequest = ReservationRequest(
                hotel = HotelIdOnly(hotelId),
                customer = CustomerIdOnly(customerId),
                checkInDate = formattedCheckIn,
                checkOutDate = formattedCheckOut,
                numberOfGuests = guestCount
            )

            lifecycleScope.launch {
                try {
                    val reservationResponse = reservationViewModel.makeReservation(reservationRequest)

                    if (reservationResponse != null) {
                        Log.d("BookingFragment", "Reservation successful: $reservationResponse")

                        val reservationId = reservationResponse.id
                        val confirmationNumber = reservationResponse.reservationNumber

                        reservationViewModel.setCheckInDate(formattedCheckIn)
                        reservationViewModel.setCheckOutDate(formattedCheckOut)
                        reservationViewModel.setGuestCount(guestCount)

                        postGuestsAndNavigate(reservationId, customerId, confirmationNumber)
                    } else {
                        Log.e("BookingFragment", "Reservation failed: response was null")
                        reservationViewModel.setReservationFailed(true)
                    }
                } catch (e: Exception) {
                    Log.e("BookingFragment", "Exception during reservation", e)
                    reservationViewModel.setReservationFailed(true)
                }
            }
        }

        return binding.root
    }

    private fun postGuestsAndNavigate(reservationId: Int, customerId: Int, confirmationNumber: String) {
        lifecycleScope.launch {
            val guests = mutableListOf<GuestRequest>()

            for (i in guestNameInputs.indices) {
                val name = guestNameInputs[i].text.toString().trim()
                val genderGroup = guestGenderGroups[i]
                val selectedId = genderGroup.checkedRadioButtonId
                val gender = genderGroup.findViewById<RadioButton>(selectedId)?.text?.toString()?.trim() ?: ""

                if (name.isBlank() || gender.isBlank()) {
                    Toast.makeText(context, "Please enter all guest names and genders.", Toast.LENGTH_SHORT).show()
                    Log.e("BookingFragment", "⛔ Guest entry incomplete - name: '$name', gender: '$gender'")
                    return@launch
                }

                val guest = GuestRequest(
                    name = name,
                    gender = gender,
                    reservation = ReservationIdOnly(reservationId),
                    customer = CustomerIdOnly(customerId)
                )
                guests.add(guest)
                Log.d("BookingFragment", "✅ Valid Guest: $guest")
            }

            Log.d("BookingFragment", "📤 Sending guest list: $guests")

            guestViewModel.addGuests(guests)

            reservationViewModel.setConfirmationNumber(confirmationNumber)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ReservationConfirmationFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun String.convertToIsoDate(): String {
    val inputFormat = java.text.SimpleDateFormat("dd-MM-yyyy")
    val outputFormat = java.text.SimpleDateFormat("yyyy-MM-dd")
    return outputFormat.format(inputFormat.parse(this))
}