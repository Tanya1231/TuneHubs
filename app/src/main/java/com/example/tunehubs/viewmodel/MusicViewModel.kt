package com.example.tunehub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunehub.repository.MusicRepository
import com.example.tunehubs.models.TrackDetailInfo
import com.example.tunehubtest.models.TopTracksResponse
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

    private val _performanceResults = MutableLiveData<String>()
    val performanceResults: LiveData<String> = _performanceResults

    fun loadTopTracks(limit: Int = 10) {
        _loading.value = true
        viewModelScope.launch {
            repository.getTopTracks(limit).fold(
                onSuccess = { response ->
                    _tracksResponse.value = response
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

    fun testApiPerformance(iterations: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repository.testApiPerformance(iterations)
                _performanceResults.value = result
            } catch (e: Exception) {
                _error.value = "Ошибка при тестировании: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}