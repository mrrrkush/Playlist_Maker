package com.example.playlistmaker.ui.audioplayer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.mediateka.favourites.FavoritesTracksInteractor
import com.example.playlistmaker.domain.api.mediateka.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.model.playlist.Playlist
import com.example.playlistmaker.domain.model.track.Track
import com.example.playlistmaker.ui.audioplayer.models.FavoriteState
import com.example.playlistmaker.ui.audioplayer.models.PlayerState
import com.example.playlistmaker.ui.mediateka.models.PlaylistsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val track: Track,
    private val playerInteractor: AudioPlayerInteractor,
    private val favoritesTracksInteractor: FavoritesTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var timerJob: Job? = null
    private var trackInFavorites = false

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState>
        get() = _playerState

    private val _isFavorite = MutableLiveData<FavoriteState>()
    val isFavorite: LiveData<FavoriteState>
        get() = _isFavorite

    private val _playlistsState = MutableLiveData<PlaylistsState>()
    val playlistsState: LiveData<PlaylistsState>
        get() = _playlistsState

    init {
        track.previewUrl?.let {
            playerInteractor.createPlayer(it) {
                _playerState.postValue(playerInteractor.getPlayerState())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    fun checkFavoriteBtn() {
        viewModelScope.launch {
            favoritesTracksInteractor.checkTrackById(track!!.trackId)
                .collect { value ->
                    trackInFavorites = value
                    _isFavorite.postValue(FavoriteState(trackInFavorites))
                }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            if (trackInFavorites) {
                favoritesTracksInteractor.deleteTrack(track!!)
                checkFavoriteBtn()
            } else {
                favoritesTracksInteractor.insertTrack(track!!)
                checkFavoriteBtn()
            }
        }
    }

    fun onPlayButtonClicked() {
        when (_playerState.value) {
            is PlayerState.Playing -> {
                onPause()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }

            is PlayerState.Default -> {
            }

            else -> {
            }
        }
    }

    fun getAllPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect {
                _playlistsState.postValue(PlaylistsState.ShowPlaylists(it))
            }
        }
    }

    fun addTrackToPlaylist(playlist: Playlist) {
        if (playlist.tracksId.contains(track!!.trackId.toInt())) {
            _playlistsState.value =
                PlaylistsState.AlreadyAdded(playlist.name)
        } else {
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(track!!, playlist)
                _playlistsState.postValue(PlaylistsState.WasAdded(playlist.name))
            }
        }
    }

    private fun startPlayer() {
        playerInteractor.play()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(UPDATE_DELAY)
                _playerState.postValue(playerInteractor.getPlayerState())
            }
        }
    }

    fun onPause() {
        playerInteractor.pause()
        _playerState.postValue(playerInteractor.getPlayerState())
        timerJob?.cancel()
    }

    companion object {
        private const val UPDATE_DELAY = 300L
    }
}
