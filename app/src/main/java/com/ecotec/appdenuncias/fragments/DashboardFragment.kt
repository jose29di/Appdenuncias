package com.ecotec.appdenuncias.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ecotec.appdenuncias.R
import com.ecotec.appdenuncias.adapter.DenunciasAdapter
import com.ecotec.appdenuncias.api.RetrofitClient
import com.ecotec.appdenuncias.models.DashboardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var txtTotal: TextView
    private lateinit var txtPublicas: TextView
    private lateinit var txtPrivadas: TextView
    private lateinit var recyclerUltimas: RecyclerView
    private lateinit var progressDashboard: ProgressBar
    private lateinit var denunciasAdapter: DenunciasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        txtTotal = view.findViewById(R.id.txtTotal)
        txtPublicas = view.findViewById(R.id.txtPublicas)
        txtPrivadas = view.findViewById(R.id.txtPrivadas)
        recyclerUltimas = view.findViewById(R.id.recyclerUltimas)
        progressDashboard = view.findViewById(R.id.progressDashboard)
        recyclerUltimas.layoutManager = LinearLayoutManager(requireContext())
        denunciasAdapter = DenunciasAdapter(emptyList())
        recyclerUltimas.adapter = denunciasAdapter

        swipeRefresh.setOnRefreshListener {
            cargarDashboard()
        }

        cargarDashboard()
        return view
    }

    private fun cargarDashboard() {
        swipeRefresh.isRefreshing = true
        progressDashboard.visibility = View.VISIBLE

        val prefs = requireActivity().getSharedPreferences("denunciasApp", 0)
        val token = prefs.getString("token", "") ?: ""
        val bearer = "Bearer $token"

        RetrofitClient.api.getDashboard(bearer).enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                swipeRefresh.isRefreshing = false
                progressDashboard.visibility = View.GONE
                if (response.isSuccessful && response.body()?.ok == true) {
                    val data = response.body()!!
                    txtTotal.text = data.total.toString()
                    txtPublicas.text = data.publicas.toString()
                    txtPrivadas.text = data.privadas.toString()

                    val denuncias = data.ultimas.ifEmpty { data.items ?: emptyList() }
                    denunciasAdapter.updateData(denuncias)
                } else {
                    Toast.makeText(requireContext(), "Error al cargar dashboard", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                progressDashboard.visibility = View.GONE
                Toast.makeText(requireContext(), "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
