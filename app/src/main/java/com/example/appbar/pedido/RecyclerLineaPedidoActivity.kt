package com.example.appbar.pedido

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionGroupInfo
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.text.TextPaint
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appbar.MainActivity
import com.example.appbar.R
import com.example.appbar.databinding.ActivityRecyclerLineaPedidoBinding
import com.example.appbar.email.EmailPrincipalActivity
import com.example.appbar.recycler_articulos.ArticuloAdapter
import com.example.appbar.recycler_pedidos.DatosPedido
import com.example.appbar.recycler_pedidos.PedidoAdapter
import com.google.firebase.database.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class RecyclerLineaPedidoActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecyclerLineaPedidoBinding
    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    lateinit var listaLineaPedido: MutableList<DatosPedido>
    lateinit var pedido: DatosPedido

    var nombreSalonPedido = ""
    var total = 0.0
    var cantidad = 0
    var precio = 0.0
    var totalProducto = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerLineaPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaLineaPedido = mutableListOf()
        cogerNombreSalon()
        rellenarLista()
        setListeners()
        swipeRecycler()
        setTitle(getString(R.string.titulo_lineas_pedido))
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
                        val adapter = binding.recyclerViewLineasPedidos.adapter as PedidoAdapter
                        adapter.removeAt(position)
                        setRecycler()
                    }

                    ItemTouchHelper.LEFT -> {
                        val position = viewHolder.adapterPosition
                        val adapter = binding.recyclerViewLineasPedidos.adapter as PedidoAdapter
                        adapter.removeAt(position)
                        setRecycler()
                    }

                    else ->{

                    }
                }
            }
        }
        val ith = ItemTouchHelper(itemTouch)
        ith.attachToRecyclerView(binding.recyclerViewLineasPedidos)
    }

//---------------------------------------------------------------------------------------------------

    private fun setListeners() {
        binding.btnPdfFactura.setOnClickListener {
            pagar()
            Toast.makeText(this, "pedido borrado", Toast.LENGTH_LONG).show()
        }

        binding.btnEnviarEmailFactura.setOnClickListener {
            val i = Intent (this, EmailPrincipalActivity::class.java)
            startActivity(i)
        }

        binding.btnVolverFactura.setOnClickListener {
            val i = Intent (this, RecyclerPedidosActivity::class.java)
            startActivity(i)
        }
    }

//---------------------------------------------------------------------------------------------------

    private fun pagar() {
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("pedidos")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaLineaPedido.clear()
                if (snapshot.exists()){
                    for (item2 in snapshot.children){
                        pedido = item2.getValue(DatosPedido::class.java)!!
                        if (pedido != null) {
                            Log.d("::::", pedido.nombreSalon.toString())
                            Log.d("::::", nombreSalonPedido.toString())
                           if (pedido.nombreSalon?.let { nombreSalonPedido.compareTo(it) } == 0) {
                                listaLineaPedido.removeAll(listOf(pedido))
                                reference = db.getReference("pedidos").child(pedido.id.toString()!!)
                                reference.removeValue()
                            }
                        }
                    }
                    setRecycler()
                    ponerTotalPagado()
                    binding.recyclerViewLineasPedidos.adapter = PedidoAdapter(listaLineaPedido)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

//---------------------------------------------------------------------------------------------------

    private fun ponerTotal() {
        binding.totalproducto.setText(total.toString() + "€")
    }

//---------------------------------------------------------------------------------------------------

   private fun ponerTotalPagado() {
        binding.totalproducto.setText("")
    }

//---------------------------------------------------------------------------------------------------

    private fun cogerNombreSalon() {
        val bundle = intent.extras
        nombreSalonPedido = bundle?.getString("nombreSalon").toString()
    }

//---------------------------------------------------------------------------------------------------

    private fun setRecycler() {
        binding.recyclerViewLineasPedidos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLineasPedidos.setHasFixedSize(true)
    }

//---------------------------------------------------------------------------------------------------

    private fun rellenarLista() {
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("pedidos")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaLineaPedido.clear()
                if (snapshot.exists()){
                    total = 0.0
                    for (item in snapshot.children){
                        pedido = item.getValue(DatosPedido::class.java)!!
                        if (pedido != null) {
                            if (pedido.nombreSalon?.compareTo(nombreSalonPedido) ?: null == 0) {
                                listaLineaPedido.add(pedido)
                                cantidad = pedido.cantidadArticulo!!
                                precio = pedido.precioArticulo!!
                                totalProducto = cantidad * precio
                                total += totalProducto
                                ponerTotal()
                            }
                        }
                    }
                    setRecycler()
                    binding.recyclerViewLineasPedidos.adapter = PedidoAdapter(listaLineaPedido)
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
                val i = Intent (this, RecyclerPedidosActivity::class.java)
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