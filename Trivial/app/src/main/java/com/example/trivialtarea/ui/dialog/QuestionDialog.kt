package com.example.trivialtarea.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.trivialtarea.Pregunta
import com.example.trivialtarea.R

class QuestionDialog : DialogFragment() {

    interface OnPreguntaContestadaListener {
        fun onPreguntaContestada(isCorrect: Boolean)
    }



    private var listener: OnPreguntaContestadaListener? = null
    private lateinit var questionText: String
    private lateinit var options: List<String>
    private var correctAnswerIndex: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPreguntaContestadaListener) {
            listener = context
        } else {
            throw RuntimeException("$context debe implementar OnPreguntaContestadaListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionText = it.getString(ARG_QUESTION_TEXT, "")
            options = it.getStringArrayList(ARG_OPTIONS)?.toList() ?: emptyList()
            correctAnswerIndex = it.getInt(ARG_CORRECT_ANSWER_INDEX, -1)
        } ?: throw IllegalStateException("Arguments were not set for QuestionDialog.")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vista = LayoutInflater.from(context).inflate(R.layout.dialog_question, null)

        val textoPreguntas: TextView = vista.findViewById(R.id.textoPreguntas)
        val listaOpciones: ListView = vista.findViewById(R.id.listaOpciones)
        val btnAceptar: Button = vista.findViewById(R.id.btn_accept)

        textoPreguntas.text = questionText
        listaOpciones.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, options)
        listaOpciones.choiceMode = ListView.CHOICE_MODE_SINGLE

        btnAceptar.setOnClickListener {
            val selectedPosition = listaOpciones.checkedItemPosition
            if (selectedPosition != ListView.INVALID_POSITION) {
                val isCorrect = selectedPosition == correctAnswerIndex
                listener?.onPreguntaContestada(isCorrect)
                dismiss()
            } else {
                Toast.makeText(context, "Selecciona una opción", Toast.LENGTH_SHORT).show()
            }
        }

        return AlertDialog.Builder(requireContext()).setView(vista).create()
    }

    companion object {
        private const val ARG_QUESTION_TEXT = "question_text"
        private const val ARG_OPTIONS = "options"
        private const val ARG_CORRECT_ANSWER_INDEX = "correct_answer_index"

        fun newInstance(questionText: String, options: ArrayList<String>, correctAnswerIndex: Int): QuestionDialog {
            val fragment = QuestionDialog()
            val args = Bundle().apply {
                putString(ARG_QUESTION_TEXT, questionText)
                putStringArrayList(ARG_OPTIONS, options)
                putInt(ARG_CORRECT_ANSWER_INDEX, correctAnswerIndex)
            }
            fragment.arguments = args
            return fragment
        }
    }

}
