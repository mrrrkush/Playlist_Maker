package com.example.playlistmaker.ui.mediateka.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentOpenPlaylistBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerFragment
import com.example.playlistmaker.ui.mediateka.view_model.OpenPlaylistViewModel
import com.example.playlistmaker.ui.track.TrackAdapter
import com.example.playlistmaker.util.formatAsMinutes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpenPlaylistFragment : Fragment() {

    private var _binding: FragmentOpenPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<OpenPlaylistViewModel>()

    var playlist: Playlist? = null

    private val tracksAdapter by lazy {
        TrackAdapter({ showPlayer(track = it) }, { showLongClickOnTrack(track = it) })
    }

    private val bottomSheetBehavior
        get() = BottomSheetBehavior.from(binding.bottomSheetSharing).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        initListeners()
        initObservers()
        initBottomSheet()

        viewModel.getPlaylist()
    }

    private fun initBottomSheet() {
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                        binding.bottomSheetLinear.isVisible = true
                    }

                    else -> {
                        binding.overlay.isVisible = true
                        binding.bottomSheetLinear.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun initListeners() {
        binding.newPlaylistToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.playlistShareIv.setOnClickListener {
            sharingPlaylist()
        }
        binding.sharePlaylistTv.setOnClickListener {
            sharingPlaylist()
        }
        binding.playlistMoreMenuIv.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.deletePlaylistTv.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            context?.let { context ->
                MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
                    .setMessage(requireContext().getString(R.string.do_you_want_to_delete_playlist))
                    .setNegativeButton(R.string.no) { dialog, which -> }
                    .setPositiveButton(R.string.yes) { dialog, which ->
                        playlist?.let { playlist ->
                            viewModel.deletePlaylist(playlist.id)
                        }
                        findNavController().popBackStack()
                    }.show()
            }
        }
        binding.editInformationTv.setOnClickListener {
            findNavController().navigate(
                R.id.action_openPlaylistFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlist!!)
            )
        }
    }

    private fun initAdapters() {
        binding.playlistTracksRv.adapter = tracksAdapter
    }

    private fun sharingPlaylist() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        if (viewModel.isEmptyTracks()) {
            Toast.makeText(
                requireContext(),
                R.string.you_do_not_have_any_tracks_in_playlist,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val sharePlaylist = viewModel.sharePlaylist(resources)
            viewModel.shareTracks(sharePlaylist)
        }
    }

    private fun initObservers() {

        viewModel.playlist.observe(viewLifecycleOwner) {
            showPlaylist(it)
            playlist = it
            if (playlist?.description == "") {
                binding.playlistDescription.isVisible = false
            }
            playlist?.id?.let { playlist_id -> viewModel.getTracks(playlist_id) }
            viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
                if (tracks.isEmpty()) {
                    tracksAdapter.tracks = arrayListOf()
                    binding.textNotFound.isVisible = true
                } else {
                    tracksAdapter.tracks = tracks as ArrayList<Track>
                    binding.playlistTimeTv.text = resources.getQuantityString(
                        R.plurals.plural_minutes,
                        tracksAdapter.tracks.sumOf { it.trackTimeMillis }.formatAsMinutes().toInt(),
                        tracksAdapter.tracks.sumOf { it.trackTimeMillis }.formatAsMinutes()
                    )
                }
            }
        }
        viewModel.getPlaylist()
        playlist?.let { showPlaylist(it) }
    }

    private fun showPlaylist(playlist: Playlist) {

        with(binding) {

            if (playlist.imageUri.toString() != "null") {
                playlistImage.setImageURI(playlist.imageUri?.toUri())
                playlistImageBottomSheet.setImageURI(playlist.imageUri?.toUri())
            } else {
                context?.let {
                    Glide
                        .with(it)
                        .load(R.drawable.placeholder)
                        .transform(CenterInside())
                        .into(playlistImage)
                }
            }

            playlistTitle.text = playlist.title
            playlistTitleBottomSheet.text = playlist.title

            if (playlistDescription.text.isNullOrEmpty()) {
                playlistDescription.text = playlist.description
                playlistDescription.isVisible = true
            } else {
                playlistDescription.isVisible = false
            }

            playlistSize.text = pluralizeWord(playlist.size, TRACK_NAME)

            playlistSizeBottomSheet.text = pluralizeWord(playlist.size, TRACK_NAME)

            binding.playlistTimeTv.text = resources.getQuantityString(
                R.plurals.plural_minutes,
                tracksAdapter.tracks.sumOf { it.trackTimeMillis }.formatAsMinutes().toInt(),
                tracksAdapter.tracks.sumOf { it.trackTimeMillis }.formatAsMinutes()
            )
        }
    }

    private fun showPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_openPlaylistFragment_to_audioplayerFragment,
            AudioPlayerFragment.createArgs(track)
        )
    }

    private fun showLongClickOnTrack(track: Track) {
        showConfirmDialog(track)
    }

    private fun showConfirmDialog(track: Track) {
        context?.let { context ->
            MaterialAlertDialogBuilder(requireContext(),  R.style.MyDialogTheme)
                .setMessage(requireContext().getString(R.string.are_you_sure_to_delete_track))
                .setNegativeButton(R.string.no) { dialog, which -> }
                .setPositiveButton(R.string.yes) { dialog, which ->
                    playlist?.let { playlist ->
                        viewModel.deleteTracks(track, playlist)
                        val indexToRemove =
                            tracksAdapter.tracks.indexOfFirst { it.trackId == track.trackId }
                        if (indexToRemove != DELETE_TRACK_FROM_PLAYLIST) {
                            indexToRemove.let { tracksAdapter.tracks.removeAt(it) }
                            indexToRemove.let { tracksAdapter.notifyItemRemoved(it) }
                        }
                    }
                }.show()
        }
    }

    private fun pluralizeWord(number: Int, word: String): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "$number $word"
            number % 10 in 2..4 && (number % 100 < 10 || number % 100 >= 20) -> "$number $word${
                if (word.endsWith('а')) "и" else "а"}"
            else -> "$number $word${if (word.endsWith('а')) "" else "ов"}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DELETE_TRACK_FROM_PLAYLIST = -1
        private const val TRACK_NAME = "трек"
    }
}