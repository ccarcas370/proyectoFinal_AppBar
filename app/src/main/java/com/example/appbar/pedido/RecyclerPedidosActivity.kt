package com.example.appbar.pedido

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
import com.example.appbar.clases_auxiliares.Prefs
import com.example.appbar.databinding.ActivityRecyclerPedidosBinding
import com.example.appbar.recycler_pedidos.DatosPedido
import com.example.appbar.recycler_salon.DatosSalon
import com.example.appbar.recycler_salon.SalonAdapter
import com.example.appbar.tpv.LineaDePedidoActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class RecyclerPedidosActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecyclerPedidosBinding
    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var listaSalones: MutableList<DatosSalon>
    lateinit var listaSalonesNombre: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaSalones = mutableListOf()
        listaSalonesNombre = mutableListOf()
        setRecycler()
        swipeRecycler()
        rellenarLista()
        setTitle(getString(R.string.titulo_pedidos))
    }

//---------------------------------------------------------------------------------------------------

    private fun rellenarLista() {
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("salones")
        reference.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item2 in snapshot.children) {
                        val salon = item2.getValue(DatosSalon::class.java)
                            if (salon != null) {
                                listaSalones.add(salon)
                            }
                    }
                    binding.recyclerViewPedidos.adapter = SalonAdapter(listaSalones)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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
                        val elementoLista = listaSalones[position]

                        val i = Intent (this@RecyclerPedidosActivity, RecyclerLineaPedidoActivity::class.java).apply {
                            putExtra("nombreSalon", elementoLista.nombreSalon)
                        }
                        startActivity(i)
                    }
                    ItemTouchHelper.LEFT -> {
                        val position = viewHolder.adapterPosition
                        val elementoLista = listaSalones[position]

                        val i = Intent (this@RecyclerPedidosActivity, RecyclerLineaPedidoActivity::class.java).apply {
                            putExtra("nombreSalon", elementoLista.nombreSalon)
                        }
                        startActivity(i)
                    }

                    else ->{

                    }
                }
            }

        }
        val ith = ItemTouchHelper(itemTouch)
        ith.attachToRecyclerView(binding.recyclerViewPedidos)
    }

//---------------------------------------------------------------------------------------------------

    private fun setRecycler() {
        binding.recyclerViewPedidos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPedidos.setHasFixedSize(true)
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
                val i = Intent (this, MainActivity::class.java)
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