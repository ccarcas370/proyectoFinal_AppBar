package com.example.appbar.tpv

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.appbar.clases_auxiliares.Prefs
import com.example.appbar.databinding.ActivityLineaDePedidoBinding
import com.example.appbar.recycler_pedidos.DatosPedido
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LineaDePedidoActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityLineaDePedidoBinding
    lateinit var adapterCantidadArticulos: ArrayAdapter<Int>
    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var prefs : Prefs

    var cantidadArticulo = 0
    var idSalon = 0
    var nombreSalon = ""
    var nombreArticulo = ""
    var precioArticulo = 0.0
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLineaDePedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)
        setListenner()
        cogerDatos()
        asignarValores()

//---------------------------------------------------------------------------------------------------

        val valores = mutableListOf(0,1,2,3,4,5,6,7,8,9,10)
        adapterCantidadArticulos = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, valores)
        binding.spCantidad.adapter = adapterCantidadArticulos
        binding.spCantidad.onItemSelectedListener = this
    }

//---------------------------------------------------------------------------------------------------

    private fun setListenner() {
        binding.btnVolver.setOnClickListener {
            val i = Intent (this, RecyclerSalonesTPVActivity::class.java)
            startActivity(i)
        }

        binding.btnGuardarLineaPedido.setOnClickListener {
            guardarPedido()
            onBackPressed()
        }
    }

//---------------------------------------------------------------------------------------------------

    private fun guardarPedido() {
        val pedido = DatosPedido (cogerContadorId(), nombreSalon, nombreArticulo, precioArticulo, cantidadArticulo)
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("pedidos")
        reference.child(id.toString()).setValue(pedido).addOnSuccessListener {
            Toast.makeText(this, getString(com.example.appbar.R.string.guardar_pedido), Toast.LENGTH_SHORT).show()
            prefs.guardarContadorPedidos(id)
        }
            .addOnFailureListener {
                Toast.makeText(this, "ERROR:" + it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

//---------------------------------------------------------------------------------------------------

    private fun cogerContadorId(): Int? {
        id = prefs.leerContadorPedidos()
        id += 1
        return id
    }

//---------------------------------------------------------------------------------------------------

    private fun asignarValores() {
        binding.nombreSalon.setText(nombreSalon)
        binding.nombreSalon.isEnabled = false

        binding.nombreArticulo.setText(nombreArticulo)
        binding.nombreArticulo.isEnabled = false

        binding.precioArticulo.setText(precioArticulo.toString())
        binding.precioArticulo.isEnabled = false

    }

//---------------------------------------------------------------------------------------------------

    private fun cogerDatos() {
        val bundle = intent.extras
        idSalon = bundle?.getInt("idSalon", 0)!!
        nombreSalon = bundle?.getString("nombreSalon").toString()
        nombreArticulo = bundle?.getString("nombreArticulo").toString()
        precioArticulo = bundle?.getDouble("precioArticulo", 0.0)!!
    }

//---------------------------------------------------------------------------------------------------

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0) {
            binding.spCantidad -> {
                if (p0.selectedItem == 0) {
                    cantidadArticulo = 0
                    binding.btnGuardarLineaPedido.isVisible = false
                } else if (p0.selectedItem == 1) {
                    cantidadArticulo = 1
                    binding.btnGuardarLineaPedido.isVisible = true
                } else if (p0.selectedItem == 2) {
                    cantidadArticulo = 2
                    binding.btnGuardarLineaPedido.isVisible = true
                } else if (p0.selectedItem == 3) {
                    cantidadArticulo = 3
                    binding.btnGuardarLineaPedido.isVisible = true
                } else if (p0.selectedItem == 4) {
                    cantidadArticulo = 4
                    binding.btnGuardarLineaPedido.isVisible = true
                } else if (p0.selectedItem == 5) {
                    cantidadArticulo = 5
                    binding.btnGuardarLineaPedido.isVisible = true
                }else if (p0.selectedItem == 6) {
                    cantidadArticulo = 6
                    binding.btnGuardarLineaPedido.isVisible = true
                }else if (p0.selectedItem == 7) {
                    cantidadArticulo = 7
                    binding.btnGuardarLineaPedido.isVisible = true
                } else if (p0.selectedItem == 8) {
                    cantidadArticulo = 8
                    binding.btnGuardarLineaPedido.isVisible = true
                } else if (p0.selectedItem == 9) {
                    cantidadArticulo = 9
                    binding.btnGuardarLineaPedido.isVisible = true
                } else if (p0.selectedItem == 10) {
                    cantidadArticulo = 10
                    binding.btnGuardarLineaPedido.isVisible = true
                }
            }
        }
    }

//---------------------------------------------------------------------------------------------------

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

//---------------------------------------------------------------------------------------------------
}