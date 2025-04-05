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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfaith.datasource.ApiSource
import com.example.myfaith.entity.RegistrationResponse
import com.example.myfaith.utils.hashPassword
import com.example.mynavigationapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val hashedPassword = hashPassword(passwordField.text.toString())

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
            val email = emailField.text.toString().trim()
            val number = numberField.text.toString().trim()
            val name = nameField.text.toString().trim()
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || number.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hashedPassword = hashPassword(password)

            ApiSource.registration.registerUser(email, number, name, username, hashedPassword)
                .enqueue(object : Callback<RegistrationResponse> {
                    override fun onResponse(
                        call: Call<RegistrationResponse>,
                        response: Response<RegistrationResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val token = response.body()!!.token
                            val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            prefs.edit().putString("auth_token", token).apply()

                            Toast.makeText(this@RegistrationActivity, "Registration successful", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@RegistrationActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                        Toast.makeText(this@RegistrationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }


        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}