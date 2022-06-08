package com.example.appbar.familias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appbar.MainActivity
import com.example.appbar.R
import com.example.appbar.databinding.ActivityMenuInicialFamiliasBinding

class MenuInicialFamiliasActivity : AppCompatActivity() {
    lateinit var binding: ActivityMenuInicialFamiliasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuInicialFamiliasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setTitle(getString(R.string.titulo_menu_inicial_familias))
    }

//---------------------------------------------------------------------------------------------------

    private fun setListeners() {
        binding.btnCrearFamilias.setOnClickListener {
            val i = Intent(this, CrearFamiliasActivity::class.java)
            startActivity(i)
        }

        binding.btnAccederFamilias.setOnClickListener {
            listaFamilias()
        }

        binding.btnFamiliasSalir.setOnClickListener {
            irMenuInicial()
        }
    }

//---------------------------------------------------------------------------------------------------

    private fun irMenuInicial() {
        val i = Intent (this, MainActivity::class.java)
        startActivity(i)
    }

//---------------------------------------------------------------------------------------------------

    private fun listaFamilias() {
        val i = Intent (this, RecyclerFamiliasActivity::class.java)
        startActivity(i)
    }

//---------------------------------------------------------------------------------------------------

}