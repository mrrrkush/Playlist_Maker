package com.example.playlistmaker.ui.mediateka.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerFragment
import com.example.playlistmaker.ui.mediateka.models.FavouriteTracksState
import com.example.playlistmaker.ui.mediateka.view_model.FavouriteTrackViewModel
import com.example.playlistmaker.ui.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTrackFragment : Fragment() {

    private var _binding: FragmentFavouriteTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavouriteTrackViewModel>()

    private val favoritesTracksAdapter = TrackAdapter {
        showPlayer(track = it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favouriteTracksRecycler.adapter = favoritesTracksAdapter

        viewModel.getFavouriteTracks()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FavouriteTracksState) {
        when (state) {
            is FavouriteTracksState.Content -> {
                favoritesTracksAdapter.clearTracks()
                favoritesTracksAdapter.tracks = state.tracks as MutableList<Track>
                binding.favouriteTracksRecycler.visibility = View.VISIBLE
                binding.nothingFound.visibility = View.GONE
            }

            is FavouriteTracksState.Empty -> {
                binding.favouriteTracksRecycler.visibility = View.GONE
                binding.nothingFound.visibility = View.VISIBLE
            }
        }
    }

    private fun showPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_audioplayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavouriteTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavouriteTrackFragment().apply {}
    }
}




