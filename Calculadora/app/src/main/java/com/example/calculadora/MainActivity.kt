package com.example.calculadora

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private var value1: Double? = null
    private var value2: Double? = null
    private var operation: String? = null
    private var isValor: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.tvResult)


        val botones = listOf(
            findViewById<Button>(R.id.btn0), findViewById<Button>(R.id.btn1), findViewById<Button>(R.id.btn2),
            findViewById<Button>(R.id.btn3), findViewById<Button>(R.id.btn4), findViewById<Button>(R.id.btn5),
            findViewById<Button>(R.id.btn6), findViewById<Button>(R.id.btn7), findViewById<Button>(R.id.btn8),
            findViewById<Button>(R.id.btn9)
        )


        botones.forEach { button ->
            button.setOnClickListener {
                agregarResultado(button.text.toString())
            }
        }


        findViewById<Button>(R.id.btnSumar).setOnClickListener { prepareOperation("+") }
        findViewById<Button>(R.id.btnRestar).setOnClickListener { prepareOperation("-") }
        findViewById<Button>(R.id.btnMultiplicar).setOnClickListener { prepareOperation("*") }
        findViewById<Button>(R.id.btnDividir).setOnClickListener { prepareOperation("/") }


        findViewById<Button>(R.id.btnExpo)?.setOnClickListener { prepareOperation("^") }
        findViewById<Button>(R.id.btnRaiz)?.setOnClickListener {
            val value = resultTextView.text.toString().toDoubleOrNull()
            if (value != null) {
                resultTextView.text = sqrt(value).toString()
            }
        }


        findViewById<Button>(R.id.btnIgual).setOnClickListener { resultadoFinal() }


        findViewById<Button>(R.id.btnC).setOnClickListener { limpiar() }
    }

    private fun agregarResultado(value: String) {
        if (isValor) {
            resultTextView.text = value
            isValor = false
        } else {
            resultTextView.append(value)
        }
    }

    private fun prepareOperation(op: String) {
        value1 = resultTextView.text.toString().toDoubleOrNull()
        operation = op
        isValor = true
    }

    private fun resultadoFinal() {
        value2 = resultTextView.text.toString().toDoubleOrNull()
        if (value1 != null && value2 != null) {
            val result = when (operation) {
                "+" -> value1!! + value2!!
                "-" -> value1!! - value2!!
                "*" -> value1!! * value2!!
                "/" -> value1!! / value2!!
                "^" -> value1!!.pow(value2!!)
                else -> 0.0
            }
            resultTextView.text = result.toString()
        }
    }

    private fun limpiar() {
        resultTextView.text = "0"
        value1 = null
        value2 = null
        operation = null
    }
}
