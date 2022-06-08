package com.example.appbar.email

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.appbar.MainActivity
import com.example.appbar.R
import com.example.appbar.databinding.ActivityEmailPrincipalBinding
import com.example.appbar.pedido.RecyclerPedidosActivity

class EmailPrincipalActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmailPrincipalBinding
    lateinit var sm: SendEmail
    lateinit var contexto: Context
    val mensaje = R.string.cadena_mensaje
    val asunto = R.string.cadena_asunto
    var email = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sm = SendEmail(this, email, asunto.toString(), mensaje.toString())
        ponerTexto()
        setListener()
    }

//---------------------------------------------------------------------------------------------------

    private fun ponerTexto() {
        binding.txtAsunto.setText(asunto)
        binding.txtMensaje.setText(mensaje)
    }

//---------------------------------------------------------------------------------------------------

    private fun setListener() {
        binding.btnEnviarEmail.setOnClickListener{
            enviarEmail()
        }
    }

//---------------------------------------------------------------------------------------------------

    private fun enviarEmail() {
        email = binding.txtEmail.getText().toString().trim()
        sendEmail(this, email, asunto.toString(), mensaje.toString())
    }

//---------------------------------------------------------------------------------------------------

    private fun sendEmail(contexto: Context, email: String, subject: String, message: String) {

        sm.execute()
    }

//---------------------------------------------------------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_recycler_salones, menu)
        return true
    }

//---------------------------------------------------------------------------------------------------

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.volver -> {
                val i = Intent (this, RecyclerPedidosActivity::class.java)
                startActivity(i)
                true
            }

            R.id.ir_menu_inicial -> {
                val i = Intent (this, MainActivity::class.java)
                startActivity(i)
                true
            }

            else -> {
                true
            }
        }
    }

//---------------------------------------------------------------------------------------------------

}