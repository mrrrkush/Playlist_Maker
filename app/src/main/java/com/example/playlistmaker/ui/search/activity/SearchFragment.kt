package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.track.TrackAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private var searchJob: Job? = null

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWindowInsets()
        setupView()
        setupAdapters()
        setupSearchEditText()

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            handleSearchResponse(tracks)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.searchHistory.observe(viewLifecycleOwner) { history ->
            updateHistory(history)
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupView() {
        binding.backFromSearch.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupAdapters() {
        searchAdapter = TrackAdapter(emptyList(), viewModel.searchHistoryInteractor)
        historyAdapter = TrackAdapter(emptyList(), viewModel.searchHistoryInteractor)

        binding.recyclerSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearchEditText() {
        with(binding) {
            searchEditText.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    handleSearchFocus(hasFocus)
                }
                setOnEditorActionListener { v, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        searchTrack()
                        v.clearFocus()
                        hideKeyboard(v)
                        true
                    } else false
                }
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        clearSearchButton.isVisible = !s.isNullOrEmpty()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        searchJob?.cancel()
                        searchJob = CoroutineScope(Dispatchers.Main).launch {
                            delay(2000)
                            searchTrack()
                        }
                        handleTextChange(s.toString())
                    }
                })
            }

            clearSearchButton.setOnClickListener {
                clearSearch()
            }
        }
    }

    private fun handleSearchFocus(hasFocus: Boolean) {
        with(binding) {
            if (hasFocus && searchEditText.text.isEmpty() && viewModel.searchHistory.value?.isNotEmpty() == true) {
                showSearchHistory()
            } else {
                hideSearchHistory()
            }
        }
    }

    private fun handleTextChange(text: String) {
        searchAdapter.filter(text)
        with(binding) {
            if (searchEditText.hasFocus() && text.isEmpty() && viewModel.searchHistory.value?.isNotEmpty() == true) {
                showSearchHistory()
            } else {
                hideSearchHistory()
            }
        }
    }

    private fun showSearchHistory() {
        with(binding) {
            historyText.isVisible = true
            recyclerSearch.isVisible = true
            recyclerSearch.adapter = historyAdapter
            clearHistoryButton.isVisible = true
            clearHistoryButton.setOnClickListener {
                viewModel.clearSearchHistory()
                hideSearchHistory()
                searchEditText.clearFocus()
                hideKeyboard(searchEditText)
                recyclerSearch.adapter = searchAdapter
            }
        }
    }

    private fun hideSearchHistory() {
        with(binding) {
            historyText.isVisible = false
            clearHistoryButton.isVisible = false
            recyclerSearch.adapter = searchAdapter
        }
    }

    private fun updateHistory(history: List<Track>) {
        historyAdapter.submitList(history)
    }

    private fun searchTrack() {
        val searchText = binding.searchEditText.text.toString()
        if (searchText.isNotEmpty()) {
            viewModel.searchTrack(searchText)
        }
    }

    private fun handleSearchResponse(foundTracks: List<Track>) {
        with(binding) {
            searchAdapter.submitList(foundTracks)
            recyclerSearch.isVisible = foundTracks.isNotEmpty()
            nothingFoundViewStub.isVisible = foundTracks.isEmpty()
        }
    }

    private fun clearSearch() {
        with(binding) {
            searchEditText.text.clear()
            searchEditText.clearFocus()
            hideKeyboard(searchEditText)
            recyclerSearch.isVisible = false
            clearHistoryButton.isVisible = false
            historyText.isVisible = false
            nothingFoundViewStub.isVisible = false
            noConnectionViewStub.isVisible = false
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchText", binding.searchEditText.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val searchText = savedInstanceState?.getString("searchText", "")
        binding.searchEditText.setText(searchText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
    }
}





