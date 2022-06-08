package com.example.appbar.tpv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appbar.MainActivity
import com.example.appbar.R
import com.example.appbar.databinding.ActivityRecyclerArticulosTpvactivityBinding
import com.example.appbar.recycler_articulos.DatosArticulo
import com.example.appbar.recycler_articulos_tpv.ArticuloTPVAdapter
import com.google.firebase.database.*

class RecyclerArticulosTPVActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecyclerArticulosTpvactivityBinding

    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var listaArticulos: MutableList<DatosArticulo>
    var idFamilaParaArticulo = 0
    var idSalon  = 0
    var nombreSalon = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_articulos_tpvactivity)
        binding = ActivityRecyclerArticulosTpvactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaArticulos = mutableListOf<DatosArticulo>()
        cogerIdFamilia()
        setRecycler()
        rellenarLista()
        swipeRecycler()
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
                    ItemTouchHelper.RIGHT -> {
                        val position = viewHolder.adapterPosition
                        val elementoLista = listaArticulos[position]

                        val i = Intent(
                            this@RecyclerArticulosTPVActivity,
                            LineaDePedidoActivity::class.java
                        ).apply {
                            putExtra("idSalon", idSalon)
                            putExtra("nombreSalon", nombreSalon)
                            putExtra("nombreArticulo", elementoLista.nombreArticulo)
                            putExtra("precioArticulo", elementoLista.precio)
                        }
                        startActivity(i)
                    }
                    ItemTouchHelper.LEFT -> {
                        val position = viewHolder.adapterPosition
                        val elementoLista = listaArticulos[position]

                        val i = Intent(
                            this@RecyclerArticulosTPVActivity,
                            LineaDePedidoActivity::class.java
                        ).apply {
                            putExtra("idSalon", idSalon)
                            putExtra("nombreSalon", nombreSalon)
                            putExtra("nombreArticulo", elementoLista.nombreArticulo)
                            putExtra("precioArticulo", elementoLista.precio)
                        }
                        startActivity(i)
                    }

                    else ->{

                    }
                }
            }

        }
        val ith = ItemTouchHelper(itemTouch)
        ith.attachToRecyclerView(binding.recyclerViewArticulosTPV)
    }

//---------------------------------------------------------------------------------------------------

    private fun cogerIdFamilia() {
        val bundle = intent.extras
        idFamilaParaArticulo = bundle?.getInt("idFamilia", 0)!!
        idSalon = bundle?.getInt("idSalon", 0)!!
        nombreSalon = bundle?.getString("nombreSalon").toString()
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
                            if (idFamilaParaArticulo == articulo.idFamilia)
                            listaArticulos.add(articulo)
                        }
                    }
                    setRecycler()
                    binding.recyclerViewArticulosTPV.adapter = ArticuloTPVAdapter(listaArticulos)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

//---------------------------------------------------------------------------------------------------

    private fun setRecycler() {
        binding.recyclerViewArticulosTPV.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewArticulosTPV.setHasFixedSize(true)
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
                onBackPressed()
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