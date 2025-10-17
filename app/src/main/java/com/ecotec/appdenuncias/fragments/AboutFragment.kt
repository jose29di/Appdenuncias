package com.ecotec.appdenuncias.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ecotec.appdenuncias.R
import com.ecotec.appdenuncias.api.RetrofitClient
import com.ecotec.appdenuncias.models.AboutResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class AboutFragment : Fragment() {

    private lateinit var txtAbout: TextView
    private lateinit var progressAbout: ProgressBar
    private lateinit var txtVersionInfo: TextView
    private lateinit var txtDevInfo: TextView
    private lateinit var txtMailInfo: TextView
    private lateinit var txtWebInfo: TextView
    private lateinit var txtUpdateInfo: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        txtAbout = view.findViewById(R.id.txtAbout)
        progressAbout = view.findViewById(R.id.progressAbout)

        txtVersionInfo = view.findViewById(R.id.txtVersionInfo)
        txtDevInfo = view.findViewById(R.id.txtDevInfo)
        txtMailInfo = view.findViewById(R.id.txtMailInfo)
        txtWebInfo = view.findViewById(R.id.txtWebInfo)
        txtUpdateInfo = view.findViewById(R.id.txtUpdateInfo)

        cargarAbout()

        return view
    }

    private fun cargarAbout() {
        val prefs = requireActivity().getSharedPreferences("denunciasApp", 0)
        val token = prefs.getString("token", "") ?: ""
        val bearer = "Bearer $token"

        progressAbout.visibility = View.VISIBLE

        RetrofitClient.api.getAbout(bearer).enqueue(object : Callback<AboutResponse> {
            override fun onResponse(call: Call<AboutResponse>, response: Response<AboutResponse>) {
                progressAbout.visibility = View.GONE
                if (response.isSuccessful && response.body()?.ok == true) {
                    val info = response.body()?.info
                    if (info != null) {
                        txtAbout.text = info.description ?: getString(R.string.about_description_default)
                        txtVersionInfo.text = getString(R.string.about_version, info.version.orEmpty())
                        txtDevInfo.text = getString(R.string.about_developer, info.developer.orEmpty())
                        txtMailInfo.text = getString(R.string.about_contact, info.contact_email.orEmpty())
                        txtWebInfo.text = getString(R.string.about_web, info.website.orEmpty())
                        txtUpdateInfo.text = getString(R.string.about_update, info.last_update.orEmpty())

                    } else {
                        txtAbout.text = getString(R.string.about_info_default)

                        txtVersionInfo.text = ""
                        txtDevInfo.text = ""
                        txtMailInfo.text = ""
                        txtWebInfo.text = ""
                        txtUpdateInfo.text = ""
                    }
                } else {
                    txtAbout.text = response.body()?.error ?: "Error al cargar información"
                    txtVersionInfo.text = ""
                    txtDevInfo.text = ""
                    txtMailInfo.text = ""
                    txtWebInfo.text = ""
                    txtUpdateInfo.text = ""
                }
            }

            override fun onFailure(call: Call<AboutResponse>, t: Throwable) {
                progressAbout.visibility = View.GONE
                txtAbout.text = "Error de conexión: ${t.message}"
                txtVersionInfo.text = ""
                txtDevInfo.text = ""
                txtMailInfo.text = ""
                txtWebInfo.text = ""
                txtUpdateInfo.text = ""
            }
        })
    }
}
