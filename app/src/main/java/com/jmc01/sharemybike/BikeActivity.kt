package com.jmc01.sharemybike

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.jmc01.sharemybike.databinding.ActivityBikeBinding
import com.jmc01.sharemybike.ui.theme.BikesContent

class BikeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBikeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBikeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BikesContent.loadBikesFromJSON(applicationContext)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_bike)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // La lógica de selección de fecha se maneja en FirstFragment.kt
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_bike)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}