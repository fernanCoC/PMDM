package com.example.laliga

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.laliga.databinding.FragmentLoginBinding
import com.example.laliga.databinding.FragmentRegistroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FragmentRegistro:Fragment() {
    private lateinit var binding:FragmentRegistroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance("https://laliga-ce334-default-rtdb.europe-west1.firebasedatabase.app/")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistroBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.btnAceptar.setOnClickListener(){
            auth.createUserWithEmailAndPassword(
                binding.etCorreo.text.toString(),
                binding.etPass.text.toString()

            ).addOnCompleteListener{
                if(it.isSuccessful){
                    val currentUser = auth.currentUser
                    val reference = firebaseDatabase.getReference("users") //user es el nombre del nodo en la base de datos
                        .child(currentUser!!.uid)

                    val user = Usuario(
                        binding.etCorreo.text.toString(),
                        binding.etPass.text.toString(),
                        binding.etNombre.text.toString(),
                        binding.etTelefono.text.toString().toInt()
                    )
                    reference.setValue(user)
                    findNavController().navigate(R.id.action_fragmentRegistro_to_fragmentLogin)

                }else{
                    Snackbar.make(binding.root,"No se ha podido crear el usuario",Snackbar.LENGTH_LONG).show()
                }

            }
        }
    }

}