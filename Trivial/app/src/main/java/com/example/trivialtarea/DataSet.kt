package com.example.trivialtarea

class DataSet {
    companion object {
        val usuarios = mutableListOf(
            Usuarios("fernan", "1234","Pepito"),
            Usuarios("fernan2@gmail.com", "1234","Juanito",),
            Usuarios("fernan3@gmail.com", "1234","Juanito",))
        fun validarDatos(correo: String, pass: String): Boolean {
            return usuarios.any { it.correo == correo && it.pass == pass }
        }
        fun registrarUsuario(nombre: String, apellido: String, correo: String, pass: String) {
            usuarios.add(Usuarios(correo, pass, nombre))
        }

        fun validarRegistro(correo: String): Boolean {
            return usuarios.any { it.correo == correo }
        }



    }
}