package com.example.trivialtarea

import android.app.DownloadManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.trivialtarea.databinding.ActiviyGameBinding
import com.example.trivialtarea.ui.dialog.QuestionDialog
import com.google.gson.Gson

class GameActivity:AppCompatActivity(), QuestionDialog.OnPreguntaContestadaListener {
    private lateinit var binding: ActiviyGameBinding
    private var aciertos = 0
    private var preguntasTotales = 0
    private var usuarios:Usuarios?=null
    private lateinit var preguntas:List<Pregunta>
    private var preguntaActual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActiviyGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

       usuarios = intent.getSerializableExtra("usuarios") as? Usuarios


        configurarToolbar()
        acciones()



    }

    fun configurarToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Jugador: ${usuarios?.nombre?: ""}"
            setDisplayHomeAsUpEnabled(true)
        }


    }



    private fun acciones() {
        binding.btnIniciar.setOnClickListener {
            val numPreguntas: Int = binding.etNumPreguntas.text.toString().toIntOrNull() ?: 0
            val dificultad = binding.spinnerDificultad.selectedItem?.toString()?.lowercase() ?: ""

            if (numPreguntas <= 0) {
                Toast.makeText(
                    this, "Introduce un número de preguntas válido",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (dificultad.isEmpty()) {
                Toast.makeText(
                    this, "Selecciona una dificultad",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            cargarPreguntas(numPreguntas, dificultad)
        }
    }

    private fun cargarPreguntas(numPreguntas: Int, dificultad: String) {
        binding.tvAciertos.text = "Aciertos: $aciertos"

        val url = "https://opentdb.com/api.php?amount=20&type=multiple"
        val peticion = JsonObjectRequest(url, { response ->
            val resultados = response.getJSONArray("results")
            val totalPreguntasDisponibles = resultados.length()

            if (totalPreguntasDisponibles > 0) {
                val preguntasCompletas = (0 until totalPreguntasDisponibles).map { i ->
                    val preguntaJSON = resultados.getJSONObject(i)
                    val preguntaTexto = preguntaJSON.getString("question")
                    val respuestasIncorrectas = preguntaJSON.getJSONArray("incorrect_answers")
                    val respuestaCorrecta = preguntaJSON.getString("correct_answer")

                    val incorrectAnswers = mutableListOf<String>()
                    for (j in 0 until respuestasIncorrectas.length()) {
                        incorrectAnswers.add(respuestasIncorrectas.getString(j))
                    }

                    Pregunta(
                        question = preguntaTexto,
                        correctAnswer = respuestaCorrecta,
                        incorrectAnswers = incorrectAnswers
                    )
                }

                preguntas = preguntasCompletas.take(numPreguntas)
                preguntasTotales = preguntas.size

                if (preguntasTotales > 0) {
                    preguntaActual = 0
                    mostrarPregunta()
                } else {
                    Toast.makeText(this, "No hay suficientes preguntas disponibles.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No se encontraron preguntas.", Toast.LENGTH_SHORT).show()
            }

        }, { error ->
            Toast.makeText(this, "Error al cargar preguntas: ${error.message}", Toast.LENGTH_SHORT).show()
        })

        Volley.newRequestQueue(this).add(peticion)
    }




    private fun mostrarPregunta() {
        if (preguntaActual < preguntas.size) {
            val pregunta = preguntas[preguntaActual]
            val opciones = ArrayList<String>().apply {
                addAll(pregunta.incorrectAnswers ?: emptyList())
                pregunta.correctAnswer?.let { add(it) }
                shuffle()
            }
            val dialog = QuestionDialog.newInstance(
                questionText = pregunta.question ?: "",
                options = opciones,
                correctAnswerIndex = opciones.indexOf(pregunta.correctAnswer)
            )
            dialog.show(supportFragmentManager, "QuestionDialog")
        } else {
            Toast.makeText(this, "No hay más preguntas. Aciertos: $aciertos/$preguntasTotales", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onPreguntaContestada(isCorrect: Boolean) {
        if (isCorrect) {
            aciertos++
            binding.tvAciertos.text = "Aciertos: $aciertos"
        }
        preguntaActual++
        mostrarPregunta()

    }

}
