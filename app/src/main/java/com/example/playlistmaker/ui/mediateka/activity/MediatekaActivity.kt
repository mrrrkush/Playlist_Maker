package com.example.playlistmaker.ui.mediateka.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.example.playlistmaker.ui.MediatekaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaActivity : AppCompatActivity() {

    private lateinit var mediaBinding: ActivityMediatekaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaBinding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(mediaBinding.root)

        mediaBinding.viewPager.adapter = MediatekaViewPagerAdapter(supportFragmentManager, lifecycle)

        mediaBinding.mediaToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        tabMediator = TabLayoutMediator(mediaBinding.tabLayout, mediaBinding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favourite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}