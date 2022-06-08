package com.example.appbar.recycler_pedidos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appbar.R
import com.example.appbar.databinding.RecyclerListaLineaPedidoBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat

class PedidoAdapter (private val lista: MutableList<DatosPedido>): RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {

//---------------------------------------------------------------------------------------------------

    lateinit var context : Context
    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference

//---------------------------------------------------------------------------------------------------

    class ViewHolder (v: View) : RecyclerView.ViewHolder(v) {
        val binding = RecyclerListaLineaPedidoBinding.bind(v)
    }

//---------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.recycler_lista_linea_pedido, parent, false)
        return ViewHolder(v)
    }

//---------------------------------------------------------------------------------------------------

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.txtNombreArticulo.text = item.nombreArticulo.toString()
        holder.binding.txtPrecioArticulo.text = item.precioArticulo.toString() + "€"

        holder.binding.txtCantidadArticulo.text = item.cantidadArticulo.toString()
        var cantidad = 0
        var precio = 0.0
        var total = 0.0

        cantidad = item.cantidadArticulo.toString().toInt()
        precio  = item.precioArticulo.toString().toDouble()
        total = cantidad * precio
        holder.binding.txtTotalArticulo.text = total.toString() + "€"
    }

//---------------------------------------------------------------------------------------------------

    override fun getItemCount(): Int {
        return lista.count()
    }

//---------------------------------------------------------------------------------------------------

    public fun removeAt(position: Int){
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("pedidos").child(lista[position].id.toString())
        reference.removeValue()
        notifyItemRemoved(position)
    }

//---------------------------------------------------------------------------------------------------
    public fun removePagado(){
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("pedidos")
        reference.removeValue()
    }
}