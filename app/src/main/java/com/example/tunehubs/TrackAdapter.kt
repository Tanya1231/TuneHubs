package com.example.tunehubs

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

        val imageUrl = track.image.firstOrNull { it.size == "large" }?.text
            ?: track.image.firstOrNull()?.text

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.trackImage)
        } else {
            holder.trackImage.setImageResource(R.drawable.ic_launcher_background)
        }

        holder.itemView.setOnClickListener {
            // Запросить подробную информацию о треке и передать в детали
            fragment.lifecycleScope.launch {
                val response = RetrofitClient.apiService.getTrackInfo(track.artist.name, track.name)
                val description =
                    if (response.isSuccessful && response.body()?.track?.wiki?.summary != null) {
                        response.body()!!.track.wiki?.summary ?: "Нет описания"
                    } else {
                        "Нет описания"
                    }

                // Получить изображение для деталей
                val detailImageUrl = track.image.firstOrNull { it.size == "large" }?.text ?: ""

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