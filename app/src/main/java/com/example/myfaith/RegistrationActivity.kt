package com.example.myfaith

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mynavigationapp.R

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val emailField = findViewById<EditText>(R.id.registration_email)
        val numberField = findViewById<EditText>(R.id.registration_number)
        val nameField = findViewById<EditText>(R.id.registration_name)
        val usernameField = findViewById<EditText>(R.id.registration_username)
        val passwordField = findViewById<EditText>(R.id.registration_password)
        val profilePhoto = findViewById<ImageView>(R.id.profile_photo)
        val registerButton = findViewById<Button>(R.id.registration_button)

        registerButton.setOnClickListener {
            // Navigate to Login after registration
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}