package com.example.myfaith.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfaith.datasource.ApiSource
import com.example.myfaith.entity.LoginResponse
import com.example.myfaith.utils.hashPassword
import com.example.mynavigationapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)
        if (!token.isNullOrEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val emailOrNumberField = findViewById<EditText>(R.id.login_email_or_number)
        val passwordField = findViewById<EditText>(R.id.login_password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<TextView>(R.id.register_text)

        val prefillEmail = intent.getStringExtra("email")
        val prefillPassword = intent.getStringExtra("password")

        prefillEmail?.let {
            emailOrNumberField.setText(it)
            emailOrNumberField.requestFocus()
        }

        val textView = findViewById<TextView>(R.id.register_text)
        val fullText = "Don't have an account? Sign up"
        val spannable = SpannableString(fullText)

        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#f2f2ec")),
            0,
            fullText.indexOf("Sign up"),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#f4ff9d")),
            fullText.indexOf("Sign up"),
            fullText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannable

        textView.text = spannable

        loginButton.setOnClickListener {
            val email = emailOrNumberField.text.toString()
            val password = passwordField.text.toString()

            ApiSource.login.loginUser(email, password).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val token = response.body()!!.token
                        val sharedPrefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        sharedPrefs.edit().putString("auth_token", token).apply()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}