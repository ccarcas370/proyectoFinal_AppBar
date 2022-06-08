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
import com.example.appbar.databinding.ActivityRecyclerSalonBinding
import com.example.appbar.recycler_salon.DatosSalon
import com.example.appbar.recycler_salon.SalonAdapter
import com.google.firebase.database.*

class RecyclerSalonesTPVActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecyclerSalonBinding
    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var listaSalones: MutableList<DatosSalon>
    var elemento = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerSalonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaSalones = mutableListOf()
        setRecycler()
        swipeRecycler()
        rellenarLista()
    }

//---------------------------------------------------------------------------------------------------

    private fun setRecycler() {
        binding.recyclerViewSalonesTPV.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSalonesTPV.setHasFixedSize(true)
    }

//---------------------------------------------------------------------------------------------------

    private fun swipeRecycler() {
        val itemTouch = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or (ItemTouchHelper.RIGHT)){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction){
                    ItemTouchHelper.RIGHT -> {
                        val position = viewHolder.adapterPosition
                        val elementoLista = listaSalones[position]

                        val i = Intent(
                            this@RecyclerSalonesTPVActivity,
                            RecyclerFamiliasTPVActivity::class.java
                        ).apply {
                            putExtra("idSalon", elementoLista.id)
                            putExtra("nombreSalon", elementoLista.nombreSalon)
                        }
                        startActivity(i)
                    }
                    ItemTouchHelper.LEFT -> {
                        val position = viewHolder.adapterPosition
                        val elementoLista = listaSalones[position]

                        val i = Intent(
                            this@RecyclerSalonesTPVActivity,
                            RecyclerFamiliasTPVActivity::class.java
                        ).apply {
                            putExtra("idSalon", elementoLista.id)
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
        ith.attachToRecyclerView(binding.recyclerViewSalonesTPV)
    }

//---------------------------------------------------------------------------------------------------

    private fun rellenarLista() {
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("salones")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaSalones.clear()
                if (snapshot.exists()){
                    for (item in snapshot.children){
                        val salon = item.getValue(DatosSalon::class.java)
                        if (salon != null) {
                            listaSalones.add(salon)
                            elemento = salon?.id!!
                        }
                    }

                    binding.recyclerViewSalonesTPV.adapter = SalonAdapter(listaSalones)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
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