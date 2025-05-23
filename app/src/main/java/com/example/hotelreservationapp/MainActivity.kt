package com.example.hotelreservationapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hotelreservationapp.R
import com.example.hotelreservationapp.databinding.ActivityMainBinding
import com.example.hotelreservationapp.view.HotelSearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HotelSearchFragment())
                .commit()
        }
    }
}



