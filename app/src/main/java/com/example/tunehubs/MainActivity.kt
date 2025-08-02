package com.example.tunehubs

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tunehub.viewmodel.MusicViewModel
import com.google.gson.GsonBuilder

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MusicViewModel
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MusicViewModel::class.java]
        resultTextView = findViewById(R.id.resultTextView)

        findViewById<Button>(R.id.fetchDataButton).setOnClickListener {
            resultTextView.text = "Загрузка данных..."
            viewModel.loadTopTracks()
        }

        findViewById<Button>(R.id.testPerformanceButton).setOnClickListener {
            resultTextView.text = "Тестирование производительности API..."
            viewModel.testApiPerformance(10)
        }

        viewModel.tracksResponse.observe(this) { response ->
            try {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonString = gson.toJson(response)
                resultTextView.text = "Результат запроса:\n$jsonString"
            } catch (e: Exception) {
                resultTextView.text = "Ошибка форматирования JSON: ${e.message}"
            }
        }

        viewModel.error.observe(this) { error ->
            resultTextView.text = "Ошибка: $error"
        }

        viewModel.performanceResults.observe(this) { results ->
            resultTextView.text = results
        }
    }
}