package com.example.tunehubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tunehub.viewmodel.MusicViewModel


class TopTracksFragment : Fragment() {

    private lateinit var viewModel: MusicViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Раздуваем макет фрагмента
        return inflater.inflate(R.layout.fragment_top_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем ViewModel
        viewModel = ViewModelProvider(this)[MusicViewModel::class.java]

        // Находим RecyclerView в разметке
        val recyclerView = view.findViewById<RecyclerView>(R.id.tracks_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Подписываемся на изменения данных и обновляем адаптер
        viewModel.tracksResponse.observe(viewLifecycleOwner) { tracksResponse ->
            val tracks = tracksResponse?.tracks?.track ?: emptyList()
            val adapter = TrackAdapter(tracks)
            recyclerView.adapter = adapter
        }

        // Загружаем данные
        viewModel.loadTopTracks()
    }
}
