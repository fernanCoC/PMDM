package com.example.cartas

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cartas.databinding.ActivitySecondBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class SecondActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySecondBinding
    private var valorCarta = 0
    private var puntuacion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombreJugador = intent.getStringExtra("PLAYER_NAME")

        Snackbar.make(binding.root, "Buenas, $nombreJugador", Snackbar.LENGTH_INDEFINITE)
            .setAction("Comenzar") {
                empezarJuego()
            }.show()
    }

    fun empezarJuego() {
        valorCarta = generarRandomCarta()
        valorDeCarta(valorCarta)
        setImagenesListener()
    }

    fun valorDeCarta(valor: Int) {
        binding.imageView.setImageResource(obtenerCarta(valor))
    }

    fun obtenerCarta(valor: Int): Int {
        val imagenesCartas = arrayOf(
            R.drawable.c1,
            R.drawable.c2,
            R.drawable.c3,
            R.drawable.c4,
            R.drawable.c5,
            R.drawable.c6,
            R.drawable.c7,
            R.drawable.c8,
            R.drawable.c9,
            R.drawable.c10,
            R.drawable.c11,
            R.drawable.c12,
            R.drawable.c13,
        )
        return imagenesCartas[valor - 1]
    }

    fun setImagenesListener() {
        binding.btnMayor.setOnClickListener { adivinarCarta(esMayor = true) }
        binding.btnMenor.setOnClickListener { adivinarCarta(esMayor = false) }
    }

    fun adivinarCarta(esMayor: Boolean) {
        val nuevaCartaValor = generarRandomCarta()

        if (nuevaCartaValor == valorCarta) {
            adivinarCarta(esMayor)
            return
        }

        val esCorrecta = if (esMayor) nuevaCartaValor > valorCarta else nuevaCartaValor < valorCarta

        if (esCorrecta) {
            puntuacion++
            valorCarta = nuevaCartaValor
            valorDeCarta(valorCarta)
            Snackbar.make(binding.root, "Acertaste! Puntuación: $puntuacion", Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(binding.root, "Perdiste. Puntos: $puntuacion", Snackbar.LENGTH_INDEFINITE)
                .setAction("Cerrar") {
                    finish()
                }
                .show()
        }
    }

    fun generarRandomCarta(): Int {
        return Random.nextInt(1, 14)
    }

    override fun onClick(v: View?) {
    }
}
