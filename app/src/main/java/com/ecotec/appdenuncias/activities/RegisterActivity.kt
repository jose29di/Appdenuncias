package com.ecotec.appdenuncias.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecotec.appdenuncias.R
import com.ecotec.appdenuncias.api.RetrofitClient
import com.ecotec.appdenuncias.models.RegisterRequest
import com.ecotec.appdenuncias.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtNombres: EditText
    private lateinit var txtSexo: EditText
    private lateinit var txtPalabraSecreta: EditText
    private lateinit var txtRespuestaSecreta: EditText
    private lateinit var txtUsuario: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var txtContrasena2: EditText
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtNombres = findViewById(R.id.txtNombres)
        txtSexo = findViewById(R.id.txtSexo)
        txtPalabraSecreta = findViewById(R.id.txtPalabraSecreta)
        txtRespuestaSecreta = findViewById(R.id.txtPalabraSecretaResp)
        txtUsuario = findViewById(R.id.txtUsuario)
        txtContrasena = findViewById(R.id.txtContrasena)
        txtContrasena2 = findViewById(R.id.txtContrasena2)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val nombres = txtNombres.text.toString().trim()
        val sexo = txtSexo.text.toString().trim().uppercase()
        val hint = txtPalabraSecreta.text.toString().trim()
        val answer = txtRespuestaSecreta.text.toString().trim()
        val usuario = txtUsuario.text.toString().trim()
        val password = txtContrasena.text.toString()
        val password2 = txtContrasena2.text.toString()

        // Validaciones
        if (nombres.isEmpty() || sexo.isEmpty() || hint.isEmpty() || answer.isEmpty() ||
            usuario.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (sexo !in listOf("M", "F", "O")) {
            Toast.makeText(this, "Sexo debe ser M, F u O", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != password2) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        val request = RegisterRequest(
            nombres = nombres,
            sexo = sexo,
            secret_hint = hint,
            secret_answer = answer,
            username = usuario,
            password = password,
            password2 = password2
        )

        RetrofitClient.api.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful && response.body()?.ok == true) {
                    val token = response.body()?.token
                    val user = response.body()?.user

                    Toast.makeText(
                        this@RegisterActivity,
                        "¡Registro exitoso! Bienvenido ${user?.nombres ?: usuario}",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (token != null) {
                        // Guardar token y datos del usuario en SharedPreferences
                        val prefs = getSharedPreferences("denunciasApp", MODE_PRIVATE)
                        prefs.edit().apply {
                            putString("token", token)
                            putString("username", user?.username ?: usuario)
                            putString("nombres", user?.nombres ?: nombres)
                            putInt("user_id", user?.id ?: 0)
                        }.apply()

                        // Abrir MainActivity
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    val errorMsg = response.body()?.error ?: "Error de registro"
                    Toast.makeText(this@RegisterActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}