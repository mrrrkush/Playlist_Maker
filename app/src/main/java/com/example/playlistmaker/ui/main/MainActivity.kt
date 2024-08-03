package com.example.playlistmaker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.mediateka.MediatekaActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val searchButton = binding.searchButton
        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
           val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(searchIntent)
             }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        val mediaButton = binding.mediatekaButton
        mediaButton.setOnClickListener {
            val mediatekaIntent = Intent(this, MediatekaActivity::class.java)
            startActivity(mediatekaIntent)
        }

        val settingsButton = binding.settingsButton
        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}