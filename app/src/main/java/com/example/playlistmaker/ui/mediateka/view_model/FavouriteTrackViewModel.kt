package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.mediateka.FavouriteTracksInteractor
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.mediateka.models.FavouriteTracksState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouriteTrackViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor
) : ViewModel() {

    private var isClickAllowed = true
    private val stateLiveData = MutableLiveData<FavouriteTracksState>()
    fun observeState(): LiveData<FavouriteTracksState> = stateLiveData

    fun getFavouriteTracks() {
        viewModelScope.launch {
            favouriteTracksInteractor
                .getFavoritesTracks()
                .collect { tracks ->
                    if (tracks.isEmpty()) {
                        renderState(FavouriteTracksState.Empty)
                    }
                    else {
                        renderState(FavouriteTracksState.Content(tracks))
                    }
                }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            viewModelScope.launch {
                delay(2000L)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun renderState(state: FavouriteTracksState) {
        stateLiveData.postValue(state)
    }
}


