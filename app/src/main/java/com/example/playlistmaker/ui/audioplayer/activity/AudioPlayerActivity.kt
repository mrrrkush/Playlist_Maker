package com.example.playlistmaker.ui.audioplayer.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.app.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.domain.model.track.getCountry
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModelFactory
import com.example.playlistmaker.ui.audioplayer.view_model.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioplayerBinding
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val previewUrl = intent.getStringExtra("previewUrl") ?: run {
            finish()
            return
        }

        viewModel = ViewModelProvider(this, AudioPlayerViewModelFactory((application as App).provideAudioPlayerInteractor())).get(AudioPlayerViewModel::class.java)

        setupWindowInsets()
        setupTrackInfo()
        setupPlaybackControls()

        viewModel.playerState.observe(this) { state ->
            handlePlayerState(state)
        }

        viewModel.currentPosition.observe(this) { position ->
            binding.timeOfSongPlay.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(position)
        }

        viewModel.prepare(previewUrl)
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupTrackInfo() {
        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val trackTimeMillis = intent.getStringExtra("trackTimeMillis")
        val artworkUrl100 = intent.getStringExtra("artworkUrl100")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")

        binding.trackTitlePlayer.text = trackName
        binding.trackArtistPlayer.text = artistName
        binding.songTime.text = trackTimeMillis

        val modifiedArtworkUrl = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        Glide.with(this)
            .load(modifiedArtworkUrl)
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.placeholder)
            .into(binding.trackCoverPlayer)

        collectionName?.let {
            binding.songAlbum.text = it
        } ?: run {
            binding.songAlbum.isVisible = false
            binding.albumTv.isVisible = false
        }

        if (releaseDate != null) {
            val year = releaseDate.substring(0, 4)
            binding.songYear.text = year
        } else {
            binding.songYear.isVisible = false
            binding.yearTv.isVisible = false
        }

        primaryGenreName?.let {
            binding.songGenre.text = it
        } ?: run {
            binding.songGenre.isVisible = false
            binding.genreTv.isVisible = false
        }

        val countryName = getCountry(country)
        country?.let {
            binding.songCountry.text = countryName
        } ?: run {
            binding.songCountry.isVisible = false
            binding.countryTv.isVisible = false
        }

        binding.backFromAudioplayer.setOnClickListener {
            finish()
        }
    }

    private fun setupPlaybackControls() {
        binding.playButton.setOnClickListener {
            viewModel.play()
        }

        binding.pauseButton.setOnClickListener {
            viewModel.pause()
        }
    }

    private fun handlePlayerState(state: PlayerState) {
        when (state) {
            PlayerState.PREPARED -> {
                binding.playButton.isVisible = true
                binding.pauseButton.isVisible = false
            }
            PlayerState.PLAYING -> {
                binding.playButton.isVisible = false
                binding.pauseButton.isVisible = true
            }
            PlayerState.PAUSED -> {
                binding.playButton.isVisible = true
                binding.pauseButton.isVisible = false
            }
            PlayerState.DEFAULT -> {
                binding.playButton.isVisible = true
                binding.pauseButton.isVisible = false
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}




