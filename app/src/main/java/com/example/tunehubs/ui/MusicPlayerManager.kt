package com.example.tunehubs.ui

import android.media.MediaPlayer
import com.example.tunehubtest.models.Track

object MusicPlayerManager {
    private var mediaPlayer: MediaPlayer? = null
    private var playlist: List<Track> = emptyList()
    private var currentIndex: Int = -1

    interface PlayerStateListener {
        fun onTrackChanged(track: Track)
        fun onPlaybackStateChanged(isPlaying: Boolean)
    }

    var listener: PlayerStateListener? = null

    fun setPlaylist(tracks: List<Track>, startIndex: Int = 0) {
        playlist = tracks
        currentIndex = startIndex
        playCurrent()
    }

    fun playCurrent() {
        if (playlist.isEmpty() || currentIndex !in playlist.indices) return

        val track = playlist[currentIndex]
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(track.url)
            prepareAsync()
            setOnPreparedListener {                it.start()
                listener?.onTrackChanged(track)
                listener?.onPlaybackStateChanged(true)
            }
            setOnCompletionListener {
                next()
            }
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        listener?.onPlaybackStateChanged(false)
    }

    fun play() {
        mediaPlayer?.start()
        listener?.onPlaybackStateChanged(true)
    }

    fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) pause() else play()
        }
    }

    fun next() {
        if (playlist.isEmpty()) return
        currentIndex = (currentIndex + 1) % playlist.size
        playCurrent()
    }

    fun previous() {
        if (playlist.isEmpty()) return
        currentIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
        playCurrent()
    }

    fun getCurrentTrack(): Track? {
        return if (playlist.isNotEmpty() && currentIndex in playlist.indices) playlist[currentIndex] else null
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}