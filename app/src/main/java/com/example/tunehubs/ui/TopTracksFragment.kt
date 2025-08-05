package com.example.tunehubs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tunehub.viewmodel.MusicViewModel
import com.example.tunehubs.R
import com.example.tunehubtest.models.Track

class TopTracksFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter
    private val tracks = mutableListOf<Track>()

    private val viewModel: MusicViewModel by viewModels()

    private var currentPage = 1
    private val pageSize = 10
    private var isLoading = false
    private var totalPages = Int.MAX_VALUE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.tracks_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = TrackAdapter(tracks, this)
        recyclerView.adapter = adapter

        // Обработка прокрутки для пагинации
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val layoutManager = rv.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && lastVisibleItem >= totalItemCount - 2 && currentPage < totalPages) {
                    currentPage++
                    loadMoreTracks()
                }
            }
        })

        // Наблюдение за данными
        viewModel.tracksResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                val newTracks = it.tracks.track
                if (currentPage == 1) {
                    tracks.clear()
                }
                tracks.addAll(newTracks)
                adapter.notifyDataSetChanged()
                isLoading = false
            }
        }

        // Начальная загрузка
        loadMoreTracks()
    }

    private fun loadMoreTracks() {
        isLoading = true
        viewModel.loadTopTracks(limit = pageSize, page = currentPage)
    }
}