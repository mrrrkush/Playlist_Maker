package com.example.playlistmaker.presentation.ui.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.searchHistory.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.track.TrackAdapter


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var searchText = ""
    private val trackList = mutableListOf<Track>()
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var trackInteractor: TrackInteractor
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()

        trackInteractor = Creator.provideTrackInteractor()
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(sharedPref)

        setupView()
        setupAdapters()
        setupSearchEditText()
        savedInstanceState?.let {
            searchText = it.getString("searchText", "")
            binding.searchEditText.setText(searchText)
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupView() {
        binding.backFromSearch.setOnClickListener {
            finish()
        }
    }

    private fun setupAdapters() {
        searchAdapter = TrackAdapter(trackList, searchHistoryInteractor)
        historyAdapter = TrackAdapter(searchHistoryInteractor.getHistory(), searchHistoryInteractor)

        binding.recyclerSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
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
                        handleTextChange(s.toString())
                        searchRunnable?.let { handler.removeCallbacks(it) }
                        searchRunnable = Runnable { searchTrack() }
                        handler.postDelayed(searchRunnable!!, 2000)
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
            if (hasFocus && searchEditText.text.isEmpty() && searchHistoryInteractor.getHistory().isNotEmpty()) {
                showSearchHistory()
            } else {
                hideSearchHistory()
            }
        }
    }

    private fun handleTextChange(text: String) {
        searchAdapter.filter(text)
        with(binding) {
            if (searchEditText.hasFocus() && text.isEmpty() && searchHistoryInteractor.getHistory().isNotEmpty()) {
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
            updateHistory()
            clearHistoryButton.setOnClickListener {
                searchHistoryInteractor.clearHistory()
                updateHistory()
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

    private fun updateHistory() {
        historyAdapter.submitList(searchHistoryInteractor.getHistory())
    }

    private fun searchTrack() {
        val searchText = binding.searchEditText.text.toString()
        if (searchText.isNotEmpty()) {
            binding.progressBar.isVisible = true
            trackInteractor.searchTrack(searchText, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTrack: List<Track>) {
                    runOnUiThread {
                        handleSearchResponse(foundTrack)
                        binding.progressBar.isVisible = false
                    }
                }
            })
        }
    }

    private fun handleSearchResponse(foundTracks: List<Track>) {
        with(binding) {
            trackList.clear()
            trackList.addAll(foundTracks)
            searchAdapter.submitList(trackList)
            recyclerSearch.isVisible = trackList.isNotEmpty()
            nothingFoundViewStub.isVisible = trackList.isEmpty()
        }
    }

    private fun handleSearchFailure() {
        with(binding) {
            recyclerSearch.isVisible = false
            if (noConnectionViewStub.parent != null) {
                noConnectionViewStub.inflate()
            } else {
                noConnectionViewStub.isVisible = true
            }
            findViewById<Button>(R.id.updateButton).setOnClickListener {
                noConnectionViewStub.isVisible = false
                recyclerSearch.isVisible = true
                searchTrack()
            }
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
        outState.putString("searchText", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString("searchText", "")
        binding.searchEditText.setText(searchText)
    }
}


