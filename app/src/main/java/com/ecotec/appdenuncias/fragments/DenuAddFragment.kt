package com.ecotec.appdenuncias.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.ecotec.appdenuncias.R
import com.ecotec.appdenuncias.api.RetrofitClient
import com.ecotec.appdenuncias.models.DenunciaRequest
import com.ecotec.appdenuncias.models.DenunciaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.text.SimpleDateFormat

class DenuAddFragment : Fragment() {

    private lateinit var txtTitulo: EditText
    private lateinit var txtDesc: EditText
    private lateinit var txtCiudad: EditText
    private lateinit var txtProvincia: EditText
    private lateinit var txtFecha: EditText
    private lateinit var txtTipo: EditText
    private lateinit var rbPublica: RadioButton
    private lateinit var rbPrivada: RadioButton
    private lateinit var btnEnviar: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_denu_add, container, false)

        txtTitulo = view.findViewById(R.id.txtTitulo)
        txtDesc = view.findViewById(R.id.txtDesc)
        txtCiudad = view.findViewById(R.id.txtCiudad)
        txtProvincia = view.findViewById(R.id.txtProvincia)
        txtFecha = view.findViewById(R.id.txtFecha)
        txtTipo = view.findViewById(R.id.txtTipo)
        rbPublica = view.findViewById(R.id.rbPublica)
        rbPrivada = view.findViewById(R.id.rbPrivada)
        btnEnviar = view.findViewById(R.id.btnEnviar)
        progressBar = view.findViewById(R.id.progressBar)

        // Fecha del día por defecto
        val hoy = Calendar.getInstance()
        val formato = SimpleDateFormat("yyyy-MM-dd")
        txtFecha.setText(formato.format(hoy.time))

        txtFecha.setOnClickListener {
            val fechaActual = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, anio, mes, dia ->
                    val seleccionada = Calendar.getInstance()
                    seleccionada.set(anio, mes, dia)
                    txtFecha.setText(formato.format(seleccionada.time))
                },
                fechaActual.get(Calendar.YEAR),
                fechaActual.get(Calendar.MONTH),
                fechaActual.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        btnEnviar.setOnClickListener { enviarDenuncia() }

        return view
    }

    private fun enviarDenuncia() {
        val titulo = txtTitulo.text.toString().trim()
        val desc = txtDesc.text.toString().trim()
        val ciudad = txtCiudad.text.toString().trim()
        val provincia = txtProvincia.text.toString().trim()
        val fecha = txtFecha.text.toString().trim()
        val tipo = txtTipo.text.toString().trim()

        // Convertir el booleano a String ("publica" o "privada")
        val visibilidadString = if (rbPublica.isChecked) "publica" else "privada"

        if (titulo.isEmpty() || desc.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val prefs = requireActivity().getSharedPreferences("denunciasApp", 0)
        val token = prefs.getString("token", "") ?: ""
        val bearer = "Bearer $token"

        progressBar.visibility = View.VISIBLE

        val request = DenunciaRequest(
            titulo = titulo,
            descripcion = desc,
            ciudad = ciudad,
            provincia = provincia,
            fecha_evento = fecha,
            tipo = tipo,
            visibilidad = visibilidadString
        )

        RetrofitClient.api.addDenuncia(bearer, request).enqueue(object : Callback<DenunciaResponse> {
            override fun onResponse(call: Call<DenunciaResponse>, response: Response<DenunciaResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body()?.ok == true) {
                    Toast.makeText(requireContext(), "Denuncia enviada exitosamente", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                } else {
                    Toast.makeText(requireContext(), response.body()?.error ?: "Error al enviar denuncia", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DenunciaResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun limpiarCampos() {
        txtTitulo.setText("")
        txtDesc.setText("")
        txtCiudad.setText("")
        txtProvincia.setText("")
        // Resetear fecha al día actual
        val formato = SimpleDateFormat("yyyy-MM-dd")
        val hoy = Calendar.getInstance()
        txtFecha.setText(formato.format(hoy.time))
        txtTipo.setText("")
        rbPublica.isChecked = true
    }
}
