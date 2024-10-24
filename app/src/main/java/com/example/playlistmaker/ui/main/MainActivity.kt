package com.example.playlistmaker.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        mainBinding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioplayerFragment, R.id.newPlaylistFragment -> {
                    mainBinding.bottomNavigationView.isVisible = false
                    mainBinding.bottomNavDivider.isVisible = false
                }
                else -> {
                    mainBinding.bottomNavigationView.isVisible = true
                    mainBinding.bottomNavDivider.isVisible = true
                }
            }
        }
    }
}