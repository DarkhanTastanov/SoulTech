package com.example.myfaith.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
        val loginButton = findViewById<TextView>(R.id.sign_in)

        val textView = findViewById<TextView>(R.id.sign_in)
        val fullText = "Already have an account? Sign in"
        val spannable = SpannableString(fullText)

        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#f2f2ec")),
            0,
            fullText.indexOf("Sign in"),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#f4ff9d")),
            fullText.indexOf("Sign in"),
            fullText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannable

        textView.text = spannable

        registerButton.setOnClickListener {
            // Navigate to Login after registration
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}