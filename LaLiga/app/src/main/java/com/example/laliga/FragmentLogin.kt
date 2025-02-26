package com.example.laliga

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.laliga.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FragmentLogin:Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.btnAceptar.setOnClickListener{
            auth.signInWithEmailAndPassword(
                binding.etCorreo.text.toString(),
                binding.etPass.text.toString()
            ).addOnCompleteListener{
                if(it.isSuccessful){
                    findNavController().navigate(R.id.action_fragmentLogin_to_fragmentPerfil)
                }else{

                }

            }


        }
        binding.textoRegistro.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentLogin_to_fragmentRegistro)
        }

    }
}