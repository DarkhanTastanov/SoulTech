package com.example.myfaith

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mynavigationapp.R

class ZikrActivity : AppCompatActivity() {
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zikr)

        val counterView = findViewById<TextView>(R.id.zikr_counter)
        val addButton = findViewById<Button>(R.id.zikr_add_button)
        val resetButton = findViewById<Button>(R.id.zikr_reset_button)

        addButton.setOnClickListener {
            counter++
            counterView.text = counter.toString()
        }

        resetButton.setOnClickListener {
            counter = 0
            counterView.text = counter.toString()
        }
    }
}