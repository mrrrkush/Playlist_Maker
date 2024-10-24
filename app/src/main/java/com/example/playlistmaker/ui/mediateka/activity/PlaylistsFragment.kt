package com.example.playlistmaker.ui.mediateka.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.ui.mediateka.ItemDecorator
import com.example.playlistmaker.ui.mediateka.models.PlaylistsScreenState
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.playlist.PlaylistsAdapter
import com.example.playlistmaker.ui.playlist.ViewObjects
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistViewModel>()

    private val playlistsAdapter = PlaylistsAdapter(viewObject = ViewObjects.Horizontal)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.newPlaylistFragment)
        }

        viewModel.fillData()

        binding.playlistsGrid.adapter = playlistsAdapter

        binding.playlistsGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsGrid.setHasFixedSize(true)
        binding.playlistsGrid.addItemDecoration(ItemDecorator(40, 0))

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render()
        }

    }

    private fun render() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsScreenState.Filled -> {
                    showPlaylists(state.playlists)
                }

                is PlaylistsScreenState.Empty -> {
                    binding.playlistsGrid.isVisible = false
                    binding.nothingFound.isVisible = true
                }
            }
        }
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        playlistsAdapter.playlists = playlists as ArrayList<Playlist>
        binding.playlistsGrid.isVisible = true
        binding.nothingFound.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}