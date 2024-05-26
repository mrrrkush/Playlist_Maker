package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audioplayer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val trackTimeMillis = intent.getLongExtra("trackTimeMillis", 0)
        val artworkUrl100 = intent.getStringExtra("artworkUrl100")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getIntExtra("releaseDate", 0)
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")

        findViewById<TextView>(R.id.trackTitlePlayer).text = trackName
        findViewById<TextView>(R.id.trackArtistPlayer).text = artistName
        findViewById<TextView>(R.id.songTime).text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

        val modifiedArtworkUrl = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        Glide.with(this)
            .load(modifiedArtworkUrl)
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.placeholder)
            .into(findViewById(R.id.trackCoverPlayer))

        collectionName?.let {
            findViewById<TextView>(R.id.songAlbum).text = it
        } ?: run {
            findViewById<TextView>(R.id.songAlbum).isVisible = false
            findViewById<TextView>(R.id.album_tv).isVisible = false
        }

        if (releaseDate != 0) {
            findViewById<TextView>(R.id.songYear).text = releaseDate.toString()
        } else {
            findViewById<TextView>(R.id.songYear).isVisible = false
            findViewById<TextView>(R.id.year_tv).isVisible = false
        }

        primaryGenreName?.let {
            findViewById<TextView>(R.id.songGenre).text = it
        } ?: run {
            findViewById<TextView>(R.id.songGenre).isVisible = false
            findViewById<TextView>(R.id.genre_tv).isVisible = false
        }

        val countryName = getCountry(country)
        country?.let {
            findViewById<TextView>(R.id.songCountry).text = countryName
        } ?: run {
            findViewById<TextView>(R.id.songCountry).isVisible = false
            findViewById<TextView>(R.id.country_tv).isVisible = false
        }

        val backFromPlayer = findViewById<ImageView>(R.id.back_from_player)
        backFromPlayer.setOnClickListener {
            finish()
        }
    }
}
