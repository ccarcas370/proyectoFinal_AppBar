package com.example.appbar.salon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appbar.MainActivity
import com.example.appbar.R
import com.example.appbar.databinding.ActivityMenuInicialBinding


class MenuInicialActivity : AppCompatActivity() {
    lateinit var binding: com.example.appbar.databinding.ActivityMenuInicialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setTitle(getString(R.string.titulo_menu_inicial))
    }

    private fun setListeners() {
        binding.btnCrear.setOnClickListener {
            val i = Intent(this, CrearSalonActivity::class.java)
            startActivity(i)
        }

        binding.btnAcceder.setOnClickListener {
            listaSalones()
        }

        binding.btnGestSalonSalir.setOnClickListener {
            irMenuInicial()
        }
    }

    private fun irMenuInicial() {
        val i = Intent (this, MainActivity::class.java)
        startActivity(i)
    }

    private fun listaSalones() {
        val i = Intent (this, RecyclerSalonActivity::class.java)
        startActivity(i)
    }

}