package com.example.tunehubs

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tunehubtest.models.Track
import com.example.tunehubs.api.RetrofitClient
import kotlinx.coroutines.launch

class TrackAdapter(
    private val tracks: List<Track>,
    private val fragment: Fragment
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackImage: ImageView = itemView.findViewById(R.id.track_image)
        val trackName: TextView = itemView.findViewById(R.id.track_name)
        val artistName: TextView = itemView.findViewById(R.id.artist_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]

        holder.trackName.text = track.name
        holder.artistName.text = track.artist.name

        // Логируем размеры изображений для анализа
        val imageSizes = track.image.map { it.size }
        Log.d("TrackAdapter", "Image sizes: $imageSizes")

        // Попытка найти изображение с размером 'extralarge', если нет - 'large'
        val rawImageUrl = track.image.firstOrNull { it.size == "extralarge" }?.text
            ?: track.image.firstOrNull { it.size == "large" }?.text

        // Формируем полный URL изображения
        val baseUrl = "https://lastfm.freetls.fastly.net"
        val imageUrl = rawImageUrl?.let {
            if (it.startsWith("http")) it else "$baseUrl$it"
        } ?: ""

        // Лог для проверки URL
        Log.d("TrackAdapter", "Image URL: $imageUrl")

        if (imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.trackImage)
        } else {
            holder.trackImage.setImageResource(R.drawable.ic_launcher_background)
        }

        holder.itemView.setOnClickListener {
            // Запросить подробную информацию о треке
            fragment.lifecycleScope.launch {
                val response = RetrofitClient.apiService.getTrackInfo(track.artist.name, track.name)
                val description =
                    if (response.isSuccessful && response.body()?.track?.wiki?.summary != null) {
                        response.body()!!.track.wiki?.summary ?: "Нет описания"
                    } else {
                        "Нет описания"
                    }

                // Обработка изображения для деталей
                val detailImageUrlRaw = track.image.firstOrNull { it.size == "extralarge" }?.text
                    ?: track.image.firstOrNull { it.size == "large" }?.text

                val detailImageUrl = detailImageUrlRaw?.let {
                    if (it.startsWith("http")) it else "$baseUrl$it"
                } ?: ""

                val args =
                    TopTracksFragmentDirections.actionTopTracksFragmentToTrackDetailsFragment(
                        trackName = track.name,
                        artistName = track.artist.name,
                        imageUrl = detailImageUrl,
                        description = description
                    )
                fragment.findNavController().navigate(args)
            }
        }
    }

    override fun getItemCount() = tracks.size
}