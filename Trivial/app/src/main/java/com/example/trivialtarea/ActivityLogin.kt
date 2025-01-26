package com.example.trivialtarea

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trivialtarea.DataSet.Companion.usuarios
import com.example.trivialtarea.databinding.LoginActivityBinding

class ActivityLogin:AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuario = Usuarios("fer","1234","Fernando")
        binding.btnValidar.setOnClickListener {
            val correo = binding.correo.text.toString()
            val pass = binding.pass.text.toString()

            if(DataSet.validarDatos(correo,pass)){
                //pantalla momentanea
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("usuarios", usuario)
                startActivity(intent)

            }else{
                Toast.makeText(this, "Credenciales incorrectas",
                    Toast.LENGTH_SHORT)
                    .show()
            }


        }
            acciones();


    }
    private fun acciones() {
        binding.btnRegistrar.setOnClickListener {
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_record ->{

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}