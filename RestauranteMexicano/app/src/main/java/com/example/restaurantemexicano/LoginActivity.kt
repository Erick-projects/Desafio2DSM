package com.example.restaurantemexicano

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val botonIniciarSesion = findViewById<Button>(R.id.login_button)
        val botonRegistrarse = findViewById<Button>(R.id.register_button)

        botonIniciarSesion.setOnClickListener {
            val correoTexto = email.text.toString().trim()
            val contrasenaTexto = password.text.toString().trim()

            if (correoTexto.isEmpty() || contrasenaTexto.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese el correo y la contraseña.", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(correoTexto, contrasenaTexto)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MenuActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Fallo la autenticación. Verifique sus credenciales.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        botonRegistrarse.setOnClickListener {
            val correoTexto = email.text.toString().trim()
            val contrasenaTexto = password.text.toString().trim()

            if (correoTexto.isEmpty() || contrasenaTexto.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese un correo y una contraseña para registrarse.", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(correoTexto, contrasenaTexto)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MenuActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Fallo el registro. Intente nuevamente.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
