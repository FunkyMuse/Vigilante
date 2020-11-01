package com.crazylegend.vigilante.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onSupportNavigateUp() = binding.navHostContainer.findNavController().navigateUp()

}