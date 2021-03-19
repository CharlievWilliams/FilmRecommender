package com.charlievwwilliams.filmrecommender

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.charlievwwilliams.filmrecommender.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        setupUI()
    }

    private fun setupUI() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.toolbar.setupWithNavController(navController)
    }

}