package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.search.NetworkError
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerFragment
import com.example.playlistmaker.ui.search.models.SearchState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _searchBinding: FragmentSearchBinding? = null
    private val searchBinding get() = _searchBinding!!
    private val viewModel by viewModel<SearchViewModel>()

    private var searchInputQuery = ""

    private val trackAdapter = TrackAdapter ({ showPlayer(track = it) }, { showLongClickOnTrack(track = it) })
    private val historyAdapter = TrackAdapter ({ showPlayer(track = it) }, { showLongClickOnTrack(track = it) })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initAdapters()

        searchBinding.searchEditText.clearFocus()

        searchBinding.searchEditText.doOnTextChanged { text, _, _, _ ->
            searchBinding.clearSearchButton.visibility = clearButtonVisibility(text)
            handleHistoryVisibility(text)
            text?.let { viewModel.searchDebounce(it.toString()) }
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.SearchHistory -> {
                historyAdapter.tracks = arrayListOf()
                showHistoryList(state.tracks)
            }

            is SearchState.Loading -> {
                showLoading()
            }

            is SearchState.SearchedTracks -> {
                showSearchResult(state.tracks)
            }

            is SearchState.SearchError -> {
                showErrorMessage(state.error)
            }
        }
    }

    private fun initAdapters() {
        searchBinding.recyclerSearch.adapter = trackAdapter
        searchBinding.searchHistoryRecycler.adapter = historyAdapter
    }

    private fun initListeners() {
        searchBinding.updateButton.setOnClickListener {
            viewModel.searchTracks(searchInputQuery)
        }
        searchBinding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }
        searchBinding.clearSearchButton.setOnClickListener {
            searchClearShowHistory()
        }
    }

    private fun showPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            viewModel.addTrackToHistory(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_audioplayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchClearShowHistory() {
        viewModel.clearSearchText()
        searchBinding.searchEditText.text?.clear()
        clearContent()

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun handleHistoryVisibility(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            searchBinding.searchHistoryLayout.isVisible = true
            searchBinding.historyText.isVisible = true
            searchBinding.clearHistoryButton.isVisible = true
            searchBinding.recyclerSearch.isVisible = false
        } else {
            searchBinding.searchHistoryLayout.isVisible = false
            searchBinding.historyText.isVisible = false
            searchBinding.clearHistoryButton.isVisible = false
            searchBinding.recyclerSearch.isVisible = true
        }
    }

    private fun showHistoryList(tracks: List<Track>) {
        clearContent()
        historyAdapter.tracks.clear()
        historyAdapter.tracks.addAll(tracks.reversed())
        if (tracks.isNotEmpty()) {
            searchBinding.searchHistoryLayout.isVisible = true
            searchBinding.clearHistoryButton.isVisible = true
        }
    }

    private fun showLoading() {
        clearContent()
        searchBinding.progressBar.isVisible = true
    }

    private fun showSearchResult(tracks: List<Track>) {
        clearContent()
        trackAdapter.clearTracks()
        trackAdapter.tracks.addAll(tracks)
        searchBinding.recyclerSearch.isVisible = true
    }

    private fun showErrorMessage(error: NetworkError) {
        clearContent()
        when (error) {
            NetworkError.EMPTY_RESULT -> {
                searchBinding.recyclerSearch.isVisible = false
                searchBinding.noConnectionViewStub.isVisible = false
                searchBinding.searchHistoryLayout.isVisible = false
                searchBinding.nothingFoundViewStub.isVisible = true
                searchBinding.progressBar.isVisible = false
            }

            NetworkError.CONNECTION_ERROR -> {
                searchBinding.recyclerSearch.isVisible = false
                searchBinding.noConnectionViewStub.isVisible = false
                searchBinding.searchHistoryLayout.isVisible = false
                searchBinding.nothingFoundViewStub.isVisible = true
                searchBinding.progressBar.isVisible = false
            }
        }
    }

    private fun clearContent() {
        searchBinding.nothingFoundViewStub.isVisible = false
        searchBinding.noConnectionViewStub.isVisible = false
        searchBinding.searchHistoryLayout.isVisible = false
        searchBinding.recyclerSearch.isVisible = false
        searchBinding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _searchBinding = null
    }

    private fun showLongClickOnTrack(track: Track) {
        //
    }
}






