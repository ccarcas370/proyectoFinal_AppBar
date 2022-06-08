package com.example.appbar.familias


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.appbar.R
import com.example.appbar.clases_auxiliares.Prefs
import com.example.appbar.databinding.ActivityCrearFamiliasBinding
import com.example.appbar.recycler_familias.DatosFamilia
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CrearFamiliasActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityCrearFamiliasBinding
    lateinit var adapterTipoFamilia: ArrayAdapter<String>

    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var prefs : Prefs

    var id = 0
    var nombreFamilia = ""
    var urlImagenFamilia = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearFamiliasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)
        setListeners()

//---------------------------------------------------------------------------------------------------

        adapterTipoFamilia = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(
                R.array.ContenidoFamilias
            )
        )
        binding.spFamilia.adapter = adapterTipoFamilia
        binding.spFamilia.onItemSelectedListener = this
    }

//---------------------------------------------------------------------------------------------------

    private fun setListeners() {
        binding.btnGuardarFamilias.setOnClickListener {
            guardarFamilias()
        }
        binding.btnLimpiarFamilias.setOnClickListener {
            limpiar()
        }
        binding.btnVolverFamilias.setOnClickListener {
            onBackPressed()
        }
    }

//---------------------------------------------------------------------------------------------------

    private fun limpiar() {
        binding.edtNombreFamilia.setText("")
        binding.spFamilia.setSelection(0)
    }

//---------------------------------------------------------------------------------------------------

    private fun guardarFamilias() {
        if (comprobarNombreFamilia()){
            nombreFamilia = binding.edtNombreFamilia.text.toString().trim()
            guardarFamilia()
            limpiar()
        }
    }

//---------------------------------------------------------------------------------------------------

    private fun comprobarNombreFamilia(): Boolean {
        var nombreFamilia = binding.edtNombreFamilia.text.toString().trim()
        if (!comprobarView(binding.edtNombreFamilia, nombreFamilia)) {
            return false
        }
        return true
    }

//---------------------------------------------------------------------------------------------------

    private fun comprobarView (et: EditText, cad: String): Boolean {
        if (cad.isNullOrEmpty()){
            et.setError(getString(com.example.appbar.R.string.mensaje_error_nombre_familia))
            et.requestFocus()
            return false
        }
        return true
    }

//---------------------------------------------------------------------------------------------------


    private fun guardarFamilia() {
        val familia = DatosFamilia (cogerContadorId(), nombreFamilia, urlImagenFamilia)
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("familias")
        reference.child(id.toString()).setValue(familia).addOnSuccessListener {
            Toast.makeText(this, getString(R.string.familia_guardada), Toast.LENGTH_SHORT).show()
            prefs.guardarContadorIdFamilia(id)
        }
            .addOnFailureListener {
                Toast.makeText(this, "ERROR:" + it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

//---------------------------------------------------------------------------------------------------

    private fun cogerContadorId(): Int {
        id = prefs.leerContadorIdFamilia()
        id += 1
        return id
    }

//---------------------------------------------------------------------------------------------------

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0) {
            binding.spFamilia -> {

                if (p0.selectedItem == "Elige una imagen") {
                    binding.imgNoImagen.isVisible = true
                    urlImagenFamilia = R.drawable.sinimagen
                    binding.imgBocadillo.isVisible = false
                    binding.imgCafe.isVisible = false
                    binding.imgPostre.isVisible = false
                    binding.imgRefresco.isVisible = false

                } else if (p0.selectedItem == "Cafés") {
                    binding.imgNoImagen.isVisible = false
                    binding.imgBocadillo.isVisible = false
                    binding.imgCafe.isVisible = true
                    urlImagenFamilia = R.drawable.cafe
                    binding.imgPostre.isVisible = false
                    binding.imgRefresco.isVisible = false

                } else if (p0.selectedItem == "Refrescos") {
                    binding.imgNoImagen.isVisible = false
                    binding.imgBocadillo.isVisible = false
                    binding.imgCafe.isVisible = false
                    binding.imgPostre.isVisible = false
                    binding.imgRefresco.isVisible = true
                    urlImagenFamilia = R.drawable.refresco


                } else if (p0.selectedItem == "Bocadillos") {
                    binding.imgNoImagen.isVisible = false
                    binding.imgBocadillo.isVisible = true
                    urlImagenFamilia = R.drawable.bocadillo
                    binding.imgCafe.isVisible = false
                    binding.imgPostre.isVisible = false
                    binding.imgRefresco.isVisible = false

                } else if (p0.selectedItem == "Postres") {
                    binding.imgNoImagen.isVisible = false
                    binding.imgBocadillo.isVisible = false
                    binding.imgCafe.isVisible = false
                    binding.imgPostre.isVisible = true
                    urlImagenFamilia = R.drawable.postre
                    binding.imgRefresco.isVisible = false
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