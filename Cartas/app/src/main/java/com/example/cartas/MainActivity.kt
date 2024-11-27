package com.example.cartas

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cartas.SecondActivity
import com.example.cartas.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        acciones()
    }

    private fun acciones() {
        binding.btnEmpezar.setOnClickListener{
            val nombreJugador = binding.nameEditText.text.toString()

            val intent=Intent(this,SecondActivity::class.java)
            intent.putExtra("PLAYER_NAME", nombreJugador)
            startActivity(intent)
        }

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


}