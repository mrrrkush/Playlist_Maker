package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var searchText = ""
    private val trackList = mutableListOf<Track>()
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var searchHistory: SearchHistory

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
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
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setupAdapters() {
        searchHistory = SearchHistory(getSharedPreferences("app_prefs", MODE_PRIVATE))
        searchAdapter = TrackAdapter(trackList, searchHistory)
        historyAdapter = TrackAdapter(searchHistory.getHistory(), searchHistory)

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
            if (hasFocus && searchEditText.text.isEmpty() && searchHistory.getHistory().isNotEmpty()) {
                showSearchHistory()
            } else {
                hideSearchHistory()
            }
        }
    }

    private fun handleTextChange(text: String) {
        searchAdapter.filter(text)
        with(binding) {
            if (searchEditText.hasFocus() && text.isEmpty() && searchHistory.getHistory().isNotEmpty()) {
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
                searchHistory.clearHistory()
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
        historyAdapter.submitList(searchHistory.getHistory())
    }

    private fun searchTrack() {
        val searchText = binding.searchEditText.text.toString()
        if (searchText.isNotEmpty()) {
            binding.progressBar.isVisible = true
            RetrofitInstance.api.search(searchText).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    handleSearchResponse(response)
                    binding.progressBar.isVisible = false
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    handleSearchFailure()
                    binding.progressBar.isVisible = false
                }
            })
        }
    }

    private fun handleSearchResponse(response: Response<SearchResponse>) {
        with(binding) {
            Log.d("SearchActivity", "Response code: ${response.code()}")
            Log.d("SearchActivity", "Response body: ${response.body()?.results}")

            if (response.code() == 200) {
                trackList.clear()
            }

            if (response.body()?.results?.isNotEmpty() == true) {
                trackList.addAll(response.body()?.results!!)
                searchAdapter.submitList(trackList)
                recyclerSearch.isVisible = true
                nothingFoundViewStub.isVisible = false
            } else {
                recyclerSearch.isVisible = false
                if (nothingFoundViewStub.parent != null) {
                    nothingFoundViewStub.inflate()
                } else {
                    nothingFoundViewStub.isVisible = true
                }
                binding.searchEditText.requestFocus()
            }
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


