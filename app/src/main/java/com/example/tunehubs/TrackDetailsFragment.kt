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

        // Установка текста
        view.findViewById<TextView>(R.id.track_name).text = args.trackName
        view.findViewById<TextView>(R.id.artist_name).text = args.artistName
        val descriptionTextView = view.findViewById<TextView>(R.id.description)
        descriptionTextView.text = args.description

        // Установка прокрутки
        descriptionTextView.movementMethod = android.text.method.ScrollingMovementMethod()

        // Загрузка изображения
        val imageView = view.findViewById<ImageView>(R.id.track_image)
        if (args.imageUrl.isNotEmpty()) {
            Glide.with(this).load(args.imageUrl).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background)
        }
    }
}