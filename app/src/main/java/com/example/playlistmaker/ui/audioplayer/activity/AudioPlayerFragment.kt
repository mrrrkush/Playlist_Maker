package com.example.playlistmaker.ui.audioplayer.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.models.PlayerState
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.playlist.PlaylistsAdapter
import com.example.playlistmaker.ui.playlist.ViewObjects
import com.example.playlistmaker.util.getCountry
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment() {

    private var _playerBinding: FragmentAudioplayerBinding? = null
    private val playerBinding get() = _playerBinding!!

    private val viewModel by viewModel<AudioPlayerViewModel>()

    private val bottomSheetPlaylistsAdapter = PlaylistsAdapter(viewObject = ViewObjects.Vertical)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _playerBinding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return playerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
        initAdapters()

        val track = requireArguments().getSerializable("track") as Track
        val bottomSheetBehavior =
            BottomSheetBehavior.from(playerBinding.bottomSheetLinear).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        playerBinding.overlay.isVisible = false
                    }

                    else -> {
                        playerBinding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        track.previewUrl?.let { viewModel.prepare(it) }

        track.trackId.let { viewModel.checkIsFavourite(it) }

        playerBinding.likeButton.setOnClickListener {
            viewModel.onFavouriteClicked(track)
        }

        bottomSheetPlaylistsAdapter.onPlayListClicked = {
            viewModel.addTrackToPlayList(track, it)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        playerBinding.addToPlaylistButton.setOnClickListener {
            viewModel.fillData()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        showTrack(track)
    }

    private fun initListeners() {
        playerBinding.toolbarInclude.toolbar.apply {
            setOnClickListener {
                findNavController().popBackStack()
            }
        }
        playerBinding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }
    }

    private fun initAdapters() {
        playerBinding.playlistsBottomSheetRecyclerview.adapter = bottomSheetPlaylistsAdapter
    }

    private fun initObservers() {
        viewModel.observeIsFavourite().observe(viewLifecycleOwner) { isFavorite ->
            playerBinding.likeButton.setImageResource(
                if (isFavorite) R.drawable.like_button_favourite else R.drawable.like_button
            )
        }
        viewModel.observeTime().observe(viewLifecycleOwner) {
            playerBinding.timeOfSongPlay.text = it
        }
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            playerBinding.playButton.setOnClickListener {
                controller(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                playerBinding.timeOfSongPlay.text = getString(R.string.default_song_time)
                setPlayIcon()
            }
        }
        viewModel.isAlreadyInPlaylist.observe(viewLifecycleOwner) {
            val message =
                if (it.second) "Добавлено в плейлист ${it.first}" else "Трек уже добавлен в плейлист ${it.first}"

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        viewModel.playlists.observe(viewLifecycleOwner) {
            bottomSheetPlaylistsAdapter.playlists = it as ArrayList<Playlist>
            viewModel.fillData()
        }
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

    private fun setPlayIcon() {
        playerBinding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun setPauseIcon() {
        playerBinding.playButton.setImageResource(R.drawable.pause_button)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.reset()
        _playerBinding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    companion object {
        fun createArgs(track: Track): Bundle {
            return bundleOf("track" to track)
        }
    }
}




