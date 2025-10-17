package com.ecotec.appdenuncias.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ecotec.appdenuncias.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 2000 // 2 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Ocultar ActionBar si existe
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            verificarSesion()
        }, SPLASH_DELAY)
    }

    private fun verificarSesion() {
        val prefs = getSharedPreferences("denunciasApp", MODE_PRIVATE)
        val token = prefs.getString("token", "")

        val intent = if (token.isNullOrEmpty()) {
            // No hay sesión activa, ir a login
            Intent(this, LoginActivity::class.java)
        } else {
            // Hay sesión activa, ir a MainActivity
            Intent(this, MainActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}