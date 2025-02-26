package com.example.laliga

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdapterEquipos(
    private var equipos: ArrayList<Equipo>,
    private val context: Context
) : RecyclerView.Adapter<AdapterEquipos.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbarEquipo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_equipo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val equipo = equipos[position]
        holder.toolbar.title = equipo.strTeam


        holder.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_favorito -> {
                    marcarFavorito(equipo)
                    true
                }
                R.id.menu_detalles -> {
                    verDetallesEquipo(equipo)
                    true
                }
                else -> false
            }
        }
    }

    override fun getItemCount(): Int = equipos.size

    private fun marcarFavorito(equipo: Equipo) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("favoritos/$userId/teams")
        ref.child(equipo.idTeam!!).setValue(equipo)
    }

    private fun verDetallesEquipo(equipo: Equipo) {
        val intent = Intent(context, DetalleEquipoFragment::class.java)
        intent.putExtra("equipo", equipo)
        context.startActivity(intent)
    }
    fun actualizarLista(nuevaLista: ArrayList<Equipo>) {
        equipos.clear()
        equipos.addAll(nuevaLista)
        notifyDataSetChanged()
    }

}
