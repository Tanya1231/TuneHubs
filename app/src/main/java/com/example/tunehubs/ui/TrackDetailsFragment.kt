package com.example.tunehubs.ui

import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tunehubs.R

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

        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_slide)

        val trackName = view.findViewById<TextView>(R.id.track_name)
        trackName.text = args.trackName
        trackName.startAnimation(animation)

        val artistName = view.findViewById<TextView>(R.id.artist_name)
        artistName.text = args.artistName
        artistName.startAnimation(animation)

        val descriptionTextView = view.findViewById<TextView>(R.id.description)
        descriptionTextView.text = args.description
        descriptionTextView.movementMethod = android.text.method.ScrollingMovementMethod()
        descriptionTextView.startAnimation(animation)

        val imageView = view.findViewById<ImageView>(R.id.track_image)
        if (args.imageUrl.isNotEmpty()) {
            Glide.with(this).load(args.imageUrl).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background)
        }
        imageView.startAnimation(animation)

        val shareButton = view.findViewById<Button>(R.id.button_share)
        shareButton.setOnClickListener {
            shareTrack()
        }
    }

    private fun shareTrack() {
        val shareText = """
            Послушайте этот трек:
            Название: ${args.trackName}
            Исполнитель: ${args.artistName}
            Описание: ${args.description}
            Посмотреть изображение: ${args.imageUrl}
        """.trimIndent()

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, "Поделиться треком"))
    }
}