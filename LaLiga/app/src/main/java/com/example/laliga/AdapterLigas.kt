package com.example.laliga

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.laliga.databinding.ItemLigaBinding

class AdapterLigas(
    private var listaLigas: ArrayList<League>,
    private var context: Context
) : RecyclerView.Adapter<AdapterLigas.ViewHolder>() {

    class ViewHolder(var binding: ItemLigaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(liga: League, context: Context) {
            binding.nombreItem.text = liga.strSport
            binding.toolbarLiga.title = liga.strLeague


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLigaBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listaLigas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val liga = listaLigas[position]
        holder.bind(liga, context)

        holder.itemView.setOnClickListener { view ->
            val bundle = bundleOf("idLiga" to liga.idLeague)
            view.findNavController().navigate(R.id.action_fragmentLigas_to_detalleEquipoFragment, bundle)
        }
    }


    fun actualizarLista(nuevaLiga:List<League>){
        this.listaLigas.addAll(nuevaLiga)
        notifyDataSetChanged()
    }
}
