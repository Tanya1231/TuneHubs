package com.example.tunehub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunehub.repository.MusicRepository
import com.example.tunehubs.models.TrackDetailInfo
import com.example.tunehubtest.models.TopTracksResponse
import com.example.tunehubtest.models.Tracks
import kotlinx.coroutines.launch

class MusicViewModel(private val repository: MusicRepository = MusicRepository()) : ViewModel() {

    private val _tracksResponse = MutableLiveData<TopTracksResponse?>()
    val tracksResponse: LiveData<TopTracksResponse?> = _tracksResponse

    private val _trackDetail = MutableLiveData<TrackDetailInfo?>()
    val trackDetail: LiveData<TrackDetailInfo?> = _trackDetail

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadTopTracks(limit: Int = 10, page: Int = 1) {
        _loading.value = true
        viewModelScope.launch {
            repository.getTopTracks(limit, page).fold(
                onSuccess = { response ->
                    val currentTracks = _tracksResponse.value?.tracks?.track ?: emptyList()
                    val newTracks = response.tracks.track

                    if (page == 1) {
                        _tracksResponse.value = response
                    } else {
                        val combinedTracks = currentTracks + newTracks
                        _tracksResponse.value = TopTracksResponse(
                            tracks = Tracks(track = combinedTracks)
                        )
                    }
                    _loading.value = false
                },
                onFailure = { e ->
                    _error.value = e.message ?: "Неизвестная ошибка"
                    _loading.value = false
                }
            )
        }
    }

    fun getTrackInfo(artist: String, track: String) {
        _loading.value = true
        viewModelScope.launch {
            repository.getTrackInfo(artist, track).fold(
                onSuccess = { response ->
                    _trackDetail.value = response.track
                    _loading.value = false
                },
                onFailure = { e ->
                    _error.value = e.message ?: "Неизвестная ошибка"
                    _loading.value = false
                }
            )
        }
    }
}