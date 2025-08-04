package com.example.tunehubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tunehub.viewmodel.MusicViewModel
import com.example.tunehubtest.models.Track

class TopTracksFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter
    private val tracks = mutableListOf<Track>()

    private val viewModel: MusicViewModel by viewModels()

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

        // Подписка на LiveData
        viewModel.tracksResponse.observe(viewLifecycleOwner) { response ->
            val newTracks = response?.tracks?.track ?: emptyList()
            tracks.clear()
            tracks.addAll(newTracks)
            adapter.notifyDataSetChanged()
        }

        // Загрузка топ треков
        viewModel.loadTopTracks()
    }
}
