package com.example.tunehubs.ui

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
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
import java.util.Calendar
import com.example.tunehubs.NotificationReceiver
import android.app.AlarmManager
import android.app.PendingIntent
import android.widget.Toast

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
        val remindButton = view.findViewById<Button>(R.id.button_remind)

        shareButton.setOnClickListener {
            shareTrack()
        }

        remindButton.setOnClickListener {
            showTimePicker()
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

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()

        val timePicker = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                scheduleNotification(hourOfDay, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(hour: Int, minute: Int) {
        val context = requireContext()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("trackName", args.trackName)
            putExtra("artistName", args.artistName)
            putExtra("description", args.description)
            putExtra("imageUrl", args.imageUrl)
        }

        val pendingIntentId = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            pendingIntentId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // если выбранное время уже прошло сегодня, добавим сутки
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        Toast.makeText(requireContext(), "Напоминание установлено на $hour:$minute", Toast.LENGTH_SHORT).show()
    }
}