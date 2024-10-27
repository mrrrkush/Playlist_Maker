package com.example.playlistmaker.ui.mediateka.playlist.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerFragment
import com.example.playlistmaker.ui.mediateka.models.DetailPlaylistState
import com.example.playlistmaker.ui.mediateka.playlist.DetailPlaylistAdapter
import com.example.playlistmaker.ui.mediateka.playlist.view_model.DetailPlaylistViewModel
import com.example.playlistmaker.util.formatAsMinutes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class DetailPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistDetailBinding
    private lateinit var trackBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var playlistId: Int = 0
    private var playlistName: String = ""
    private val viewModel: DetailPlaylistViewModel by viewModel()

    private val adapter = DetailPlaylistAdapter(
        object : DetailPlaylistAdapter.TrackClickListener {
            override fun onTrackClick(track: Track) {
                findNavController().navigate(
                    R.id.action_detailPlaylistFragment_to_audioplayerFragment,
                    AudioPlayerFragment.createArgs(track = track)
                )
            }

            override fun onTrackLongClick(track: Track): Boolean {
                showDeleteTrackDialog(track)
                return true
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        binding.root.doOnNextLayout {
            calculatePeekHeight()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().let {
            playlistId = it.getInt(ARGS_PLAYLIST)
        }
        viewModel.getPlaylistById(playlistId)
        setBottomSheets()
        setTracksRv()
        setClickListeners()
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun setBottomSheets() {
        trackBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLinear).apply {
            state = STATE_COLLAPSED
        }
        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetSharing).apply {
            state = STATE_HIDDEN
            addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        STATE_HIDDEN -> {
                            binding.overlay.isVisible = false
                        }

                        else -> {
                            binding.overlay.isVisible = true
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = 1 - abs(slideOffset)
                }
            })
        }
    }

    private fun setTracksRv() {
        binding.playlistTracksRv.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistTracksRv.adapter = adapter
    }

    private fun setClickListeners() {
        with(binding) {
            newPlaylistToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            playlistShareIv.setOnClickListener {
                menuBottomSheetBehavior.state = STATE_HIDDEN
                viewModel.sharePlaylist(requireContext())
            }

            playlistMoreMenuIv.setOnClickListener {
                menuBottomSheetBehavior.state = STATE_COLLAPSED
                bottomSheetSharing.isVisible = true
            }

            sharePlaylistTv.setOnClickListener {
                menuBottomSheetBehavior.state = STATE_HIDDEN
                viewModel.sharePlaylist(requireContext())
            }

            binding.editInformationTv.setOnClickListener {
                findNavController().navigate(
                    R.id.action_detailPlaylistFragment_to_createPlaylistFragment,
                    CreatePlaylistFragment.createArgs(playlistId)
                )
            }

            binding.deletePlaylistTv.setOnClickListener {
                menuBottomSheetBehavior.state = STATE_HIDDEN
                MaterialAlertDialogBuilder(
                    requireContext(),
                    R.style.MaterialAlertDialog
                )
                    .setTitle(getString(R.string.title_delete_playlist_dialog, playlistName))
                    .setPositiveButton(getString(R.string.yes))
                    { _, _ ->
                        viewModel.deletePlaylist()
                    }
                    .setNegativeButton(getString(R.string.no))
                    { _, _ -> }
                    .show()
            }
        }
    }

    private fun renderState(state: DetailPlaylistState) {
        when (state) {
            is DetailPlaylistState.Content -> {
                showPlaylistInfo(state.playlist)
                showTrackRv(state.trackList)
            }

            is DetailPlaylistState.Message -> {
                showToast(state.text)
            }

            is DetailPlaylistState.PlaylistDeleted -> {
                findNavController().navigateUp()
            }
        }
    }

    private fun showPlaylistInfo(playlist: Playlist) {
        with(binding) {
            with(playlist) {
                val coverUri = if (coverUri.toString().isNotBlank()) coverUri else R.drawable.placeholder_player
                Glide.with(root)
                    .load(coverUri)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            resources.getDimensionPixelSize(R.dimen.search_radius_padding_8dp)
                        )
                    )
                    .into(playlistImage)

                Glide.with(root)
                    .load(coverUri)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            resources.getDimensionPixelSize(R.dimen.radius_2dp)
                        )
                    )
                    .into(playlistImageBottomSheet)

                playlistTitle.text = name
                playlistDescription.text = description
                playlistSize.text = resources.getQuantityString(
                    R.plurals.plural_tracks,
                    numberOfTracks,
                    numberOfTracks
                )
                playlistTimeTv.text = resources.getQuantityString(
                    R.plurals.plural_minutes,
                    longToMinInt(playlistDuration),
                    playlistDuration.formatAsMinutes()
                )
                playlistTitleBottomSheet.text = name
                playlistSizeBottomSheet.text = resources.getQuantityString(
                    R.plurals.plural_tracks,
                    numberOfTracks,
                    numberOfTracks
                )
            }
        }
    }


    private fun showTrackRv(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            binding.textNotFound.isVisible = true
            binding.playlistTracksRv.isVisible = false
        } else {
            adapter.setTracks(tracks)
            binding.textNotFound.isVisible = false
            binding.playlistTracksRv.isVisible = true
        }

    }

    private fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MaterialAlertDialog
        )
            .setTitle(getString(R.string.are_you_sure_to_delete_track))
            .setPositiveButton(getString(R.string.yes))
            { _, _ ->
                viewModel.deleteTrack(track)
            }
            .setNegativeButton(getString(R.string.no))
            { _, _ -> }
            .show()
    }

    private fun showToast(text: String) {
        Toast.makeText(
            requireContext(),
            text, Toast.LENGTH_SHORT
        ).show()
    }

    private fun longToMinInt(tracksTime: Long): Int {
        return (tracksTime.toInt() / 60000)
    }

    private fun calculatePeekHeight() {
        val screenHeight = binding.root.height
        menuBottomSheetBehavior.peekHeight =
            (screenHeight - binding.playlistTitle.bottom).coerceAtLeast(
                BS_MIN_SIZE_PX
            )
        trackBottomSheetBehavior.peekHeight =
            (screenHeight - binding.playlistShareIv.bottom - BS_TRACKS_OFFSET_PX).coerceAtLeast(
                BS_MIN_SIZE_PX
            )
        trackBottomSheetBehavior.state = STATE_COLLAPSED
    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        private const val BS_TRACKS_OFFSET_PX = 24
        private const val BS_MIN_SIZE_PX = 100

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(ARGS_PLAYLIST to playlistId)
    }
}