package com.example.playlistmaker.ui.audioplayer.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.models.PlayerState
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.util.getCountry
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var playerBinding: ActivityAudioplayerBinding
    private val viewModel by viewModel<AudioPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerBinding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(playerBinding.root)

        initListeners()

        val track = intent.getSerializableExtra("track") as Track
        track.previewUrl?.let { viewModel.prepare(it) }

        viewModel.observeState().observe(this) { state ->
            playerBinding.playButton.setOnClickListener {
                controller(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                playerBinding.timeOfSongPlay.text = getString(R.string.default_song_time)
                setPlayIcon()
            }
        }

        viewModel.checkIsFavourite(track.trackId)

        playerBinding.likeButton.setOnClickListener {
            viewModel.onFavouriteClicked(track)
        }

        viewModel.observeTime().observe(this) {
            playerBinding.timeOfSongPlay.text = it
        }

        viewModel.observeIsFavourite().observe(this) {
                isFavorite ->
            playerBinding.likeButton.setImageResource(
                if (isFavorite) R.drawable.like_button_favourite else R.drawable.like_button
            )
        }

        showTrack(track)
    }

    private fun showTrack(track: Track) {
        playerBinding.apply {
            Glide
                .with(trackCoverPlayer)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.search_radius_padding_8dp)))
                .into(trackCoverPlayer)

            trackTitlePlayer.text = track.trackName
            trackArtistPlayer.text = track.artistName
            songGenre.text = track.primaryGenreName
            songCountry.text = getCountry(track.country)

            songTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            val date =
                track.releaseDate?.let {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                        it
                    )
                }
            if (date != null) {
                val formattedDatesString =
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                songYear.text = formattedDatesString
            }

            if (track.collectionName.isNotEmpty()) {
                songAlbum.text = track.collectionName
            } else {
                albumTv.isVisible = false
                songAlbum.isVisible = false
            }
        }
    }

    private fun controller(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                viewModel.play()
                setPauseIcon()
            }

            PlayerState.STATE_PLAYING -> {
                viewModel.pause()
                setPlayIcon()
            }
        }
    }

    private fun initListeners() {
        playerBinding.toolbarInclude.toolbar.apply {
            title = ""
            setSupportActionBar(this)
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun setPlayIcon() {
        playerBinding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun setPauseIcon() {
        playerBinding.playButton.setImageResource(R.drawable.pause_button)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}




