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
import com.example.appbar.databinding.ActivityModificarFamiliasBinding
import com.example.appbar.recycler_articulos.DatosArticulo
import com.example.appbar.recycler_familias.DatosFamilia
import com.google.firebase.database.*

class ModificarFamiliasActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityModificarFamiliasBinding

    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var adapterTipoFamilia: ArrayAdapter<String>
    lateinit var listaIdFamiliaEnArticulo: MutableList<Int>
    lateinit var listaIdDeArticulo: MutableList<Int>
    lateinit var listaPrecioDeArticulo: MutableList<Double>
    lateinit var listaNombreDeArticulo: MutableList<String>


    var idFamilia = 0
    var nombreFamilia = ""
    var urlImagenFamilia = 0

    var idDeFamiliaParaArticulo = 0
    var idDeArticulo = 0
    var nombreDeArticulo = ""
    var precioDeArticulo = 0.0
    var urlImagenFamiliaParaArticulo = 0
    var idFamiliaparaArticulo = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarFamiliasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaIdFamiliaEnArticulo = mutableListOf<Int>()
        listaIdDeArticulo = mutableListOf<Int>()
        listaPrecioDeArticulo = mutableListOf<Double>()
        listaNombreDeArticulo = mutableListOf<String>()
        cogerDatos()
        setListeners()

//---------------------------------------------------------------------------------------------------

        adapterTipoFamilia = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(
                R.array.ContenidoFamilias
            )
        )
        binding.spFamiliaModificar.adapter = adapterTipoFamilia
        binding.spFamiliaModificar.onItemSelectedListener = this

        if (urlImagenFamilia == R.drawable.sinimagen) {
            binding.spFamiliaModificar.setSelection(0)
        }
        else if (urlImagenFamilia == R.drawable.cafe ) {
            binding.spFamiliaModificar.setSelection(1)
        }
        else if (urlImagenFamilia == R.drawable.refresco ) {
            binding.spFamiliaModificar.setSelection(2)
        }
        else if (urlImagenFamilia == R.drawable.bocadillo ) {
            binding.spFamiliaModificar.setSelection(3)
        }
        else if (urlImagenFamilia == R.drawable.postre ) {
            binding.spFamiliaModificar.setSelection(4)
        }

    }

//---------------------------------------------------------------------------------------------------

    private fun cogerDatos() {
        val bundle = intent.extras
        idFamilia = bundle?.getInt("idFamilia",0)!!
        nombreFamilia = bundle?.getString("nombreFamilia").toString()
        urlImagenFamilia = bundle?.getInt("imagen", 0)!!

        binding.edtNombreModificarFamilia.setText(nombreFamilia)

    }

//---------------------------------------------------------------------------------------------------

    private fun setListeners() {
        binding.btnModificarFamilia.setOnClickListener {
            modificarFamilia()
        }
        binding.btnVolverModificarFamilia.setOnClickListener {
            onBackPressed()
        }
    }

