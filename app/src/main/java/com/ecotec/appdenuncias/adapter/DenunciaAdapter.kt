package com.ecotec.appdenuncias.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ecotec.appdenuncias.R
import com.ecotec.appdenuncias.models.Denuncia

class DenunciasAdapter(private var lista: List<Denuncia>) :
    RecyclerView.Adapter<DenunciasAdapter.DenunciaViewHolder>() {

    class DenunciaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        val txtTipo: TextView = itemView.findViewById(R.id.txtTipo)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        val txtCiudad: TextView? = itemView.findViewById(R.id.txtCiudad)
        val txtUsuario: TextView? = itemView.findViewById(R.id.txtUsuario) // Poner en tu XML
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenunciaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_denuncia, parent, false)
        return DenunciaViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: DenunciaViewHolder, position: Int) {
        val denuncia = lista[position]
        holder.txtTitulo.text = denuncia.titulo
        holder.txtDescripcion.text = denuncia.descripcion
        holder.txtTipo.text = "Tipo: ${denuncia.tipo}"
        holder.txtFecha.text = denuncia.fecha_evento
        holder.txtCiudad?.text = "${denuncia.ciudad}, ${denuncia.provincia}"

        // Muestra el nombre del usuario solo si viene (públicas)
        if (denuncia.username != null) {
            holder.txtUsuario?.visibility = View.VISIBLE
            holder.txtUsuario?.text = "Por: ${denuncia.username}"
        } else {
            holder.txtUsuario?.visibility = View.GONE
        }
    }

    fun updateData(nuevaLista: List<Denuncia>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
