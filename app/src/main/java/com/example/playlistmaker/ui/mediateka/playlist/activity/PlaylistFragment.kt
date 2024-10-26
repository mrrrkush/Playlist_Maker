package com.example.playlistmaker.ui.mediateka.playlist.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.ui.mediateka.models.PlaylistState
import com.example.playlistmaker.ui.mediateka.playlist.PlaylistAdapter
import com.example.playlistmaker.ui.mediateka.playlist.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var adapter: PlaylistAdapter
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPlaylistRv()
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediatekaFragment_to_createPlaylistFragment,
                DetailPlaylistFragment.createArgs(0)
            )
        }

        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setPlaylistRv() {
        adapter = PlaylistAdapter { playlist ->
            findNavController().navigate(
                R.id.action_mediatekaFragment_to_detailPlaylistFragment,
                DetailPlaylistFragment.createArgs(playlist.id)
            )
        }
        binding.playlistsGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsGrid.adapter = adapter
    }

    private fun renderState(state: PlaylistState) {
        when (state) {
            is PlaylistState.Empty -> showEmpty()
            is PlaylistState.Content -> showContent(state.playlists)
        }
    }

    private fun showEmpty() {
        adapter.setPlaylists(emptyList())
        binding.playlistsGrid.visibility = View.GONE
        binding.nothingFound.visibility = View.VISIBLE
    }

    private fun showContent(playlists: List<Playlist>) {
        adapter.setPlaylists(playlists)
        binding.nothingFound.visibility = View.GONE
        binding.playlistsGrid.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}