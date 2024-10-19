package com.example.playlistmaker.ui.mediateka.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.mediateka.models.FavouriteTracksState
import com.example.playlistmaker.ui.mediateka.view_model.FavouriteTrackViewModel
import com.example.playlistmaker.ui.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTrackFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteTracksBinding
    private val viewModel by viewModel<FavouriteTrackViewModel>()

    private val favoritesTracksAdapter = TrackAdapter {
        showPlayer(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
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
            val intent = Intent(requireContext(), AudioPlayerActivity::class.java).apply {
                putExtra("track", track)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavouriteTracks()
    }

    companion object {
        fun newInstance() = FavouriteTrackFragment().apply {}
    }
}




