package com.example.playlistmaker.presentation.ui.tracks

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.domain.models.getCountry
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    companion object {
        private const val delayMillis = 300L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT
    private lateinit var binding: ActivityAudioplayerBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var handler: Handler
    private lateinit var updateProgressRunnable: Runnable
    private var previewUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewUrl = intent.getStringExtra("previewUrl")
        if (previewUrl == null) {
            finish()
            return
        }

        setupWindowInsets()
        setupTrackInfo()
        setupMediaPlayer()
        setupPlaybackControls()
        preparePlayer()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            binding.playButton.isVisible = true
            binding.pauseButton.isVisible = false
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            binding.playButton.isVisible = true
            binding.pauseButton.isVisible = false
            binding.timeOfSongPlay.text = getResources().getString(R.string.default_song_time)
            handler.removeCallbacks(updateProgressRunnable)
        }
    }

    private fun setupTrackInfo() {
        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val trackTimeMillis = intent.getLongExtra("trackTimeMillis", 0)
        val artworkUrl100 = intent.getStringExtra("artworkUrl100")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")

        binding.trackTitlePlayer.text = trackName
        binding.trackArtistPlayer.text = artistName
        binding.songTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

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
            playbackControl()
        }

        binding.pauseButton.setOnClickListener {
            pausePlayer()
        }
    }

    private fun preparePlayer() {
        mediaPlayer.reset()
        previewUrl?.let {
            mediaPlayer.setDataSource(it)
            mediaPlayer.prepareAsync()
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun startPlayer() {
        if (playerState == STATE_PREPARED || playerState == STATE_PAUSED) {
            mediaPlayer.start()
            playerState = STATE_PLAYING

            binding.playButton.isVisible = false
            binding.pauseButton.isVisible = true

            startProgressUpdater()
        }
    }

    private fun pausePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            playerState = STATE_PAUSED

            binding.playButton.isVisible = true
            binding.pauseButton.isVisible = false
            handler.removeCallbacks(updateProgressRunnable)
        }
    }

    private fun startProgressUpdater() {
        handler = Handler(Looper.getMainLooper())
        updateProgressRunnable = object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    val currentPosition = mediaPlayer.currentPosition
                    binding.timeOfSongPlay.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
                    handler.postDelayed(this, delayMillis)
                }
            }
        }
        handler.post(updateProgressRunnable)
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) {
            pausePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::updateProgressRunnable.isInitialized) {
            handler.removeCallbacks(updateProgressRunnable)
        }
        mediaPlayer.release()
    }
}


