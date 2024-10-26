package com.example.playlistmaker.ui.mediateka.favourites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.mediateka.favourites.FavoritesTracksInteractor
import com.example.playlistmaker.ui.mediateka.favourites.models.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesTracksInteractor: FavoritesTracksInteractor) :
    ViewModel() {
    private val _state = MutableLiveData<FavoritesState>()

    val state: LiveData<FavoritesState>
        get() = _state

    fun getFavoritesTracks() {
        viewModelScope.launch {
            favoritesTracksInteractor.getFavoritesTracks().collect() {
                if (it.isEmpty()) {
                    _state.postValue(FavoritesState.Empty)
                } else {
                    _state.postValue(FavoritesState.Content(it))
                }
            }
        }
    }
}


