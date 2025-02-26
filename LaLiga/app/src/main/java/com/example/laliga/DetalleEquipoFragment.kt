package com.example.laliga



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.laliga.databinding.FragmentDetalleEquipoBinding
import com.google.gson.Gson

class DetalleEquipoFragment : Fragment() {
    private lateinit var binding: FragmentDetalleEquipoBinding
    private var equipo: Equipo? = null
    private lateinit var adapterEquipos: AdapterEquipos
    private var listaEquipos: ArrayList<Equipo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        equipo = arguments?.getSerializable("equipo") as? Equipo
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalleEquipoBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRecyclerView() {
        adapterEquipos = AdapterEquipos(listaEquipos, requireContext())
        binding.recyclerViewEquipos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEquipos.adapter = adapterEquipos
    }

    private fun mostrarEquipos(listaEquipos: List<Equipo>) {
        this.listaEquipos.clear()
        this.listaEquipos.addAll(listaEquipos)
        adapterEquipos.notifyDataSetChanged()
    }

    private fun cargarEquiposLiga(idLiga: String) {
        val urlEquipos = "https://www.thesportsdb.com/api/v1/json/3/lookup_all_teams.php?id=$idLiga"
        val gson = Gson()

        val peticion = JsonObjectRequest(urlEquipos, { response ->
            if (!response.has("teams") || response.isNull("teams")) {
                Log.e("API_ERROR", "No se encontraron equipos para la liga $idLiga")
                return@JsonObjectRequest
            }

            val resultado = response.getJSONArray("teams")
            val listaEquipos = ArrayList<Equipo>()

            for (i in 0 until resultado.length()) {
                val equipoJSON = resultado.getJSONObject(i)
                val equipo = gson.fromJson(equipoJSON.toString(), Equipo::class.java)
                listaEquipos.add(equipo)
            }

            mostrarEquipos(listaEquipos)

        }, { error ->
            Log.e("API_ERROR", "Error al obtener equipos: ${error.message}")
        })

        Volley.newRequestQueue(requireContext()).add(peticion)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idLiga = arguments?.getString("idLiga") ?: return
        cargarEquiposLiga(idLiga) // Ya tienes esta función en tu código
    }



    companion object {
        fun newInstance(equipo: Equipo): DetalleEquipoFragment {
            val fragment = DetalleEquipoFragment()
            val args = Bundle()
            args.putSerializable("equipo", equipo)
            fragment.arguments = args
            return fragment
        }
    }
}
