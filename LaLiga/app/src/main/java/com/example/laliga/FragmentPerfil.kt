package com.example.laliga

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.laliga.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FragmentPerfil: Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var binding: FragmentPerfilBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase
            .getInstance("https://laliga-ce334-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentPerfilBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //quiero que me lleguen los datos del login

        /*val currentUser = auth.currentUser

        if(currentUser != null){
            binding.textoNombre.text = currentUser.displayName?: "No hay usuario"
            binding.textoTelefono.text = currentUser.uid?: "Sin numero de telefono"

        }else{
            binding.textoNombre.text = "Usuario no reconocido"*/
        firebaseDatabase.getReference("users").child(auth.currentUser!!.uid).get().addOnSuccessListener {
            binding.textoNombre.text = it.child("nombre").value.toString()
            binding.textoTelefono.text = it.child("telefono").value.toString()

        }
        binding.btnVerLigas.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentPerfil_to_fragmentLigas)
        }


    }





}