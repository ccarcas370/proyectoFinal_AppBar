package com.example.appbar.articulos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appbar.MainActivity
import com.example.appbar.R
import com.example.appbar.clases_auxiliares.Prefs
import com.example.appbar.databinding.ActivityRecyclerArticulosBinding
import com.example.appbar.recycler_articulos.ArticuloAdapter
import com.example.appbar.recycler_articulos.DatosArticulo
import com.google.firebase.database.*

class RecyclerArticulosActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecyclerArticulosBinding

    //Para la conexión con Realtimedatabsae
    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var listaArticulos: MutableList<DatosArticulo>
    lateinit var listaArticulosIdFamilia: MutableList<Int>

    var contador = 0
    var elemento = 0
    var idFamilia = 0
    lateinit var prefs : Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerArticulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaArticulos = mutableListOf<DatosArticulo>()
        listaArticulosIdFamilia = mutableListOf<Int>()
        prefs = Prefs(this)
        setRecycler()
        swipeRecycler()
        rellenarLista()
    }

//---------------------------------------------------------------------------------------------------

    private fun guardarElementos() {
        prefs.guardarContadorIdArticulo(elemento)
    }

//---------------------------------------------------------------------------------------------------

    private fun rellenarLista() {

        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("articulos")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaArticulos.clear()
                if (snapshot.exists()){
                    for (item in snapshot.children){
                        val articulo = item.getValue(DatosArticulo::class.java)
                        if (articulo != null) {
                            listaArticulos.add(articulo)
                            elemento = articulo?.id!!
                            idFamilia = articulo.idFamilia!!
                            listaArticulosIdFamilia.add(idFamilia)
                        }
                        guardarElementos()
                    }
                    binding.recyclerViewArticulos.adapter = ArticuloAdapter(listaArticulos)
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

//---------------------------------------------------------------------------------------------------

    private fun swipeRecycler() {
        val itemTouch = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or (ItemTouchHelper.RIGHT)){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction){
                    ItemTouchHelper.LEFT -> {
                        val alertDialog = AlertDialog.Builder(this@RecyclerArticulosActivity)
                            .setTitle(R.string.titulo_borrar_articulo)
                            .setMessage(R.string.mensaje_borrar_articulo)
                            .setNegativeButton(R.string.no_borrar_salon){v,_->v.dismiss()
                                setRecycler()
                            }
                            .setPositiveButton(R.string.si_borrar_salon) { _, _ ->
                                //Borramos el elemento, hemos realizado el método removeAt en el Adapter
                                val position = viewHolder.adapterPosition
                                val adapter = binding.recyclerViewArticulos.adapter as ArticuloAdapter
                                adapter.removeAt(position)
                                guardarElementos()
                                setRecycler()

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                                if ((listaArticulos.isNullOrEmpty() || listaArticulos.size == 1) && contador == 0) {
                                    while (contador == 0) {
                                        startActivity(intent)
                                        contador += 1
                                    }
                                }
                                //Esto se ve chapucero pero resuelve el error

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            }
                            .setCancelable(false)
                            .create()
                            .show()
                    }

                    ItemTouchHelper.RIGHT->{
                        val position = viewHolder.adapterPosition
                        val elementoLista = listaArticulos[position]

                        val i = Intent(this@RecyclerArticulosActivity, ModificarArticulosActivity::class.java).apply {
                            putExtra("idArticulo",elementoLista.id)
                            putExtra("nombreArticulo",elementoLista.nombreArticulo)
                            putExtra("precioArticulo", elementoLista.precio)
                            putExtra("idFamilia", elementoLista.idFamilia)
                            putExtra("urlFamiliaParaArticulo", elementoLista.urlImagenFamilia)
                        }
                        startActivity(i)
                    }

                    else ->{

                    }
                }
            }

        }
        val ith = ItemTouchHelper(itemTouch)
        ith.attachToRecyclerView(binding.recyclerViewArticulos)
    }

//---------------------------------------------------------------------------------------------------

    private fun setRecycler() {
        binding.recyclerViewArticulos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewArticulos.setHasFixedSize(true)
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
                val i = Intent (this, MenuInicialArticulosActivity::class.java)
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

    override fun onRestart() {
        super.onRestart()
        rellenarLista()
    }

//---------------------------------------------------------------------------------------------------
}