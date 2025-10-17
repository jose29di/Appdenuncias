package com.ecotec.appdenuncias.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.ecotec.appdenuncias.R
import com.ecotec.appdenuncias.api.RetrofitClient
import com.ecotec.appdenuncias.models.UpdateProfileRequest
import com.ecotec.appdenuncias.models.UpdateProfileResponse
import com.ecotec.appdenuncias.models.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var txtNombres: EditText
    private lateinit var txtSexo: EditText
    private lateinit var txtUsuario: EditText
    private lateinit var txtPalabraSecreta: EditText
    private lateinit var txtRespuestaSecreta: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var btnActualizar: Button
    private lateinit var progressProfile: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        txtNombres = view.findViewById(R.id.txtNombres)
        txtSexo = view.findViewById(R.id.txtSexo)
        txtUsuario = view.findViewById(R.id.txtUsuario)
        txtPalabraSecreta = view.findViewById(R.id.txtPalabraSecreta)
        txtRespuestaSecreta = view.findViewById(R.id.txtRespuestaSecreta)
        txtContrasena = view.findViewById(R.id.txtContrasena)
        btnActualizar = view.findViewById(R.id.btnActualizar)
        progressProfile = view.findViewById(R.id.progressProfile)

        cargarPerfil()

        btnActualizar.setOnClickListener { actualizarPerfil() }

        return view
    }

    private fun cargarPerfil() {
        progressProfile.visibility = View.VISIBLE
        val prefs = requireActivity().getSharedPreferences("denunciasApp", 0)
        val token = prefs.getString("token", "") ?: ""
        val bearer = "Bearer $token"

        RetrofitClient.api.getMe(bearer).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                progressProfile.visibility = View.GONE
                if (response.isSuccessful && response.body()?.ok == true) {
                    response.body()?.user?.let { user ->
                        txtNombres.setText(user.nombres)
                        txtSexo.setText(user.sexo)
                        txtUsuario.setText(user.username)
                        txtPalabraSecreta.setText("")
                        txtRespuestaSecreta.setText("")
                        txtContrasena.setText("")
                    }
                } else {
                    Toast.makeText(requireContext(), response.body()?.error ?: "Error al cargar perfil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                progressProfile.visibility = View.GONE
                Toast.makeText(requireContext(), "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarPerfil() {
        val prefs = requireActivity().getSharedPreferences("denunciasApp", 0)
        val token = prefs.getString("token", "") ?: ""
        val bearer = "Bearer $token"

        val request = UpdateProfileRequest(
            nombres = txtNombres.text.toString().trim().takeIf { it.isNotEmpty() },
            sexo = txtSexo.text.toString().trim().takeIf { it.isNotEmpty() },
            username = txtUsuario.text.toString().trim().takeIf { it.isNotEmpty() },
            password = txtContrasena.text.toString().trim().takeIf { it.isNotEmpty() },
            secret_hint = txtPalabraSecreta.text.toString().trim().takeIf { it.isNotEmpty() },
            secret_answer = txtRespuestaSecreta.text.toString().trim().takeIf { it.isNotEmpty() }
        )

        progressProfile.visibility = View.VISIBLE

        RetrofitClient.api.updateProfile(bearer, request).enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                progressProfile.visibility = View.GONE
                if (response.isSuccessful && response.body()?.ok == true) {
                    Toast.makeText(requireContext(), "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show()
                    cargarPerfil() // Recargar datos actualizados
                } else {
                    Toast.makeText(requireContext(), response.body()?.error ?: "Error al actualizar perfil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                progressProfile.visibility = View.GONE
                Toast.makeText(requireContext(), "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}