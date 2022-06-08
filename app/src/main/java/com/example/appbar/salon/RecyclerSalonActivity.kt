package com.example.appbar.salon

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
import com.example.appbar.databinding.ActivityRecyclerSalonBinding
import com.example.appbar.recycler_salon.DatosSalon
import com.example.appbar.recycler_salon.SalonAdapter
import com.google.firebase.database.*

class RecyclerSalonActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecyclerSalonBinding

    //Para la conexión con Realtimedatabsae
    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var listaSalones: MutableList<DatosSalon>
    var contador = 0
    var elemento = 0
    lateinit var prefs : Prefs


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerSalonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaSalones = mutableListOf<DatosSalon>()
        prefs = Prefs(this)
        setRecycler()
        swipeRecycler()
        rellenarLista()

    }

//---------------------------------------------------------------------------------------------------

    private fun guardarElementos() {
        prefs.guardarContadorIdSalon(elemento)
    }


//---------------------------------------------------------------------------------------------------

    private fun setRecycler() {
        binding.recyclerViewSalonesTPV.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSalonesTPV.setHasFixedSize(true)
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
                        guardarElementos()
                    }

                    binding.recyclerViewSalonesTPV.adapter = SalonAdapter(listaSalones)

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

//---------------------------------------------------------------------------------------------------

    private fun swipeRecycler() {
        val itemTouch = object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or (ItemTouchHelper.RIGHT)){
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
                    val alertDialog = AlertDialog.Builder(this@RecyclerSalonActivity)
                        .setTitle(R.string.titulo_borrar_salon)
                        .setMessage(R.string.mensaje_borrar_salon)
                        .setNegativeButton(R.string.no_borrar_salon){v,_->v.dismiss()
                            setRecycler()
                        }
                        .setPositiveButton(R.string.si_borrar_salon) { _, _ ->
                            //Borramos el elemento, hemos realizado el método removeAt en el Adapter
                            val position = viewHolder.adapterPosition
                            val adapter = binding.recyclerViewSalonesTPV.adapter as SalonAdapter
                            adapter.removeAt(position)
                            guardarElementos()
                            setRecycler()

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                            if ((listaSalones.isNullOrEmpty() || listaSalones.size == 1) && contador == 0) {
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
                    ItemTouchHelper.RIGHT -> {
                        val position = viewHolder.adapterPosition
                        val elementoLista = listaSalones[position]

                        val i = Intent (this@RecyclerSalonActivity, ModificarSalonActivity::class.java).apply {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_recycler_salones, menu)
        return true
    }

//---------------------------------------------------------------------------------------------------

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.volver -> {
                val i = Intent (this, MenuInicialActivity::class.java)
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

    private fun confirmarBorrado() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.titulo_borrar_salon)
            .setMessage(R.string.mensaje_borrar_salon)
            .setNegativeButton(R.string.no_borrar_salon){v,_->v.dismiss()
            }
            .setPositiveButton(R.string.si_borrar_salon) { _, _ ->
                finishAffinity()
            }
            .setCancelable(false)
            .create()
            .show()
    }

//---------------------------------------------------------------------------------------------------

}