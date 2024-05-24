package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewStub
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SearchActivity : AppCompatActivity() {
    private var searchText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backFromSearch = findViewById<ImageView>(R.id.back_from_search)
        backFromSearch.setOnClickListener {
            val backFromSearchIntent = Intent(this, MainActivity::class.java)
            startActivity(backFromSearchIntent)
        }
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val clearButton: ImageButton = findViewById(R.id.clearSearchButton)

        val trackList = mutableListOf<Track>()

        val nothingFoundStub: ViewStub? = findViewById(R.id.nothingFoundViewStub)
        val noConnectionStub: ViewStub? = findViewById(R.id.noConnectionViewStub)
        val recyclerSearch: RecyclerView = findViewById(R.id.recyclerSearch)
        val historyText: TextView = findViewById(R.id.historyText)
        val clearHistoryButton: AppCompatButton = findViewById(R.id.clearHistoryButton)
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPreferences)
        val searchAdapter = TrackAdapter(trackList, searchHistory)
        recyclerSearch.adapter = searchAdapter
        recyclerSearch.layoutManager = LinearLayoutManager(this)

        val historyAdapter = TrackAdapter(searchHistory.getHistory(), searchHistory)

        fun updateHistory() {
            historyAdapter.submitList(searchHistory.getHistory())
        }
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
             if (hasFocus && searchEditText.text.isEmpty() && searchHistory.getHistory().isNotEmpty()) {
                 historyText.isVisible = true
                 recyclerSearch.isVisible = true
                 recyclerSearch.adapter = historyAdapter
                 clearHistoryButton.isVisible = true
                 updateHistory()
                 clearHistoryButton.setOnClickListener {
                     searchHistory.clearHistory()
                     updateHistory()
                     historyText.isVisible = false
                     clearHistoryButton.isVisible = false
                     recyclerSearch.isVisible = false
                     searchEditText.clearFocus()
                     hideKeyboard(searchEditText)
                     recyclerSearch.adapter = searchAdapter
                 }
             } else {
                 historyText.isVisible = false
                 clearHistoryButton.isVisible = false
                 recyclerSearch.adapter = searchAdapter
             }
        }

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString("searchText", "")
            searchEditText.setText(searchText)
        }

        fun searchTrack() {
            recyclerSearch.adapter = searchAdapter
            val searchText = searchEditText.text.toString()
            if (searchText.isNotEmpty()) {
                RetrofitInstance.api.search(searchText).enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                        if (response.code() == 200) {
                            trackList.clear()
                        }
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            searchAdapter.submitList(trackList)
                            recyclerSearch.isVisible = true
                            nothingFoundStub?.isVisible = false
                        }
                        if (trackList.isEmpty()) {
                            recyclerSearch.isVisible = false
                            if (nothingFoundStub?.parent != null) nothingFoundStub.inflate() else nothingFoundStub?.isVisible = true
                            searchEditText.requestFocus()
                        }
                    }
                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        recyclerSearch.isVisible = false
                        if (noConnectionStub?.parent != null) noConnectionStub.inflate() else noConnectionStub?.isVisible = true
                        val updateButton = findViewById<Button>(R.id.updateButton)
                        updateButton.setOnClickListener {
                            searchTrack()
                            noConnectionStub?.isVisible = false
                            recyclerSearch.isVisible = true
                        }
                    }
                })
            }
        }

        searchEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
                v.clearFocus()
                hideKeyboard(v)
                true
            } else false
        }

        searchEditText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                clearButton.isVisible = if (s.isNullOrEmpty()) false else true
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchAdapter.filter(s.toString())
                    if (searchEditText.hasFocus() && s?.isEmpty() == true && searchHistory.getHistory().isNotEmpty()) {
                        historyText.isVisible = true
                        recyclerSearch.isVisible = true
                        recyclerSearch.adapter = historyAdapter
                        clearHistoryButton.isVisible = true
                    } else {
                        historyText.isVisible = false
                        clearHistoryButton.isVisible = false
                        recyclerSearch.adapter = searchAdapter
                    }
            }
        })
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            searchEditText.clearFocus()
            val hideKeyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyboard.hideSoftInputFromWindow(it.windowToken, 0)
            recyclerSearch.isVisible = false
            clearHistoryButton.isVisible = false
            historyText.isVisible = false
            if (nothingFoundStub != null && noConnectionStub != null) {
                nothingFoundStub.isVisible = false
                noConnectionStub.isVisible = false
            }
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
        searchText = savedInstanceState.getString("searchText", searchText)
        findViewById<EditText>(R.id.searchEditText).setText(searchText)
    }

}