//---------------------------------------------------------------------------------------------------

    private fun modificarFamilia() {
        var sw = true
        if (comprobarNombreFamilia()) {
            nombreFamilia = binding.edtNombreModificarFamilia.text.toString().trim()

            val familia = DatosFamilia(idFamilia, nombreFamilia, urlImagenFamilia)
            db =
                FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
            reference = db.getReference("familias")
            reference.child(idFamilia.toString()).setValue(familia).addOnSuccessListener {
                Toast.makeText(this, getString(R.string.familia_modificada), Toast.LENGTH_SHORT)
                    .show()

                db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
                reference = db.getReference()
                reference.child("articulos").addValueEventListener(object :ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists() && sw == true) {
                            for (ds in snapshot.children) {
                                idDeFamiliaParaArticulo = ds.child("idFamilia").getValue().toString().toInt()
                                listaIdFamiliaEnArticulo.add(idDeFamiliaParaArticulo)
                                idDeArticulo = ds.child("id").getValue().toString().toInt()
                                listaIdDeArticulo.add(idDeArticulo)
                                nombreDeArticulo = ds.child("nombreArticulo").getValue().toString()
                                listaNombreDeArticulo.add(nombreDeArticulo)
                                precioDeArticulo = ds.child("precio").getValue().toString().toDouble()
                                listaPrecioDeArticulo.add(precioDeArticulo)
                            }
                                for (j in listaIdFamiliaEnArticulo.indices) {
                                    if (listaIdFamiliaEnArticulo[j] == idFamilia && sw == true) {
                                        idDeArticulo = listaIdDeArticulo[j]
                                        nombreDeArticulo = listaNombreDeArticulo[j]
                                        precioDeArticulo = listaPrecioDeArticulo[j]
                                        urlImagenFamiliaParaArticulo = urlImagenFamilia
                                        idFamiliaparaArticulo = listaIdFamiliaEnArticulo[j]
                                        modificarArticulo()
                                    }
                                }
                            sw = false
                            listaIdFamiliaEnArticulo.clear()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
                .addOnFailureListener {
                    Toast.makeText(this, "ERROR:" + it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

//---------------------------------------------------------------------------------------------------

    private fun comprobarNombreFamilia(): Boolean {
        var nombreFamilia = binding.edtNombreModificarFamilia.text.toString().trim()
        if (!comprobarView(binding.edtNombreModificarFamilia, nombreFamilia)) {
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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0) {
            binding.spFamiliaModificar -> {

                if (p0.selectedItem == "Elige una imagen") {
                    binding.imgNoImagenModificar.isVisible = true
                    urlImagenFamilia = R.drawable.sinimagen
                    binding.imgBocadilloModificar.isVisible = false
                    binding.imgCafeModificar.isVisible = false
                    binding.imgPostreModificar.isVisible = false
                    binding.imgRefrescoModificar.isVisible = false

                } else if (p0.selectedItem == "Cafés") {
                    binding.imgNoImagenModificar.isVisible = false
                    binding.imgBocadilloModificar.isVisible = false
                    binding.imgCafeModificar.isVisible = true
                    urlImagenFamilia = R.drawable.cafe
                    binding.imgPostreModificar.isVisible = false
                    binding.imgRefrescoModificar.isVisible = false

                } else if (p0.selectedItem == "Refrescos") {
                    binding.imgNoImagenModificar.isVisible = false
                    binding.imgBocadilloModificar.isVisible = false
                    binding.imgCafeModificar.isVisible = false
                    binding.imgPostreModificar.isVisible = false
                    binding.imgRefrescoModificar.isVisible = true
                    urlImagenFamilia = R.drawable.refresco

                } else if (p0.selectedItem == "Bocadillos") {
                    binding.imgNoImagenModificar.isVisible = false
                    binding.imgBocadilloModificar.isVisible = true
                    urlImagenFamilia = R.drawable.bocadillo
                    binding.imgCafeModificar.isVisible = false
                    binding.imgPostreModificar.isVisible = false
                    binding.imgRefrescoModificar.isVisible = false

                } else if (p0.selectedItem == "Postres") {
                    binding.imgNoImagenModificar.isVisible = false
                    binding.imgBocadilloModificar.isVisible = false
                    binding.imgCafeModificar.isVisible = false
                    binding.imgPostreModificar.isVisible = true
                    urlImagenFamilia = R.drawable.postre
                    binding.imgRefrescoModificar.isVisible = false
                }

            }
        }
    }

//---------------------------------------------------------------------------------------------------

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

//---------------------------------------------------------------------------------------------------

    private fun modificarArticulo() {
            val articulo = DatosArticulo( idDeArticulo, idFamilia, nombreDeArticulo, precioDeArticulo, urlImagenFamiliaParaArticulo)
            db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
            reference = db.getReference("articulos")
            reference.child(idDeArticulo.toString()).setValue(articulo).addOnSuccessListener {
                return@addOnSuccessListener
            }
                .addOnFailureListener {
                    Toast.makeText(this, "ERROR:" + it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
    }

//---------------------------------------------------------------------------------------------------
}