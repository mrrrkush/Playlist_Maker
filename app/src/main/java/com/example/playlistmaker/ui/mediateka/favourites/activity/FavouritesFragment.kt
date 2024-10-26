package com.example.playlistmaker.ui.mediateka.favourites.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerFragment
import com.example.playlistmaker.ui.mediateka.favourites.models.FavoritesState
import com.example.playlistmaker.ui.mediateka.favourites.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.track.TrackAdapter
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(), TrackAdapter.TrackClickListener {

    private val viewModel: FavoritesViewModel by viewModel()
    private var isClickAllowed = true
    private val favoritesTrackAdapter = TrackAdapter(this)
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private val onTrackClickDebounce = debounce<Boolean>(
        CLICK_DEBOUNCE_DELAY,
        lifecycleScope,
        false
    ) { param ->
        isClickAllowed = param
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favouriteTracksRecycler.adapter = favoritesTrackAdapter
        viewModel.getFavoritesTracks()
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoritesTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            findNavController().navigate(
                R.id.action_mediatekaFragment_to_audioplayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }
    }

    private fun renderState(state: FavoritesState) {
        when (state) {
            is FavoritesState.Empty -> showPlaceHolder()
            is FavoritesState.Content -> showFavoritesTracks(state.favorites.toMutableList())
        }
    }

    private fun showPlaceHolder() {
        binding.nothingFound.visibility = View.VISIBLE
        binding.favouriteTracksRecycler.visibility = View.GONE
    }

    private fun showFavoritesTracks(favoritesTrack: MutableList<Track>) {
        favoritesTrackAdapter.setTracks(favoritesTrack)
        binding.nothingFound.visibility = View.GONE
        binding.favouriteTracksRecycler.visibility = View.VISIBLE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(true)
        }
        return current
    }

    private fun TrackAdapter.setTracks(content: List<Track>) {
        this.tracks = content.toMutableList()
    }

    companion object {
        fun newInstance() = FavoritesFragment()

        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}




