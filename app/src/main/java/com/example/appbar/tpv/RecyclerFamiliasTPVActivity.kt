package com.example.appbar.tpv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appbar.MainActivity
import com.example.appbar.R
import com.example.appbar.databinding.ActivityRecyclerFamiliasTpvactivityBinding
import com.example.appbar.recycler_articulos.DatosArticulo
import com.example.appbar.recycler_familias.DatosFamilia
import com.example.appbar.recycler_familias.FamiliaAdapter
import com.google.firebase.database.*

class RecyclerFamiliasTPVActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecyclerFamiliasTpvactivityBinding

    //Para la conexión con Realtimedatabsae
    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var listaFamilias: MutableList<DatosFamilia>
    var elemento = 0
    var idSalon = 0
    var nombreSalon  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerFamiliasTpvactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaFamilias = mutableListOf<DatosFamilia>()
        cogerDatos()
        setRecycler()
        swipeRecycler()
        rellenarLista()
    }

//---------------------------------------------------------------------------------------------------

    private fun cogerDatos() {
        val bundle = intent.extras
        idSalon  = bundle?.getInt("idSalon", 0)!!
        nombreSalon = bundle?.getString("nombreSalon").toString()
    }

//---------------------------------------------------------------------------------------------------

    private fun rellenarLista() {
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("familias")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaFamilias.clear()
                if (snapshot.exists()){
                    //Recorremos los datos
                    for (item in snapshot.children){
                        val familia = item.getValue(DatosFamilia::class.java)
                        if (familia != null) {
                            listaFamilias.add(familia)
                            elemento = familia?.id!!
                        }
                    }

                    binding.recyclerViewFamiliasTPV.adapter = FamiliaAdapter(listaFamilias)
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
                    ItemTouchHelper.RIGHT -> {
                        val position = viewHolder.adapterPosition
                        val elementoLista = listaFamilias[position]

                        val i = Intent(
                            this@RecyclerFamiliasTPVActivity,
                            RecyclerArticulosTPVActivity::class.java
                        ).apply {
                            putExtra("idFamilia", elementoLista.id)
                            putExtra("idSalon", idSalon)
                            putExtra("nombreSalon", nombreSalon)
                        }
                        startActivity(i)
                    }
                    ItemTouchHelper.LEFT -> {
                        val position = viewHolder.adapterPosition
                        val elementoLista = listaFamilias[position]

                        val i = Intent(
                            this@RecyclerFamiliasTPVActivity,
                            RecyclerArticulosTPVActivity::class.java
                        ).apply {
                            putExtra("idFamilia", elementoLista.id)
                            putExtra("idSalon", idSalon)
                            putExtra("nombreSalon", nombreSalon)
                        }
                        startActivity(i)
                    }

                    else ->{

                    }
                }
            }

        }
        val ith = ItemTouchHelper(itemTouch)
        ith.attachToRecyclerView(binding.recyclerViewFamiliasTPV)
    }

//---------------------------------------------------------------------------------------------------

    private fun setRecycler() {
        binding.recyclerViewFamiliasTPV.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFamiliasTPV.setHasFixedSize(true)
    }

//---------------------------------------------------------------------------------------------------

    override fun onRestart() {
        super.onRestart()
        rellenarLista()
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
                val i = Intent (this, RecyclerSalonesTPVActivity::class.java)
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