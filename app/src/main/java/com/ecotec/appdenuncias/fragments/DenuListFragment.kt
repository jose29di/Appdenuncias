package com.ecotec.appdenuncias.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecotec.appdenuncias.R
import com.ecotec.appdenuncias.adapter.DenunciasAdapter
import com.ecotec.appdenuncias.api.RetrofitClient
import com.ecotec.appdenuncias.models.DenunciaListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DenuListFragment : Fragment() {
    private lateinit var recyclerDenu: RecyclerView
    private lateinit var progressDenu: ProgressBar
    private lateinit var adapter: DenunciasAdapter

    companion object {
        fun newInstance(tipo: String): DenuListFragment {
            val fragment = DenuListFragment()
            val args = Bundle()
            args.putString("tipo", tipo)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_denu_list, container, false)
        recyclerDenu = view.findViewById(R.id.recyclerDenu)
        progressDenu = view.findViewById(R.id.progressDenu)
        recyclerDenu.layoutManager = LinearLayoutManager(context)
        adapter = DenunciasAdapter(emptyList())
        recyclerDenu.adapter = adapter

        val tipo = arguments?.getString("tipo") ?: "privada"
        cargarDenuncias(tipo)
        return view
    }

    private fun cargarDenuncias(tipo: String) {
        progressDenu.visibility = View.VISIBLE
        val prefs = activity?.getSharedPreferences("denunciasApp", 0)
        val token = prefs?.getString("token", "") ?: ""
        val bearer = "Bearer $token"

        when (tipo) {
            "publica" -> {
                // Públicas: endpoint de todos
                RetrofitClient.api.getDenunciasPublicas(bearer).enqueue(object : Callback<DenunciaListResponse> {
                    override fun onResponse(call: Call<DenunciaListResponse>, response: Response<DenunciaListResponse>) {
                        progressDenu.visibility = View.GONE
                        if (!isAdded) return
                        if (response.isSuccessful && response.body()?.ok == true) {
                            val denuncias = response.body()?.items ?: emptyList()
                            adapter.updateData(denuncias)
                            if (denuncias.isEmpty()) {
                                context?.let { Toast.makeText(it, "No hay denuncias públicas", Toast.LENGTH_SHORT).show() }
                            }
                        } else {
                            context?.let { Toast.makeText(it, "Error al cargar públicas", Toast.LENGTH_SHORT).show() }
                        }
                    }
                    override fun onFailure(call: Call<DenunciaListResponse>, t: Throwable) {
                        progressDenu.visibility = View.GONE
                        if (!isAdded) return
                        context?.let { Toast.makeText(it, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show() }
                    }
                })
            }
            "privada" -> {
                // Solo privadas propias
                RetrofitClient.api.getMisDenuncias(bearer).enqueue(object : Callback<DenunciaListResponse> {
                    override fun onResponse(call: Call<DenunciaListResponse>, response: Response<DenunciaListResponse>) {
                        progressDenu.visibility = View.GONE
                        if (!isAdded) return
                        if (response.isSuccessful && response.body()?.ok == true) {
                            val denuncias = response.body()?.items?.filter { it.visibilidad == "privada" } ?: emptyList()
                            adapter.updateData(denuncias)
                            if (denuncias.isEmpty()) {
                                context?.let { Toast.makeText(it, "No tienes denuncias privadas", Toast.LENGTH_SHORT).show() }
                            }
                        } else {
                            context?.let { Toast.makeText(it, "Error al cargar privadas", Toast.LENGTH_SHORT).show() }
                        }
                    }
                    override fun onFailure(call: Call<DenunciaListResponse>, t: Throwable) {
                        progressDenu.visibility = View.GONE
                        if (!isAdded) return
                        context?.let { Toast.makeText(it, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show() }
                    }
                })
            }
            else -> {
                // "todas" o no se especificó: públicas Y privadas propias (menú "mis denuncias")
                RetrofitClient.api.getMisDenuncias(bearer).enqueue(object : Callback<DenunciaListResponse> {
                    override fun onResponse(call: Call<DenunciaListResponse>, response: Response<DenunciaListResponse>) {
                        progressDenu.visibility = View.GONE
                        if (!isAdded) return
                        if (response.isSuccessful && response.body()?.ok == true) {
                            val denuncias = response.body()?.items ?: emptyList()
                            adapter.updateData(denuncias)
                            if (denuncias.isEmpty()) {
                                context?.let { Toast.makeText(it, "No tienes denuncias", Toast.LENGTH_SHORT).show() }
                            }
                        } else {
                            context?.let { Toast.makeText(it, "Error al cargar denuncias", Toast.LENGTH_SHORT).show() }
                        }
                    }
                    override fun onFailure(call: Call<DenunciaListResponse>, t: Throwable) {
                        progressDenu.visibility = View.GONE
                        if (!isAdded) return
                        context?.let { Toast.makeText(it, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show() }
                    }
                })
            }
        }
    }
}
