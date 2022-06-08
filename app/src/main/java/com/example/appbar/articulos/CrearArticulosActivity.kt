package com.example.appbar.articulos

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.appbar.clases_auxiliares.Prefs
import com.example.appbar.databinding.ActivityCrearArticulosBinding
import com.example.appbar.recycler_articulos.DatosArticulo
import com.example.appbar.recycler_familias.DatosFamilia
import com.google.firebase.database.*
import kotlin.math.roundToInt

class CrearArticulosActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityCrearArticulosBinding
    lateinit var adapterTipoFamilia: ArrayAdapter<String>
    lateinit var listaFamiliasArticulo : MutableList<String>
    lateinit var listaImagenes : MutableList<Int>
    lateinit var listaIdFamilia : MutableList<Int>

    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var prefs : Prefs

    var id = 0
    var idFamilia = 0
    var nombreArticulo = ""
    var urlImagenFamilia = 0
    var urlImagenFamiliaParaArticulo = 0
    var contador = 0
    var precio = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearArticulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)
        listaFamiliasArticulo = mutableListOf<String>()
        listaImagenes = mutableListOf<Int>()
        listaIdFamilia = mutableListOf<Int>()

        setListeners()
        cargarFamilias()

//---------------------------------------------------------------------------------------------------

      adapterTipoFamilia = ArrayAdapter(
            this, R.layout.simple_spinner_dropdown_item, listaFamiliasArticulo
          )
    }

//---------------------------------------------------------------------------------------------------

    private fun cargarFamilias() {
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference()
        reference.child("familias").addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        var idDeFamilia = ds.child("id").getValue().toString().toInt()
                        var nombreFamilia = ds.child("nombreFamilia").getValue().toString()
                        urlImagenFamilia = ds.child("urlImagenFamilia").getValue().toString().toInt()
                        var familia = DatosFamilia(idDeFamilia, nombreFamilia, urlImagenFamilia)
                        listaFamiliasArticulo.add(nombreFamilia)
                        listaImagenes.add(urlImagenFamilia)
                        listaIdFamilia.add(idDeFamilia)
                        if (contador == 0) {
                            familia.urlImagenFamilia?.let {
                                binding.imgFamiliaArticulo.setImageResource(
                                    it
                                )
                                binding.imgFamiliaArticulo.isVisible = true
                                contador ++
                            }
                        }
                    }
                    binding.spArticulo.adapter = adapterTipoFamilia
                    binding.spArticulo.onItemSelectedListener = this@CrearArticulosActivity
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

//---------------------------------------------------------------------------------------------------

    private fun setListeners() {
        binding.btnGuardarArticulo.setOnClickListener {
            guardarArticulos()
        }
        binding.btnLimpiarArticulo.setOnClickListener {
            limpiar()
        }
        binding.btnVolverArticulo.setOnClickListener {
            onBackPressed()
        }
    }

//---------------------------------------------------------------------------------------------------

    private fun limpiar() {
        binding.edtNombreArticulo.setText("")
        binding.spArticulo.setSelection(0)
        binding.edtPrecioArticulo.setText("")
    }

//---------------------------------------------------------------------------------------------------

    private fun guardarArticulos() {
        if (comprobarNombreArticulo() && comprobarPrecioArticulo()){
            nombreArticulo = binding.edtNombreArticulo.text.toString().trim()
            precio = binding.edtPrecioArticulo.text.toString().trim().toDouble()
            precio = (precio * 100.0).roundToInt() / 100.0
            guardarArticulo()
            limpiar()
        }
    }

//---------------------------------------------------------------------------------------------------

    private fun comprobarNombreArticulo(): Boolean {
        var nombreArticulo = binding.edtNombreArticulo.text.toString().trim()
        if (!comprobarView(binding.edtNombreArticulo, nombreArticulo)) {
            return false
        }
        return true
    }

//---------------------------------------------------------------------------------------------------

    private fun comprobarPrecioArticulo(): Boolean {
        var precioArticulo = binding.edtPrecioArticulo.text.toString().trim()
        if (precioArticulo.isNullOrEmpty()) {
            binding.edtPrecioArticulo.setError(getString(com.example.appbar.R.string.mensaje_error_precio_articulo))
            binding.edtPrecioArticulo.requestFocus()
            return false
        }
        return true
    }

//---------------------------------------------------------------------------------------------------

    private fun comprobarView (et: EditText, cad: String): Boolean {
        if (cad.isNullOrEmpty()){
            et.setError(getString(com.example.appbar.R.string.mensaje_error_nombre_articulo))
            et.requestFocus()
            return false
        }
        return true
    }

//---------------------------------------------------------------------------------------------------

    private fun guardarArticulo() {
        val articulo = DatosArticulo (cogerContadorId(), idFamilia, nombreArticulo, precio, urlImagenFamiliaParaArticulo)
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("articulos")
        reference.child(id.toString()).setValue(articulo).addOnSuccessListener {
            Toast.makeText(this, getString(com.example.appbar.R.string.articulo_guardado), Toast.LENGTH_SHORT).show()
            prefs.guardarContadorIdArticulo(id)
        }
            .addOnFailureListener {
                Toast.makeText(this, "ERROR:" + it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

//---------------------------------------------------------------------------------------------------

    private fun cogerContadorId(): Int {
        id = prefs.leerContadorIdArticulo()
        id += 1
        return id
    }

//---------------------------------------------------------------------------------------------------

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0) {
            binding.spArticulo -> {
                for (i in listaImagenes.indices) {
                    if (p0.selectedItemPosition == i) {
                        var imagen = listaImagenes.get(i)
                        binding.imgFamiliaArticulo.setImageResource(imagen)
                        idFamilia = listaIdFamilia.get(i)
                        urlImagenFamiliaParaArticulo = listaImagenes.get(i)
                    }
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
