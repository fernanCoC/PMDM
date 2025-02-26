package com.example.laliga

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.laliga.databinding.FragmentLigasBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson

class FragmentLigas : Fragment() {

    private lateinit var binding: FragmentLigasBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var listaLigas: ArrayList<League>
    private lateinit var listaEquipos: ArrayList<Equipo>
    private lateinit var adapterLigas: AdapterLigas
    private lateinit var adapterEquipos: AdapterEquipos
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private var nombresLigas: ArrayList<String> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        listaLigas = ArrayList()
        listaEquipos = ArrayList()
        adapterLigas = AdapterLigas(listaLigas, requireContext())
        adapterEquipos = AdapterEquipos(listaEquipos, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLigasBinding.inflate(inflater, container, false)

        binding.recyclerViewLigas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewLigas.adapter = adapterLigas

        // Configurar RecyclerView de Equipos
        binding.recyclerViewEquipos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEquipos.adapter = adapterEquipos

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarDatosAPI("Soccer")
        cargarLigasDesdeFirebase()

        binding.spinnerFiltro.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val ligaSeleccionada = nombresLigas[position]
                    filtrarLigas(ligaSeleccionada)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun cargarDatosAPI(deporte: String) {
        val urlConsulta = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
        val gson = Gson()

        val peticion = JsonObjectRequest(urlConsulta, { response ->
            val resultado = response.getJSONArray("leagues")
            listaLigas.clear()
            nombresLigas.clear()

            for (i in 0 until resultado.length()) {
                val ligaJSON = resultado.getJSONObject(i)
                val liga = gson.fromJson(ligaJSON.toString(), League::class.java)
                if (liga.strSport == deporte) {
                    listaLigas.add(liga)
                    nombresLigas.add(liga.strLeague ?: "Desconocida")

                    firebaseDatabase.getReference("leagues").child(liga.idLeague!!)
                        .setValue(liga)
                }
            }

            actualizarSpinner()
            adapterLigas.actualizarLista(listaLigas)

        }, { error ->
            Log.e("API_ERROR", "Error al obtener ligas: ${error.message}")
        })

        Volley.newRequestQueue(requireContext()).add(peticion)
    }

    private fun cargarLigasDesdeFirebase() {
        firebaseDatabase.getReference("leagues").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaLigas.clear()
                nombresLigas.clear()

                for (ligaSnapshot in snapshot.children) {
                    val liga = ligaSnapshot.getValue(League::class.java)
                    if (liga != null) {
                        listaLigas.add(liga)
                        nombresLigas.add(liga.strLeague ?: "Desconocida")
                    }
                }

                actualizarSpinner()
                adapterLigas.actualizarLista(listaLigas)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al obtener ligas: ${error.message}")
            }
        })
    }

    private fun cargarEquiposLiga(idLiga: String?) {
        if (idLiga.isNullOrEmpty()) {
            Log.e("ERROR", "ID de Liga nulo o vacío")
            return
        }

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




    private fun mostrarEquipos(listaEquipos: ArrayList<Equipo>) {
        adapterEquipos.actualizarLista(listaEquipos)
    }

    private fun actualizarSpinner() {
        spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresLigas)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFiltro.adapter = spinnerAdapter
    }

    private fun filtrarLigas(nombreLiga: String) {
        val ligaSeleccionada = listaLigas.find { it.strLeague == nombreLiga }
        if (ligaSeleccionada?.idLeague != null) {
            cargarEquiposLiga(ligaSeleccionada.idLeague)
        }
    }
}
