package com.example.trivialtarea

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialtarea.databinding.RegisterActivityBinding

class ActivityRegister : AppCompatActivity() {
    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val apellido = binding.etApellido.text.toString().trim()
            val correo = binding.etCorreo.text.toString().trim()
            val pass = binding.etPass.text.toString()

            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!DataSet.validarRegistro(correo)) {
                DataSet.registrarUsuario(nombre, apellido, correo, pass)
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ActivityLogin::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
