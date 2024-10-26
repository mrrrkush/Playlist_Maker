package com.example.playlistmaker.ui.audioplayer.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.models.FavoriteState
import com.example.playlistmaker.ui.audioplayer.models.PlayerState
import com.example.playlistmaker.ui.audioplayer.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.mediateka.models.PlaylistsState
import com.example.playlistmaker.ui.mediateka.playlist.BottomSheetPlaylistsAdapter
import com.example.playlistmaker.ui.mediateka.playlist.activity.DetailPlaylistFragment
import com.example.playlistmaker.util.formatAsTime
import com.example.playlistmaker.util.getCountry
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class AudioPlayerFragment : Fragment() {

    private lateinit var binding: FragmentAudioplayerBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var adapter: BottomSheetPlaylistsAdapter
    private lateinit var track: Track

    private val playerViewModel: AudioPlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().let {
            track = it.getParcelable(ARGS_TRACK)!!
        }
        setPlaylistsRv()
        setBottomSheet()
        checkFavoriteBtn()
        setTrackInfoAndAlbumImg(track)
        setClickListeners()
        playerViewModel.playerState.observe(viewLifecycleOwner) {
            renderPlayerState(it)
        }
        playerViewModel.isFavorite.observe(viewLifecycleOwner) {
            renderFavoriteState(it)
        }
        playerViewModel.playlistsState.observe(viewLifecycleOwner) {
            renderPlaylistsState(it)
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.onPause()
    }

    private fun setClickListeners() {
        binding.newPlaylistToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.addToPlaylistButton.setOnClickListener {
            playerViewModel.getAllPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.mediaButtons.setOnClickListener {
            playerViewModel.onPlayButtonClicked()
        }

        binding.likeButton.setOnClickListener {
            playerViewModel.toggleFavorite()
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_audioplayerFragment_to_createPlaylistFragment,
                DetailPlaylistFragment.createArgs(0)
            )
        }
    }

    private fun setPlaylistsRv() {
        adapter = BottomSheetPlaylistsAdapter { playlist ->
            playerViewModel.addTrackToPlaylist(playlist)
        }
        binding.playlistsBottomSheetRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistsBottomSheetRecyclerview.adapter = adapter
    }

    private fun setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLinear).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = 1 - abs(slideOffset)
            }
        })
    }

    private fun checkFavoriteBtn() {
        playerViewModel.checkFavoriteBtn()
    }

    private fun setTrackInfoAndAlbumImg(track: Track?) {
        if (track != null) {
            with(binding) {
                with(track) {
                    trackTitlePlayer.setTextOrHide(trackName, trackTitlePlayer)
                    trackArtistPlayer.setTextOrHide(artistName, trackArtistPlayer)
                    songTime.setTextOrHide(
                        trackTimeMillis?.formatAsTime(),
                        songTimeTv
                    )
                    songAlbum.setTextOrHide(collectionName, albumTv)
                    songYear.setTextOrHide(getYear(releaseDate), yearTv)
                    songGenre.setTextOrHide(primaryGenreName, genreTv)
                    songCountry.setTextOrHide(getCountry(country), countryTv)

                    Glide.with(requireContext())
                        .load(getCoverArtwork(artworkUrl100))
                        .placeholder(R.drawable.placeholder_player)
                        .transform(
                            CenterCrop(),
                            RoundedCorners(
                                resources.getDimensionPixelSize(R.dimen.search_radius_padding_8dp)
                            ),
                        )
                        .into(trackCoverPlayer)
                }
            }
        }
    }

    private fun TextView.setTextOrHide(text: String?, view: TextView) {
        if (text.isNullOrEmpty()) {
            this.visibility = View.GONE
            view.visibility = View.GONE
        } else {
            this.text = text
        }
    }

    private fun renderPlayerState(state: PlayerState) {
        if (state.buttonIsPlay) {
            showPlayBtn()
        } else {
            showPauseBtn()
        }
        if (!state.isPlayButtonEnabled) {
            showNotReady()
        }
        binding.timeOfSongPlay.text = state.progress.toLong().formatAsTime()
    }

    private fun renderFavoriteState(state: FavoriteState) {
        if (state.isFavorite) {
            binding.likeButton.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.like_button_favourite
                )
            )
        } else {
            binding.likeButton.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.like_button
                )
            )
        }
    }

    private fun renderPlaylistsState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.AlreadyAdded -> showToast(
                getString(
                    R.string.track_already_added_to_playlist,
                    state.playlistName
                )
            )

            is PlaylistsState.WasAdded -> showTrackAdded(state.playlistName)
            is PlaylistsState.ShowPlaylists -> {
                adapter.setPlaylists(state.playlists)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showTrackAdded(playlistName: String) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        showToast(getString(R.string.added_to_playlist, playlistName))
    }

    private fun showNotReady() {
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.GONE
        binding.mediaButtons.isEnabled = false
    }

    private fun showPlayBtn() {
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.GONE
        binding.mediaButtons.isEnabled = true
    }

    private fun showPauseBtn() {
        binding.playButton.visibility = View.GONE
        binding.pauseButton.visibility = View.VISIBLE
        binding.mediaButtons.isEnabled = true

        binding.pauseButton.setOnClickListener {
            playerViewModel.onPlayButtonClicked()
        }
    }

    private fun getCoverArtwork(artworkUrl100: String?) =
        artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    private fun getYear(date: String?) =
        date?.substringBefore('-')

    companion object {
        const val ARGS_TRACK = "track"

        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)
    }
}




