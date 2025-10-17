package com.ecotec.appdenuncias.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecotec.appdenuncias.R
import com.ecotec.appdenuncias.api.RetrofitClient
import com.ecotec.appdenuncias.models.LoginRequest
import com.ecotec.appdenuncias.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUsuario: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtUsuario = findViewById(R.id.txtUsuario)
        txtContrasena = findViewById(R.id.txtContrasena)
        btnLogin = findViewById(R.id.btnLogin)
        txtRegistro = findViewById(R.id.txtRegistro)

        btnLogin.setOnClickListener {
            login()
        }

        txtRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        val usuario = txtUsuario.text.toString().trim()
        val password = txtContrasena.text.toString().trim()

        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val request = LoginRequest(username = usuario, password = password)

        RetrofitClient.api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()?.ok == true) {
                    val token = response.body()?.token
                    val user = response.body()?.user

                    if (token != null) {
                        // Guardar token y datos del usuario
                        val prefs = getSharedPreferences("denunciasApp", MODE_PRIVATE)
                        prefs.edit().apply {
                            putString("token", token)
                            putString("username", user?.username ?: "")
                            putString("nombres", user?.nombres ?: "")
                            putInt("user_id", user?.id ?: 0)
                        }.apply()

                        Toast.makeText(
                            this@LoginActivity,
                            "Bienvenido ${user?.nombres ?: usuario}",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    val errorMsg = response.body()?.error ?: "Error al iniciar sesión"
                    Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}