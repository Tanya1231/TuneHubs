package com.example.tunehubs

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide

class TrackDetailsFragment : Fragment() {

    private val args: TrackDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_track_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackNameTextView: TextView = view.findViewById(R.id.track_name)
        val artistNameTextView: TextView = view.findViewById(R.id.artist_name)
        val descriptionTextView: TextView = view.findViewById(R.id.description)
        val imageView: ImageView = view.findViewById(R.id.track_image)

        trackNameTextView.text = args.trackName
        artistNameTextView.text = args.artistName
        descriptionTextView.text = args.description ?: "Нет описания"

        if (args.imageUrl.isNotEmpty()) {
            Glide.with(this).load(args.imageUrl).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background)
        }
    }
}